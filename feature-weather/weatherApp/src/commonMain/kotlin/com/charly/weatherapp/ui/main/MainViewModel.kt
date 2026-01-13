package com.charly.weatherapp.ui.main

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charly.domain.usecases.GetDailyWeatherForecastListUseCase
import com.charly.weatherapp.formatdata.datetime.DateFormatter
import com.charly.weatherapp.provider.StringProvider
import com.charly.weatherapp.ui.main.mappers.mapToDailyForecastMainModelList
import com.charly.weatherapp.ui.main.model.DailyForecastMainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import weathertechtask.feature_weather.weatherapp.generated.resources.Res
import weathertechtask.feature_weather.weatherapp.generated.resources.data_not_available_text

class MainViewModel(
    private val getDailyWeatherForecastListUseCase: GetDailyWeatherForecastListUseCase,
    private val dateFormatter: DateFormatter,
    private val stringProvider: StringProvider,
    private val mainViewModelReducer: MainViewModelReducer,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _state.update { mainViewModelReducer.reduce(it, Action.FetchError) }
    }

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        handleIntent(MainViewIntent.FetchDailyWeatherForecast(false))
    }

    fun handleIntent(mainViewIntent: MainViewIntent) {
        when (mainViewIntent) {
            is MainViewIntent.FetchDailyWeatherForecast -> fetchDailyWeatherForecast(mainViewIntent.invalidateCache)
        }
    }

    private fun fetchDailyWeatherForecast(invalidateCache: Boolean) {
        _state.update { mainViewModelReducer.reduce(it, Action.Fetching) }
        viewModelScope.launch(exceptionHandler) {
            getDailyWeatherForecastListUseCase.execute(invalidateCache)
                .map {
                    val noDataAvailable =
                        stringProvider.getStringForResource(Res.string.data_not_available_text)
                    it.mapToDailyForecastMainModelList(dateFormatter, noDataAvailable)
                }
                .flowOn(ioDispatcher)
                .collect { dailyForecastMainModelList ->
                    _state.update {
                        mainViewModelReducer.reduce(
                            it,
                            Action.FetchSuccess(dailyForecastMainModelList)
                        )
                    }
                }
        }
    }
}

@Immutable
data class MainScreenState(
    val mainUiState: MainUiState = MainUiState.Loading
)

@Immutable
sealed interface MainUiState {
    data class Success(
        val dailyForecastMainModelList: List<DailyForecastMainModel>,
        val isSnackBarVisible: Boolean = false,
        val isRefreshing: Boolean = false
    ) : MainUiState

    data object Loading : MainUiState
    data object Error : MainUiState
}

sealed interface MainViewIntent {
    data class FetchDailyWeatherForecast(val invalidateCache: Boolean) : MainViewIntent
}
