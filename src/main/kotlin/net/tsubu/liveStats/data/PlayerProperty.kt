package net.tsubu.liveStats.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A generic class representing a player's property with observable state.
 *
 * This class manages a value of type `T` and provides functionality to observe and update it.
 *
 * @param T The type of the value the property holds.
 * @constructor Initializes a new instance of `PlayerProperty` with the given initial value.
 * @property flow A StateFlow object that allows observing the current value updates.
 * @property value A property that provides the current value of the property.
 *
 * @function update Updates the current value of the property.
 * @param newValue The new value to set.
 */
class PlayerProperty<T>(
    initialValue: T,
) {
    private val _value = MutableStateFlow(initialValue)
    val flow = _value.asStateFlow()

    fun update(newValue: T) {
        _value.value = newValue
    }

    val value: T
        get() = _value.value

    override fun toString(): String = value.toString()
}
