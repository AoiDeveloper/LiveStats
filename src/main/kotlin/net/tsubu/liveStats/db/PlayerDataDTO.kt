package net.tsubu.liveStats.db

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class PlayerDataDTO(
    val uuid: String,
    // Propertyのキー(String)と値(JsonElement)を保存するマップ
    val properties: Map<String, JsonElement>,
)
