package com.something.volkswagentechtask.previews.weatherapp.error

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.charly.weatherapp.ui.common.error.ScreenError

@Preview
@Composable
fun ScreenErrorPreview(
    onRetryButtonClicked: () -> Unit = {}
) {
    ScreenError(onRetryButtonClicked)
}
