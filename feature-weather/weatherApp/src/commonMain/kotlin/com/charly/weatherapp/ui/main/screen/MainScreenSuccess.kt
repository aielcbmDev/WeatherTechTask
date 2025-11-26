package com.charly.weatherapp.ui.main.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.charly.uitheme.Dimensions
import com.charly.uitheme.TypographySize
import com.charly.weatherapp.ui.main.model.DailyForecastMainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import volkswagentechtask.feature_weather.weatherapp.generated.resources.Res
import volkswagentechtask.feature_weather.weatherapp.generated.resources.main_screen_snack_bar_action_label
import volkswagentechtask.feature_weather.weatherapp.generated.resources.main_screen_snack_bar_message
import volkswagentechtask.feature_weather.weatherapp.generated.resources.main_screen_top_app_bar_title

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenSuccess(
    dailyForecastMainModelList: List<DailyForecastMainModel>,
    isSnackBarVisible: Boolean,
    isRefreshing: Boolean,
    onDailyForecastModelClick: (Long) -> Unit,
    onRetryButtonClicked: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.main_screen_top_app_bar_title)) },
                colors = TopAppBarDefaults.topAppBarColors(titleContentColor = Color.Red)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        PullToRefreshList(
            isRefreshing = isRefreshing,
            dailyForecastMainModelList = dailyForecastMainModelList,
            onDailyForecastModelClick = onDailyForecastModelClick,
            onRetryButtonClicked = onRetryButtonClicked,
            padding = padding
        )
        DisplaySnackBar(
            coroutineScope = scope,
            snackBarHostState = snackBarHostState,
            isSnackBarVisible = isSnackBarVisible,
            onRetryButtonClicked = onRetryButtonClicked
        )
    }
}

@Composable
private fun PullToRefreshList(
    isRefreshing: Boolean,
    dailyForecastMainModelList: List<DailyForecastMainModel>,
    onDailyForecastModelClick: (Long) -> Unit,
    onRetryButtonClicked: () -> Unit,
    padding: PaddingValues
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { onRetryButtonClicked.invoke() }
    ) {
        val state = rememberLazyListState()
        LazyColumn(
            state = state,
            modifier = Modifier.padding(padding)
        ) {
            items(
                items = dailyForecastMainModelList,
                key = { item -> item.id }
            ) { dailyForecastMainModel ->
                DailyItem(
                    dailyForecastMainModel = dailyForecastMainModel,
                    onDailyForecastModelClick = onDailyForecastModelClick
                )
            }
        }
    }
}

@Composable
private fun DisplaySnackBar(
    coroutineScope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    isSnackBarVisible: Boolean,
    onRetryButtonClicked: () -> Unit
) {
    if (isSnackBarVisible) {
        coroutineScope.launch {
            val result = snackBarHostState
                .showSnackbar(
                    message = getString(Res.string.main_screen_snack_bar_message),
                    actionLabel = getString(Res.string.main_screen_snack_bar_action_label),
                    duration = SnackbarDuration.Indefinite
                )
            when (result) {
                SnackbarResult.ActionPerformed -> onRetryButtonClicked.invoke()
                else -> {}
            }
        }
    }
}

@Composable
private fun DailyItem(
    dailyForecastMainModel: DailyForecastMainModel,
    onDailyForecastModelClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onDailyForecastModelClick.invoke(dailyForecastMainModel.id) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = Dimensions.large)
        ) {
            Text(
                text = dailyForecastMainModel.dt,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                fontSize = TypographySize.title,
                modifier = modifier.padding(top = Dimensions.medium, bottom = Dimensions.small),
            )

            Text(
                text = dailyForecastMainModel.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = TypographySize.body
            )
        }
    }
}
