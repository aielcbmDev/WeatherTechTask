package com.charly.domain.usecases

import com.charly.domain.model.DailyForecast
import com.charly.domain.repositories.GetDailyWeatherForecastListRepository
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame

class GetDailyWeatherForecastListUseCaseTest {

    @Test
    fun `Verify that fetching a list of daily forecast from the database succeed`() = runTest {
        // GIVEN
        val dailyForecastList = listOf<DailyForecast>()
        val getDailyWeatherForecastListRepository = mock<GetDailyWeatherForecastListRepository> {
            everySuspend { execute() } returns flowOf(dailyForecastList)
        }
        val getDailyWeatherForecastListUseCase =
            GetDailyWeatherForecastListUseCase(getDailyWeatherForecastListRepository)

        // WHEN
        val result = getDailyWeatherForecastListUseCase.execute().first()
        assertSame(dailyForecastList, result)
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            @Suppress("UnusedFlow")
            getDailyWeatherForecastListRepository.execute()
        }

    }

    @Test
    fun `Verify that fetching a list of daily forecast from the database fails`() = runTest {
        // GIVEN
        val getDailyWeatherForecastListRepository = mock<GetDailyWeatherForecastListRepository> {
            everySuspend { execute() } throws Exception()
        }
        val getDailyWeatherForecastListUseCase =
            GetDailyWeatherForecastListUseCase(getDailyWeatherForecastListRepository)

        // WHEN
        val actualException = assertFailsWith<Exception> {
            getDailyWeatherForecastListUseCase.execute().first()
        }

        // THEN
        assertIs<Exception>(actualException)
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            @Suppress("UnusedFlow")
            getDailyWeatherForecastListRepository.execute()
        }
    }
}
