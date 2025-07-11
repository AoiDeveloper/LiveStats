package net.tsubu.liveStats.api

import kotlinx.serialization.KSerializer
import org.bukkit.plugin.java.JavaPlugin

/**
 * Represents a key associated with a property and its corresponding serializer.
 * This class is used to define a property uniquely using its name and the serializer
 * responsible for handling the property's value type.
 *
 * @param T The type of the value associated with the property.
 * @property name The unique name of the property.
 * @property serializer The serializer used to handle the property's value type.
 */
data class PropertyKey<T>(
    val name: Identifier,
    val serializer: KSerializer<T>,
    val defaultValue: T,
) {
    /**
     * Represents an identifier that uniquely defines a property within a plugin.
     *
     * @property pluginId The unique ID of the plugin associated with the property.
     * @property propertyName The name of the property within the specified plugin.
     */
    @ConsistentCopyVisibility
    data class Identifier private constructor(
        val plugin: String,
        val propertyName: String,
    ) {
        companion object {
            private val SNAKE_CASE_REGEX = Regex("^[a-z0-9_]+$")

            fun of(
                plugin: JavaPlugin,
                propertyName: String,
            ): Identifier = Identifier(plugin.name, propertyName)

            @Deprecated(
                "This method is deprecated. Use of(plugin: JavaPlugin, propertyName: String) instead normally. Only if you changed your plugin name, you can use this method.",
                ReplaceWith("of(plugin, propertyName)"),
            )
            fun of(
                pluginId: String,
                propertyName: String,
            ): Identifier = Identifier(pluginId, propertyName)
        }

        init {
            require(propertyName.isNotBlank()) { "Property name must not be blank." }
            require(SNAKE_CASE_REGEX.matches(propertyName)) { "Property name must be snake_case." }
        }

        override fun toString(): String = "$plugin:$propertyName"
    }
}
