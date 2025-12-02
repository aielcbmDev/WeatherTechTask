package com.something.volkswagentechtask.previews.weatherapp.main.success

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.charly.weatherapp.ui.main.model.DailyForecastMainModel
import com.charly.weatherapp.ui.main.screen.MainScreenSuccess

@Preview
@Composable
fun MainScreenSuccessPreview(
    @PreviewParameter(DailyForecastMainModelListParameterProvider::class) dailyForecastMainModelList: List<DailyForecastMainModel>,
    isSnackBarVisible: Boolean = false,
    isRefreshing: Boolean = false,
    onDailyForecastModelClick: (Long) -> Unit = {},
    onRetryButtonClicked: () -> Unit = {}
) {
    MainScreenSuccess(
        dailyForecastMainModelList = dailyForecastMainModelList,
        isSnackBarVisible = isSnackBarVisible,
        isRefreshing = isRefreshing,
        onDailyForecastModelClick = onDailyForecastModelClick,
        onRetryButtonClicked = onRetryButtonClicked
    )
}
