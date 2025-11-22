package com.charly.datastore.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.charly.datastore.createDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DatastoreDataSourceTest {

    @Test
    fun verify_datastore_data_is_saved_and_retrieved_successfully() = runTest {
        // GIVEN
        val testDataStore: DataStore<Preferences> = createDataStore()
        val expectedResult = 5L
        val key = "AnyKey"
        val datastoreDataSource = DatastoreDataSource(testDataStore)
        datastoreDataSource.saveLongValue(key, expectedResult)

        // WHEN
        val result = datastoreDataSource.getLongValue(key).first()

        // THEN
        assertNotNull(result)
        assertEquals(expectedResult, result)
    }
}
