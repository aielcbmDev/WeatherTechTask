package com.charly.domain.usecases

import com.charly.domain.model.DailyForecast
import com.charly.domain.repositories.GetDailyWeatherForecastByIdRepository
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame

class GetDailyWeatherForecastByIdUseCaseTest {

    @Test
    fun `Verify that fetching a daily forecast by id from the database succeeds`() = runTest {
        // GIVEN
        val id = 5L
        val dailyForecast = DailyForecast()
        val getDailyWeatherForecastByIdRepository = mock<GetDailyWeatherForecastByIdRepository>() {
            everySuspend {
                execute(id)
            } returns dailyForecast
        }
        val getDailyWeatherForecastByIdUseCase =
            GetDailyWeatherForecastByIdUseCase(getDailyWeatherForecastByIdRepository)

        // WHEN
        val result = getDailyWeatherForecastByIdUseCase.execute(id)

        // THEN
        assertSame(dailyForecast, result)
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            getDailyWeatherForecastByIdRepository.execute(id)
        }
    }

    @Test
    fun `Verify that fetching a daily forecast by id from the database fails`() = runTest {
        // GIVEN
        val id = 5L
        val getDailyWeatherForecastByIdRepository = mock<GetDailyWeatherForecastByIdRepository>() {
            everySuspend {
                execute(id)
            } throws Exception()
        }
        val getDailyWeatherForecastByIdUseCase =
            GetDailyWeatherForecastByIdUseCase(getDailyWeatherForecastByIdRepository)

        // WHEN
        val actualException = assertFailsWith<Exception> {
            getDailyWeatherForecastByIdUseCase.execute(id)
        }

        // THEN
        assertIs<Exception>(actualException)
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            getDailyWeatherForecastByIdRepository.execute(id)
        }
    }
}
