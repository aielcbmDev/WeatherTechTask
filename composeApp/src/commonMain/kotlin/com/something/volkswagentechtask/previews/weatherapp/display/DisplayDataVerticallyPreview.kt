package com.something.volkswagentechtask.previews.weatherapp.display

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.charly.weatherapp.ui.common.display.DisplayDataVertically

@Preview
@Composable
fun DisplayDataVerticallyPreview(
    title: String = "Title",
    data: String = "Data"
) {
    DisplayDataVertically(
        title = title,
        data = data
    )
}
