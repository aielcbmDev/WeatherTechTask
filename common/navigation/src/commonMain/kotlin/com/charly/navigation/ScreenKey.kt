package com.charly.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ScreenKey : NavKey {

    @Serializable
    object MainScreenKey : ScreenKey

    @Serializable
    data class DetailScreenKey(val id: Long) : ScreenKey
}
