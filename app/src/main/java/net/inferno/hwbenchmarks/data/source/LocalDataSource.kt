package net.inferno.hwbenchmarks.data.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.inferno.hwbenchmarks.model.BenchmarkModel

class LocalDataSource(
    context: Context,
) : DataSource {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "offline_data")

    private val dataStore = context.dataStore

    override suspend fun getSingleCPUsData() = getFromDataStore(BenchmarkModel.Type.CPUS)

    override suspend fun getGPUsData() = getFromDataStore(BenchmarkModel.Type.GPUS)

    private suspend fun getFromDataStore(
        type: BenchmarkModel.Type,
    ): List<BenchmarkModel> =
        withContext(Dispatchers.IO) {
            val preferenceKey = stringPreferencesKey(type.name)
            val json = dataStore.data.first()[preferenceKey] ?: "[]"

            Json.decodeFromString(json)
        }

    suspend fun saveData(
        type: BenchmarkModel.Type,
        data: List<BenchmarkModel>,
    ) = withContext(Dispatchers.IO) {
        val json = Json.encodeToString(data)
        val preferenceKey = stringPreferencesKey(type.name)

        dataStore.edit {
            it[preferenceKey] = json
        }
    }
}