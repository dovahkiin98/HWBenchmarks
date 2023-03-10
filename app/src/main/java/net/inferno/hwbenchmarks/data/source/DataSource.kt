package net.inferno.hwbenchmarks.data.source

import net.inferno.hwbenchmarks.model.BenchmarkModel

interface DataSource {
    suspend fun getSingleCPUsData(): List<BenchmarkModel>

    suspend fun getGPUsData(): List<BenchmarkModel>

    suspend fun getBenchmarks(type: BenchmarkModel.Type) = when (type) {
        BenchmarkModel.Type.CPUS -> getSingleCPUsData()
        BenchmarkModel.Type.GPUS -> getGPUsData()
    }
}