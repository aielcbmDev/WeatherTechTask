package com.something.volkswagentechtask.previews.weatherapp.detail

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.charly.weatherapp.ui.detail.DetailScreenState
import com.charly.weatherapp.ui.detail.DetailUiState
import com.charly.weatherapp.ui.detail.model.DailyForecastDetailModel

class DetailScreenParameterProvider : PreviewParameterProvider<DetailScreenState> {

    override val values: Sequence<DetailScreenState> = generateDetailScreenState()

    private fun generateDetailScreenState(): Sequence<DetailScreenState> {
        return sequenceOf(
            DetailScreenState(
                detailUiState = DetailUiState.Success(
                    dailyForecastDetailModel = generateDailyForecastDetailModel()
                )

            )
        )
    }

    private fun generateDailyForecastDetailModel(): DailyForecastDetailModel {
        return DailyForecastDetailModel(
            dt = "24/11/2025",
            sunrise = "11:53:38",
            sunset = "21:32:20",
            summary = "Expect a day of partly cloudy with clear spells",
            minTemp = "5.36ºC",
            maxTemp = "9.79ºC",
            windSpeed = "6.75meter/sec",
            windDeg = "12.25meter/sec",
            windGust = "325º"
        )
    }
}
