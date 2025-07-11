package net.tsubu.liveStats.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import net.tsubu.liveStats.LiveStats
import net.tsubu.liveStats.data.PlayerData
import java.sql.DriverManager
import java.util.UUID

class DatabaseManager(
    private val plugin: LiveStats,
) {
    private val dbFile = plugin.dataFolder.resolve("statscore.db")
    private val dbUrl = "jdbc:sqlite:${dbFile.absolutePath}"
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun setupDatabase() =
        withContext(Dispatchers.IO) {
            plugin.dataFolder.mkdirs()
            connect().use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.execute(
                        """
                        CREATE TABLE IF NOT EXISTS player_data (
                            uuid TEXT PRIMARY KEY,
                            data TEXT NOT NULL
                        )
                        """.trimIndent(),
                    )
                }
            }
        }

    private fun connect() = DriverManager.getConnection(dbUrl)

    suspend fun savePlayerData(playerData: PlayerData) =
        withContext(Dispatchers.IO) {
            val dto = playerData.toDTO()
            val jsonString = json.encodeToString(dto)
            connect().use { conn ->
                conn
                    .prepareStatement("INSERT OR REPLACE INTO player_data (uuid, data) VALUES (?, ?)")
                    .use { pstmt ->
                        pstmt.setString(1, playerData.uuid.toString())
                        pstmt.setString(2, jsonString)
                        pstmt.executeUpdate()
                    }
            }
        }

    suspend fun loadPlayerData(uuid: UUID): PlayerData? =
        withContext(Dispatchers.IO) {
            connect().use { conn ->
                conn.prepareStatement("SELECT data FROM player_data WHERE uuid = ?").use { pstmt ->
                    pstmt.setString(1, uuid.toString())
                    val rs = pstmt.executeQuery()
                    if (rs.next()) {
                        val jsonString = rs.getString("data")
                        json.decodeFromString<PlayerDataDTO>(jsonString).toPlayerData()
                    } else {
                        null
                    }
                }
            }
        }
}
