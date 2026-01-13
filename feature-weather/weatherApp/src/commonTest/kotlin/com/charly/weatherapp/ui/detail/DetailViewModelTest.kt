package com.charly.weatherapp.ui.detail

import com.charly.domain.model.DailyForecast
import com.charly.domain.usecases.GetDailyWeatherForecastByIdUseCase
import com.charly.weatherapp.formatdata.datetime.DateFormatter
import com.charly.weatherapp.formatdata.datetime.TimeFormatter
import com.charly.weatherapp.formatdata.degrees.DegreesFormatter
import com.charly.weatherapp.formatdata.speed.SpeedFormatter
import com.charly.weatherapp.formatdata.temperature.TemperatureFormatter
import com.charly.weatherapp.provider.StringProvider
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import weathertechtask.feature_weather.weatherapp.generated.resources.Res
import weathertechtask.feature_weather.weatherapp.generated.resources.data_not_available_text
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

private const val DAILY_FORECAST =
    "{\"id\":16,\"dt\":1764432000,\"sunrise\":1764417544,\"sunset\":1764451812,\"summary\":\"The day will start with clear sky through the late morning hours, transitioning to partly cloudy\",\"minTemp\":\"1\",\"maxTemp\":\"6.7\",\"windSpeed\":\"6.59\",\"windDeg\":\"279\",\"windGust\":\"12.42\"}"

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Verify that fetching a daily weather forecast by id succeeds`() = runTest {
        // GIVEN
        val dailyForecast = Json.decodeFromString<DailyForecast>(DAILY_FORECAST)
        val itemId = 5L
        val getDailyWeatherForecastByIdUseCase = mock<GetDailyWeatherForecastByIdUseCase> {
            everySuspend { execute(itemId) } returns dailyForecast
        }
        val dateFormatter = mock<DateFormatter> {
            everySuspend { formatEpochSecondsToDateString(dailyForecast.dt) } returns "27/11/2025"
        }
        val timeFormatter = mock<TimeFormatter> {
            everySuspend { formatEpochSecondsToTimeString(epochSeconds = any()) } returns "12:56:56"
        }
        val speedFormatter = mock<SpeedFormatter> {
            everySuspend { formatSpeed(speed = any()) } returns "8.55meter/sec"
        }
        val temperatureFormatter = mock<TemperatureFormatter> {
            everySuspend { formatTemperature(temperature = any()) } returns "3.12ºC"
        }
        val degreesFormatter = mock<DegreesFormatter> {
            everySuspend { formatDegrees(degrees = any()) } returns "275º"
        }
        val stringProvider = mock<StringProvider> {
            everySuspend { getStringForResource(Res.string.data_not_available_text) } returns "No data available"
        }
        val detailViewModel = DetailViewModel(
            itemId = itemId,
            getDailyWeatherForecastByIdUseCase = getDailyWeatherForecastByIdUseCase,
            dateFormatter = dateFormatter,
            timeFormatter = timeFormatter,
            speedFormatter = speedFormatter,
            temperatureFormatter = temperatureFormatter,
            degreesFormatter = degreesFormatter,
            stringProvider = stringProvider,
            ioDispatcher = testDispatcher
        )

        // WHEN
        detailViewModel.handleIntent(DetailViewIntent.FetchDailyWeatherForecastById)
        assertIs<DetailUiState.Loading>(detailViewModel.state.value.detailUiState)
        runCurrent()

        // THEN
        val mainUiState = detailViewModel.state.value.detailUiState
        assertIs<DetailUiState.Success>(mainUiState)
        val dailyForecastDetailModel = mainUiState.dailyForecastDetailModel
        assertEquals(16, dailyForecastDetailModel.id)
        assertEquals("27/11/2025", dailyForecastDetailModel.dt)
        assertEquals("12:56:56", dailyForecastDetailModel.sunrise)
        assertEquals("12:56:56", dailyForecastDetailModel.sunset)
        assertEquals(
            "The day will start with clear sky through the late morning hours, transitioning to partly cloudy",
            dailyForecastDetailModel.summary
        )
        assertEquals("3.12ºC", dailyForecastDetailModel.minTemp)
        assertEquals("3.12ºC", dailyForecastDetailModel.maxTemp)
        assertEquals("8.55meter/sec", dailyForecastDetailModel.windSpeed)
        assertEquals("275º", dailyForecastDetailModel.windDeg)
        assertEquals("8.55meter/sec", dailyForecastDetailModel.windGust)
    }

    @Test
    fun `Verify that fetching a daily weather forecast by id fails`() = runTest {
        val itemId = 5L
        val getDailyWeatherForecastByIdUseCase = mock<GetDailyWeatherForecastByIdUseCase> {
            everySuspend { execute(itemId) } throws Exception()
        }
        val dateFormatter = mock<DateFormatter>()
        val timeFormatter = mock<TimeFormatter>()
        val speedFormatter = mock<SpeedFormatter>()
        val temperatureFormatter = mock<TemperatureFormatter>()
        val degreesFormatter = mock<DegreesFormatter>()
        val stringProvider = mock<StringProvider>()
        val detailViewModel = DetailViewModel(
            itemId = itemId,
            getDailyWeatherForecastByIdUseCase = getDailyWeatherForecastByIdUseCase,
            dateFormatter = dateFormatter,
            timeFormatter = timeFormatter,
            speedFormatter = speedFormatter,
            temperatureFormatter = temperatureFormatter,
            degreesFormatter = degreesFormatter,
            stringProvider = stringProvider,
            ioDispatcher = testDispatcher
        )

        // WHEN
        detailViewModel.handleIntent(DetailViewIntent.FetchDailyWeatherForecastById)
        assertIs<DetailUiState.Loading>(detailViewModel.state.value.detailUiState)
        runCurrent()

        // THEN
        val mainUiState = detailViewModel.state.value.detailUiState
        assertIs<DetailUiState.Error>(mainUiState)
    }
}
