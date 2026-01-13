package com.charly.weatherapp.ui.common.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.charly.uitheme.TypographySize
import org.jetbrains.compose.resources.stringResource
import weathertechtask.feature_weather.weatherapp.generated.resources.Res
import weathertechtask.feature_weather.weatherapp.generated.resources.any_error
import weathertechtask.feature_weather.weatherapp.generated.resources.error_fix_suggestion
import weathertechtask.feature_weather.weatherapp.generated.resources.retry_button_text

@Composable
fun ScreenError(
    onRetryButtonClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(Res.string.any_error),
            fontSize = TypographySize.title,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.error_fix_suggestion),
            fontSize = TypographySize.title,
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
                fontSize = TypographySize.body
            )
        }
    }
}
