package com.charly.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object MainScreenKey : NavKey

@Serializable
data class DetailScreenKey(val id: Long) : NavKey
