package com.charly.weatherapp.ui.main

import com.charly.domain.model.DailyForecast
import com.charly.domain.usecases.GetDailyWeatherForecastListUseCase
import com.charly.weatherapp.formatdata.datetime.DateFormatterImpl
import com.charly.weatherapp.provider.StringProvider
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.TimeZone
import kotlinx.serialization.json.Json
import weathertechtask.feature_weather.weatherapp.generated.resources.Res
import weathertechtask.feature_weather.weatherapp.generated.resources.data_not_available_text
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

private const val DAILY_FORECAST_LIST =
    "[{\"id\":1,\"dt\":1763827200,\"sunrise\":1763812283,\"sunset\":1763847206,\"summary\":\"Expect a day of partly cloudy with rain\",\"minTemp\":\"5.64\",\"maxTemp\":\"10.6\",\"windSpeed\":\"5.44\",\"windDeg\":\"338\",\"windGust\":\"8.32\"},{\"id\":2,\"dt\":1763913600,\"sunrise\":1763898751,\"sunset\":1763933572,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"4.08\",\"maxTemp\":\"9.62\",\"windSpeed\":\"6.22\",\"windDeg\":\"294\",\"windGust\":\"12.17\"},{\"id\":3,\"dt\":1764000000,\"sunrise\":1763985218,\"sunset\":1764019940,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"5.36\",\"maxTemp\":\"9.79\",\"windSpeed\":\"6.75\",\"windDeg\":\"325\",\"windGust\":\"12.25\"},{\"id\":4,\"dt\":1764086400,\"sunrise\":1764071685,\"sunset\":1764106310,\"summary\":\"You can expect partly cloudy in the morning, with rain in the afternoon\",\"minTemp\":\"6.12\",\"maxTemp\":\"10.14\",\"windSpeed\":\"3.46\",\"windDeg\":\"168\",\"windGust\":\"10.93\"},{\"id\":5,\"dt\":1764172800,\"sunrise\":1764158151,\"sunset\":1764192682,\"summary\":\"You can expect partly cloudy in the morning, with rain in the afternoon\",\"minTemp\":\"10.07\",\"maxTemp\":\"12.87\",\"windSpeed\":\"2.3\",\"windDeg\":\"241\",\"windGust\":\"7.04\"},{\"id\":6,\"dt\":1764259200,\"sunrise\":1764244616,\"sunset\":1764279057,\"summary\":\"Expect a day of partly cloudy with rain\",\"minTemp\":\"3.29\",\"maxTemp\":\"13.01\",\"windSpeed\":\"8.51\",\"windDeg\":\"271\",\"windGust\":\"12.81\"},{\"id\":7,\"dt\":1764345600,\"sunrise\":1764331080,\"sunset\":1764365433,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"1.01\",\"maxTemp\":\"4.8\",\"windSpeed\":\"9\",\"windDeg\":\"274\",\"windGust\":\"14.63\"},{\"id\":8,\"dt\":1764432000,\"sunrise\":1764417544,\"sunset\":1764451812,\"summary\":\"Expect a day of partly cloudy with clear spells\",\"minTemp\":\"1.15\",\"maxTemp\":\"5.62\",\"windSpeed\":\"7.54\",\"windDeg\":\"305\",\"windGust\":\"14.61\"}]"

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

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
    fun `Verify that fetching the daily weather forecast list succeeds`() = runTest {
        // GIVEN
        val dailyForecastList = Json.decodeFromString<List<DailyForecast>>(DAILY_FORECAST_LIST)
        val getDailyWeatherForecastListUseCase = mock<GetDailyWeatherForecastListUseCase> {
            everySuspend { execute() } returns flowOf(dailyForecastList)
        }
        val dateFormatter = DateFormatterImpl(TimeZone.UTC)
        val stringProvider = mock<StringProvider> {
            everySuspend { getStringForResource(Res.string.data_not_available_text) } returns "Not available"
        }
        val mainViewModelReducer = MainViewModelReducer()

        // WHEN
        val mainViewModel = MainViewModel(
            getDailyWeatherForecastListUseCase = getDailyWeatherForecastListUseCase,
            dateFormatter = dateFormatter,
            stringProvider = stringProvider,
            mainViewModelReducer = mainViewModelReducer,
            ioDispatcher = testDispatcher
        )
        assertIs<MainUiState.Loading>(mainViewModel.state.value.mainUiState)
        runCurrent()

        // THEN
        val mainUiState = mainViewModel.state.value.mainUiState
        assertIs<MainUiState.Success>(mainUiState)
        val dailyForecastMainModelList = mainUiState.dailyForecastMainModelList
        assertEquals(8, mainUiState.dailyForecastMainModelList.size)
        val dailyForecastMainModel0 = dailyForecastMainModelList[0]
        assertEquals(1L, dailyForecastMainModel0.id)
        assertEquals("22/11/2025", dailyForecastMainModel0.dt)
        assertEquals("Expect a day of partly cloudy with rain", dailyForecastMainModel0.summary)

        val dailyForecastMainModel1 = dailyForecastMainModelList[1]
        assertEquals(2L, dailyForecastMainModel1.id)
        assertEquals("23/11/2025", dailyForecastMainModel1.dt)
        assertEquals(
            "Expect a day of partly cloudy with clear spells",
            dailyForecastMainModel1.summary
        )

        val dailyForecastMainModel2 = dailyForecastMainModelList[2]
        assertEquals(3L, dailyForecastMainModel2.id)
        assertEquals("24/11/2025", dailyForecastMainModel2.dt)
        assertEquals(
            "Expect a day of partly cloudy with clear spells",
            dailyForecastMainModel2.summary
        )

        val dailyForecastMainModel3 = dailyForecastMainModelList[3]
        assertEquals(4L, dailyForecastMainModel3.id)
        assertEquals("25/11/2025", dailyForecastMainModel3.dt)
        assertEquals(
            "You can expect partly cloudy in the morning, with rain in the afternoon",
            dailyForecastMainModel3.summary
        )

        val dailyForecastMainModel4 = dailyForecastMainModelList[4]
        assertEquals(5L, dailyForecastMainModel4.id)
        assertEquals("26/11/2025", dailyForecastMainModel4.dt)
        assertEquals(
            "You can expect partly cloudy in the morning, with rain in the afternoon",
            dailyForecastMainModel4.summary
        )

        val dailyForecastMainModel5 = dailyForecastMainModelList[5]
        assertEquals(6L, dailyForecastMainModel5.id)
        assertEquals("27/11/2025", dailyForecastMainModel5.dt)
        assertEquals("Expect a day of partly cloudy with rain", dailyForecastMainModel5.summary)

        val dailyForecastMainModel6 = dailyForecastMainModelList[6]
        assertEquals(7L, dailyForecastMainModel6.id)
        assertEquals("28/11/2025", dailyForecastMainModel6.dt)
        assertEquals(
            "Expect a day of partly cloudy with clear spells",
            dailyForecastMainModel6.summary
        )

        val dailyForecastMainModel7 = dailyForecastMainModelList[7]
        assertEquals(8L, dailyForecastMainModel7.id)
        assertEquals("29/11/2025", dailyForecastMainModel7.dt)
        assertEquals(
            "Expect a day of partly cloudy with clear spells",
            dailyForecastMainModel7.summary
        )

        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            @Suppress("UnusedFlow")
            getDailyWeatherForecastListUseCase.execute()
            stringProvider.getStringForResource(Res.string.data_not_available_text)
        }
    }

    @Test
    fun `Verify that refetching the daily weather forecast list succeeds`() = runTest {
        // GIVEN
        val dailyForecastList = Json.decodeFromString<List<DailyForecast>>(DAILY_FORECAST_LIST)
        val getDailyWeatherForecastListUseCase = mock<GetDailyWeatherForecastListUseCase> {
            everySuspend { execute(any()) } returns flowOf(dailyForecastList)
        }
        val dateFormatter = DateFormatterImpl(TimeZone.UTC)
        val stringProvider = mock<StringProvider> {
            everySuspend { getStringForResource(Res.string.data_not_available_text) } returns "Not available"
        }
        val mainViewModelReducer = MainViewModelReducer()

        // WHEN
        val mainViewModel = MainViewModel(
            getDailyWeatherForecastListUseCase = getDailyWeatherForecastListUseCase,
            dateFormatter = dateFormatter,
            stringProvider = stringProvider,
            mainViewModelReducer = mainViewModelReducer,
            ioDispatcher = testDispatcher
        )
        assertIs<MainUiState.Loading>(mainViewModel.state.value.mainUiState)
        runCurrent()
        mainViewModel.handleIntent(MainViewIntent.FetchDailyWeatherForecast(true))
        val mainUiState = mainViewModel.state.value.mainUiState
        assertIs<MainUiState.Success>(mainUiState)
        assertTrue(mainUiState.isRefreshing)
        runCurrent()
        val dailyForecastMainModelList = mainUiState.dailyForecastMainModelList
        assertEquals(8, mainUiState.dailyForecastMainModelList.size)
        val dailyForecastMainModel0 = dailyForecastMainModelList[0]
        assertEquals(1L, dailyForecastMainModel0.id)
        assertEquals("22/11/2025", dailyForecastMainModel0.dt)
        assertEquals("Expect a day of partly cloudy with rain", dailyForecastMainModel0.summary)

        val dailyForecastMainModel1 = dailyForecastMainModelList[1]
        assertEquals(2L, dailyForecastMainModel1.id)
        assertEquals("23/11/2025", dailyForecastMainModel1.dt)
        assertEquals(
            "Expect a day of partly cloudy with clear spells",
            dailyForecastMainModel1.summary
        )

        val dailyForecastMainModel2 = dailyForecastMainModelList[2]
        assertEquals(3L, dailyForecastMainModel2.id)
        assertEquals("24/11/2025", dailyForecastMainModel2.dt)
        assertEquals(
            "Expect a day of partly cloudy with clear spells",
            dailyForecastMainModel2.summary
        )

        val dailyForecastMainModel3 = dailyForecastMainModelList[3]
        assertEquals(4L, dailyForecastMainModel3.id)
        assertEquals("25/11/2025", dailyForecastMainModel3.dt)
        assertEquals(
            "You can expect partly cloudy in the morning, with rain in the afternoon",
            dailyForecastMainModel3.summary
        )

        val dailyForecastMainModel4 = dailyForecastMainModelList[4]
        assertEquals(5L, dailyForecastMainModel4.id)
        assertEquals("26/11/2025", dailyForecastMainModel4.dt)
        assertEquals(
            "You can expect partly cloudy in the morning, with rain in the afternoon",
            dailyForecastMainModel4.summary
        )

        val dailyForecastMainModel5 = dailyForecastMainModelList[5]
        assertEquals(6L, dailyForecastMainModel5.id)
        assertEquals("27/11/2025", dailyForecastMainModel5.dt)
        assertEquals("Expect a day of partly cloudy with rain", dailyForecastMainModel5.summary)

        val dailyForecastMainModel6 = dailyForecastMainModelList[6]
        assertEquals(7L, dailyForecastMainModel6.id)
        assertEquals("28/11/2025", dailyForecastMainModel6.dt)
        assertEquals(
            "Expect a day of partly cloudy with clear spells",
            dailyForecastMainModel6.summary
        )

        val dailyForecastMainModel7 = dailyForecastMainModelList[7]
        assertEquals(8L, dailyForecastMainModel7.id)
        assertEquals("29/11/2025", dailyForecastMainModel7.dt)
        assertEquals(
            "Expect a day of partly cloudy with clear spells",
            dailyForecastMainModel7.summary
        )
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            @Suppress("UnusedFlow")
            getDailyWeatherForecastListUseCase.execute(false)
            stringProvider.getStringForResource(Res.string.data_not_available_text)
            @Suppress("UnusedFlow")
            getDailyWeatherForecastListUseCase.execute(true)
            stringProvider.getStringForResource(Res.string.data_not_available_text)
        }
    }

    @Test
    fun `Verify that fetching the daily weather forecast list fails `() = runTest {
        // GIVEN
        val getDailyWeatherForecastListUseCase = mock<GetDailyWeatherForecastListUseCase> {
            everySuspend { execute() } throws Exception()
        }
        val dateFormatter = DateFormatterImpl(TimeZone.UTC)
        val stringProvider = mock<StringProvider>()
        val mainViewModelReducer = MainViewModelReducer()

        // WHEN
        val mainViewModel = MainViewModel(
            getDailyWeatherForecastListUseCase = getDailyWeatherForecastListUseCase,
            dateFormatter = dateFormatter,
            stringProvider = stringProvider,
            mainViewModelReducer = mainViewModelReducer,
            ioDispatcher = testDispatcher
        )
        assertIs<MainUiState.Loading>(mainViewModel.state.value.mainUiState)
        runCurrent()

        // THEN
        val mainUiState = mainViewModel.state.value.mainUiState
        assertIs<MainUiState.Error>(mainUiState)
    }
}
