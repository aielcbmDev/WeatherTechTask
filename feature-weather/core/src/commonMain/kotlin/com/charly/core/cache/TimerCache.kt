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
        val currentMoment: Instant = clock.now()
        val savedTimeInMillis: Long? = datastoreDataSource.getLongValue(CACHE_KEY).first()
        return if (savedTimeInMillis == null) {
            // If no time is saved, treat it as expired to trigger an initial fetch.
            true
        } else {
            val savedInstant = Instant.fromEpochMilliseconds(savedTimeInMillis)
            val elapsedTime = currentMoment - savedInstant
            // Check if more than one hour has passed
            elapsedTime > CACHE_TIME.hours
        }
    }
}
