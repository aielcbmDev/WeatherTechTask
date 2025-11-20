package com.charly.weatherapp.ui.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charly.domain.usecases.DailyWeatherForecastUseCase
import com.charly.weatherapp.formatdata.TimeFormatter
import com.charly.weatherapp.mappers.mapToDailyForecastModelList
import com.charly.weatherapp.model.DailyForecastModel
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

class MainViewModel(
    private val weatherApiKey: String,
    private val dailyWeatherForecastUseCase: DailyWeatherForecastUseCase,
    private val timeFormatter: TimeFormatter
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _state.update { it.copy(uiState = UiState.Error) }
    }

    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()

    init {
        println("weatherApiKey: $weatherApiKey")
        fetchDailyWeatherForecast()
    }

    fun fetchDailyWeatherForecast() {
        _state.update { it.copy(uiState = UiState.Loading) }
        viewModelScope.launch(exceptionHandler) {
            dailyWeatherForecastUseCase.getDailyWeatherForecastList()
                .map { it.mapToDailyForecastModelList(timeFormatter, "n/a") }
                .flowOn(Dispatchers.IO)
                .collect { dailyForecastModelList ->
                    _state.update { it.copy(uiState = UiState.Success(dailyForecastModelList = dailyForecastModelList)) }
                }
        }
    }
}

data class MainScreenState(
    val uiState: UiState = UiState.Loading
)

sealed interface UiState {
    data class Success(
        val dailyForecastModelList: List<DailyForecastModel>,
    ) : UiState

    object Loading : UiState
    object Error : UiState
}
