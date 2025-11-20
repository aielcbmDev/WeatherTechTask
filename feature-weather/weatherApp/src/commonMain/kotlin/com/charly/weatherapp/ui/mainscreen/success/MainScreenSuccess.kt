package com.charly.weatherapp.ui.mainscreen.success

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.charly.weatherapp.model.DailyForecastModel
import org.jetbrains.compose.resources.stringResource
import volkswagentechtask.feature_weather.weatherapp.generated.resources.Res
import volkswagentechtask.feature_weather.weatherapp.generated.resources.main_screen_top_app_bar_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenSuccess(
    dailyForecastModelList: List<DailyForecastModel>,
    onDailyForecastModelClick: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.main_screen_top_app_bar_title)) },
                colors = TopAppBarDefaults.topAppBarColors(titleContentColor = Color.Red)
            )
        }
    ) { padding ->
        val state = rememberLazyListState()
        LazyColumn(
            state = state,
            modifier = Modifier.padding(padding)
        ) {
            items(
                items = dailyForecastModelList,
                key = { item -> item.id }
            ) { dailyForecastModel ->
                DailyItem(
                    dailyForecastModel = dailyForecastModel,
                    onDailyForecastModelClick = onDailyForecastModelClick
                )
            }
        }
    }
}

@Composable
fun DailyItem(
    dailyForecastModel: DailyForecastModel,
    onDailyForecastModelClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onDailyForecastModelClick.invoke(dailyForecastModel.id) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = dailyForecastModel.dt,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = modifier.padding(top = 12.dp, bottom = 8.dp),
            )

            Text(
                text = dailyForecastModel.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
