package com.something.volkswagentechtask.previews.weatherapp.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.charly.weatherapp.ui.detail.DetailScreenState
import com.charly.weatherapp.ui.detail.screen.DetailScreen

@Preview
@Composable
fun DetailScreenPreview(
    @PreviewParameter(DetailScreenParameterProvider::class) detailScreenState: DetailScreenState,
    onRetryButtonClicked: () -> Unit = {},
    onBackButtonClicked: () -> Unit = {}
) {
    DetailScreen(
        detailScreenState = detailScreenState,
        onRetryButtonClicked = onRetryButtonClicked,
        onBackButtonClicked = onBackButtonClicked
    )
}
