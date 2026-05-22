package com.prawin.quoteit

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events (forward and back) by updating the [NavigationState].
 *
 * Follows Unidirectional Data Flow:
 * - [Navigator] handles events and updates [NavigationState].
 * - The UI (provided by NavDisplay) observes [NavigationState] and reacts to changes.
 */
class Navigator(val state: NavigationState) {

    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            // Top-level route — just switch to it
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks[state.topLevelRoute]
            ?: error("Stack for ${state.topLevelRoute} not found")
        val currentRoute = currentStack.last()
        // If at the base of the current route, go back to the start route stack
        if (currentRoute == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeLastOrNull()
        }
    }
}
