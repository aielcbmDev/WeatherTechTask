@file:OptIn(ExperimentalTime::class)

package com.charly.core.cache

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CacheTimer(
    private val clock: Clock
)
