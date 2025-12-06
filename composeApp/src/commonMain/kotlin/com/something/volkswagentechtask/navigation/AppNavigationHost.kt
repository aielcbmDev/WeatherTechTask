package com.something.volkswagentechtask.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.charly.navigation.ScreenKey
import com.charly.navigation.ScreenKey.MainScreenKey
import com.charly.weatherapp.navigation.AddWeatherFeatureEntries

@Composable
fun AppNavigationHost() {
    val backStack = remember { mutableStateListOf<ScreenKey>(MainScreenKey) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            AddWeatherFeatureEntries(backStack)
        }
    )
}
