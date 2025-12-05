package com.charly.navigation

sealed interface ScreenKey

data object MainScreenKey : ScreenKey
data class DetailScreenKey(val id: Long) : ScreenKey
