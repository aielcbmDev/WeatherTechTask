package com.charly.weatherapp.ui.detailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.charly.domain.usecases.GetDailyWeatherForecastByIdUseCase
import com.charly.weatherapp.formatdata.DateFormatter
import com.charly.weatherapp.formatdata.TimeFormatter
import com.charly.weatherapp.mappers.mapToDailyForecastModel
import com.charly.weatherapp.model.DailyForecastModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getString
import volkswagentechtask.feature_weather.weatherapp.generated.resources.Res
import volkswagentechtask.feature_weather.weatherapp.generated.resources.data_not_available_text

class DetailViewModel(
    private val itemId: Long,
    private val getDailyWeatherForecastByIdUseCase: GetDailyWeatherForecastByIdUseCase,
    private val dateFormatter: DateFormatter,
    private val timeFormatter: TimeFormatter
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        _state.update { it.copy(detailUiState = DetailUiState.Error) }
    }

    private val _state = MutableStateFlow(DetailScreenState())
    val state: StateFlow<DetailScreenState> = _state.asStateFlow()

    init {
        fetchDailyWeatherForecastById()
    }

    fun handleIntent(viewIntent: DetailViewIntent) {
        when (viewIntent) {
            is DetailViewIntent.FetchDailyWeatherForecastById -> fetchDailyWeatherForecastById()
        }
    }

    private fun fetchDailyWeatherForecastById() {
        _state.update { it.copy(detailUiState = DetailUiState.Loading) }
        viewModelScope.launch(exceptionHandler) {
            val dailyForecastModel = withContext(Dispatchers.IO) {
                getDailyWeatherForecastByIdUseCase.execute(itemId)
                    .mapToDailyForecastModel(
                        dateFormatter = dateFormatter,
                        timeFormatter = timeFormatter,
                        noDataAvailable = getString(Res.string.data_not_available_text)
                    )
            }
            _state.update {
                it.copy(detailUiState = DetailUiState.Success(dailyForecastModel = dailyForecastModel))
            }
        }
    }
}

data class DetailScreenState(
    val detailUiState: DetailUiState = DetailUiState.Loading
)

sealed interface DetailUiState {
    data class Success(
        val dailyForecastModel: DailyForecastModel,
    ) : DetailUiState

    object Loading : DetailUiState
    object Error : DetailUiState
}

sealed interface DetailViewIntent {
    object FetchDailyWeatherForecastById : DetailViewIntent
}
