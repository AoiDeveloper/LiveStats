package net.tsubu.liveStats.api

/**
 * Represents an achievement within the application.
 *
 * This class is used to define a specific in-game achievement with its unique identifier,
 * display name, and description. Achievements can be registered and associated with specific
 * conditions based on player properties using the `registerAchievement` function.
 *
 * @property id A unique identifier for the achievement. Should be represented in snake_case.
 * @property name The display name of the achievement in the application.
 * @property description A brief description of the achievement, providing details of its requirements or significance.
 */
data class Achievement(
    val id: String,
    val name: String,
    val description: String,
)
