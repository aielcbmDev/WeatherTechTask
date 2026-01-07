package com.charly.datastore.datasource

import kotlinx.coroutines.flow.Flow

interface DatastoreDataSource {

    suspend fun saveLongValue(key: String, value: Long)

    fun getLongValue(key: String): Flow<Long?>
}
