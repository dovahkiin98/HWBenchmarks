package net.inferno.hwbenchmarks.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withTimeout
import net.inferno.hwbenchmarks.data.source.LocalDataSource
import net.inferno.hwbenchmarks.data.source.RemoteDataSource
import net.inferno.hwbenchmarks.model.BenchmarkModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val remoteSource = RemoteDataSource()
    private val localSource = LocalDataSource(context)

    suspend fun getBenchmarks(type: BenchmarkModel.Type) = localSource.getBenchmarks(type).ifEmpty {
        withTimeout(8_000) {
            remoteSource.getBenchmarks(type).also {
                localSource.saveData(type, it)
            }
        }
    }
}