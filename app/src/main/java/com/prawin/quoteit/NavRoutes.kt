package com.prawin.quoteit

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Navigation route definitions for the QuoteIt app.
 * Each route implements NavKey to integrate with Navigation 3.
 */
@Serializable
data object Home : NavKey

@Serializable
data object Tags : NavKey

@Serializable
data object Saved : NavKey

/** Route for the quote share/preview screen. [quote] is the quote text to display. */
@Serializable
data class Share(val quote: String) : NavKey

/** Route for the saved quotes detail screen. [tag] is the category tag name. */
@Serializable
data class SavedDetail(val tag: String) : NavKey
