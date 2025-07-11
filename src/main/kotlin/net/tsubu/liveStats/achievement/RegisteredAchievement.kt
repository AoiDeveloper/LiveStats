package net.tsubu.liveStats.achievement

import net.tsubu.liveStats.api.Achievement
import net.tsubu.liveStats.api.PropertyAccessor
import net.tsubu.liveStats.api.PropertyKey

/**
 * Represents a registered achievement containing its definition, required properties, and
 * a condition to determine when the achievement is unlocked.
 *
 * This data class plays a vital role in the achievement system by binding an achievement
 * to the properties it depends on and the logic needed to evaluate its completion criteria.
 *
 * @property achievement The achievement definition including its ID, name, and description.
 * @property requiredProperties A list of property keys required to evaluate the condition.
 * These represent the data sources that influence the achievement's status.
 * @property condition A lambda function defining the condition to unlock the achievement.
 * It accepts a PropertyAccessor instance which provides access to the current values of
 * properties specified in `requiredProperties` and returns a boolean indicating whether
 * the condition is met.
 */
data class RegisteredAchievement(
    val achievement: Achievement,
    val requiredProperties: List<PropertyKey<*>>,
    val condition: (accessor: PropertyAccessor) -> Boolean,
)
