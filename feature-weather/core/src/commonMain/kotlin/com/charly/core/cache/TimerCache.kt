@file:OptIn(ExperimentalTime::class)

package com.charly.core.cache

import com.charly.datastore.datasource.DatastoreDataSource
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private const val CACHE_KEY = "CACHE_KEY"
private const val CACHE_TIME = 1

class TimerCache(
    private val clock: Clock,
    private val datastoreDataSource: DatastoreDataSource,
) {
    suspend fun isCacheExpired(): Boolean {
        // If no time is saved, treat it as expired to trigger an initial fetch.
        val savedTimeInMillis = datastoreDataSource.getLongValue(CACHE_KEY).first() ?: return true
        val currentInstant = clock.now()
        val savedInstant = Instant.fromEpochMilliseconds(savedTimeInMillis)
        val elapsedTime = currentInstant - savedInstant
        // Check if more than one hour has passed
        return elapsedTime > CACHE_TIME.hours
    }

    suspend fun saveCacheTime() {
        datastoreDataSource.saveLongValue(CACHE_KEY, clock.now().toEpochMilliseconds())
    }
}
