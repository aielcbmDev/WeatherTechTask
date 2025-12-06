package com.charly.navigation

import androidx.navigation3.runtime.NavKey

data object MainScreenKey : NavKey
data class DetailScreenKey(val id: Long) : NavKey
