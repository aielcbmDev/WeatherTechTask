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
import volkswagentechtask.feature_weather.weatherapp.generated.resources.Res
import volkswagentechtask.feature_weather.weatherapp.generated.resources.data_not_available_text

class MainViewModel(
    private val getDailyWeatherForecastListUseCase: GetDailyWeatherForecastListUseCase,
    private val dateFormatter: DateFormatter,
    private val stringProvider: StringProvider,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _state.update {
            if (it.mainUiState is MainUiState.Success) {
                it.copy(mainUiState = it.mainUiState.copy(isSnackBarVisible = true))
            } else {
                it.copy(mainUiState = MainUiState.Error)
            }
        }
    }

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        handleIntent(MainViewIntent.FetchDailyWeatherForecast)
    }

    fun handleIntent(mainViewIntent: MainViewIntent) {
        when (mainViewIntent) {
            is MainViewIntent.FetchDailyWeatherForecast -> fetchDailyWeatherForecast()
        }
    }

    private fun fetchDailyWeatherForecast() {
        _state.update {
            if (it.mainUiState is MainUiState.Success) {
                it
            } else {
                it.copy(mainUiState = MainUiState.Loading)
            }
        }
        viewModelScope.launch(exceptionHandler) {
            getDailyWeatherForecastListUseCase.execute()
                .map {
                    val noDataAvailable =
                        stringProvider.getStringForResource(Res.string.data_not_available_text)
                    it.mapToDailyForecastMainModelList(dateFormatter, noDataAvailable)
                }
                .flowOn(ioDispatcher)
                .collect { dailyForecastMainModelList ->
                    _state.update {
                        it.copy(mainUiState = MainUiState.Success(dailyForecastMainModelList = dailyForecastMainModelList))
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
        val isSnackBarVisible: Boolean = false
    ) : MainUiState

    object Loading : MainUiState
    object Error : MainUiState
}

sealed interface MainViewIntent {
    object FetchDailyWeatherForecast : MainViewIntent
}
