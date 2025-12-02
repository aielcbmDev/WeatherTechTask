package com.something.volkswagentechtask.previews.weatherapp.main.success

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.charly.weatherapp.ui.main.model.DailyForecastMainModel

class DailyForecastMainModelListParameterProvider :
    PreviewParameterProvider<List<DailyForecastMainModel>> {

    override val values: Sequence<List<DailyForecastMainModel>> =
        generateDailyForecastMainModelList()

    private fun generateDailyForecastMainModelList(): Sequence<List<DailyForecastMainModel>> {
        return sequenceOf(
            listOf(
                DailyForecastMainModel(
                    id = 0,
                    dt = "22/11/2025",
                    summary = "Expect a day of partly cloudy with rain"
                ),
                DailyForecastMainModel(
                    id = 1,
                    dt = "23/11/2025",
                    summary = "Expect a day of partly cloudy with clear spells"
                ),
                DailyForecastMainModel(
                    id = 2,
                    dt = "24/11/2025",
                    summary = "Expect a day of partly cloudy with clear spells"
                ),
                DailyForecastMainModel(
                    id = 3,
                    dt = "25/11/2025",
                    summary = "You can expect partly cloudy in the morning, with rain in the afternoon"
                ),
                DailyForecastMainModel(
                    id = 4,
                    dt = "26/11/2025",
                    summary = "You can expect partly cloudy in the morning, with rain in the afternoon"
                )
            )
        )
    }
}
