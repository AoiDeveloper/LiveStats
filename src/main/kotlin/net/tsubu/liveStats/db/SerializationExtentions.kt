package net.tsubu.liveStats.db

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import net.tsubu.liveStats.api.PropertyKey
import net.tsubu.liveStats.api.StatsProperties
import net.tsubu.liveStats.data.PlayerData
import java.util.UUID

// JSONの変換設定
private val json =
    Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

@Suppress("UNCHECKED_CAST")
private fun <T> encodeValue(
    key: PropertyKey<T>,
    value: Any?,
): JsonElement = json.encodeToJsonElement(key.serializer, value as T)

fun PlayerData.toDTO(): PlayerDataDTO {
    val propsMap =
        this
            .getAllProperties()
            .mapValues { (key, prop) ->
                encodeValue(key, prop.value)
            }.mapKeys { (key, _) -> key.name.toString() }

    return PlayerDataDTO(this.uuid.toString(), propsMap)
}

fun PlayerDataDTO.toPlayerData(): PlayerData {
    val playerData = PlayerData(UUID.fromString(this.uuid))
    this.properties.forEach { (keyName, jsonElement) ->
        val propertyKey = StatsProperties.findByName(keyName)

        if (propertyKey != null) {
            val value =
                json.decodeFromJsonElement(propertyKey.serializer, jsonElement) ?: error("Failed to deserialize property $keyName.")

            @Suppress("UNCHECKED_CAST")
            (playerData.getProperty(propertyKey as PropertyKey<Any>)).update(value)
        }
    }
    return playerData
}
