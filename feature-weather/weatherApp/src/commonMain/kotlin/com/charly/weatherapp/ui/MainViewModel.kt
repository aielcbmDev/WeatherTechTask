package com.charly.weatherapp.ui

import androidx.lifecycle.ViewModel

class MainViewModel(
    private val weatherApiKey: String
) : ViewModel() {

    init {
        val x = weatherApiKey
    }
}
