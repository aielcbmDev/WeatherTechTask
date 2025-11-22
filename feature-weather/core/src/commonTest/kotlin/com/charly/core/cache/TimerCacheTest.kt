@file:OptIn(ExperimentalTime::class)

package com.charly.core.cache

import com.charly.datastore.datasource.DatastoreDataSource
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class TimerCacheTest {

    @Test
    fun `Verify cache is expired because there is no time saved`() = runTest {
        // GIVEN
        val clock = mock<Clock>()
        val cacheTimeInMillis = 3600000L // 1 hour
        val datastoreDataSource = mock<DatastoreDataSource> {
            every { getLongValue("CACHE_KEY") } returns flowOf(null)
        }
        val timerCache = TimerCache(clock, cacheTimeInMillis, datastoreDataSource)

        // WHEN
        val result = timerCache.isCacheExpired()

        // THEN
        assertTrue(result)
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            @Suppress("UnusedFlow")
            datastoreDataSource.getLongValue("CACHE_KEY")
        }
    }

    @Test
    fun `Verify cache is expired`() = runTest {
        // GIVEN
        val cacheTimeInMillis = 5L // 5 milliseconds

        val now = Instant.fromEpochSeconds(25L)
        val clock = mock<Clock> {
            every { now() } returns now
        }
        val savedTimeInMillis = 500L
        val datastoreDataSource = mock<DatastoreDataSource> {
            every { getLongValue("CACHE_KEY") } returns flowOf(savedTimeInMillis)
        }
        val timerCache = TimerCache(clock, cacheTimeInMillis, datastoreDataSource)

        // WHEN
        val result = timerCache.isCacheExpired()

        // THEN
        assertTrue(result)
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            @Suppress("UnusedFlow")
            datastoreDataSource.getLongValue("CACHE_KEY")
            clock.now()
        }
    }

    @Test
    fun `Verify cache is NOT expired`() = runTest {
        // GIVEN
        val cacheTimeInMillis = 3600000L // 1 hour

        val now = Instant.fromEpochSeconds(25L)
        val clock = mock<Clock> {
            every { now() } returns now
        }
        val savedTimeInMillis = 500L
        val datastoreDataSource = mock<DatastoreDataSource> {
            every { getLongValue("CACHE_KEY") } returns flowOf(savedTimeInMillis)
        }
        val timerCache = TimerCache(clock, cacheTimeInMillis, datastoreDataSource)

        // WHEN
        val result = timerCache.isCacheExpired()

        // THEN
        assertFalse(result)
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            @Suppress("UnusedFlow")
            datastoreDataSource.getLongValue("CACHE_KEY")
            clock.now()
        }
    }

    @Test
    fun `Verify cache time is saved successfully`() = runTest {
        // GIVEN
        val now = Instant.fromEpochSeconds(25L)
        val clock = mock<Clock> {
            every { now() } returns now
        }
        val clockValue = clock.now().toEpochMilliseconds()
        val cacheTimeInMillis = 3600000L // 1 hour
        val datastoreDataSource = mock<DatastoreDataSource> {
            everySuspend { saveLongValue("CACHE_KEY", clockValue) } returns Unit
        }
        val timerCache = TimerCache(clock, cacheTimeInMillis, datastoreDataSource)

        // WHEN
        timerCache.saveCacheTime()

        // THEN
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            datastoreDataSource.saveLongValue("CACHE_KEY", clockValue)
        }
    }
}
