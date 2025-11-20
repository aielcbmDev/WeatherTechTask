package com.charly.weatherapp.ui.mainscreen.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import volkswagentechtask.feature_weather.weatherapp.generated.resources.Res
import volkswagentechtask.feature_weather.weatherapp.generated.resources.any_error
import volkswagentechtask.feature_weather.weatherapp.generated.resources.error_fix_suggestion
import volkswagentechtask.feature_weather.weatherapp.generated.resources.retry_button_text

@Composable
fun MainScreenError(
    onRetryButtonClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.any_error),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.error_fix_suggestion),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = {
                onRetryButtonClicked.invoke()
            }
        ) {
            Text(
                text = stringResource(Res.string.retry_button_text),
                fontSize = 14.sp
            )
        }
    }
}

@Preview
@Composable
fun MainScreenErrorPreview(
    onRetryButtonClicked: () -> Unit = {}
) {
    MainScreenError(
        onRetryButtonClicked = onRetryButtonClicked
    )
}
