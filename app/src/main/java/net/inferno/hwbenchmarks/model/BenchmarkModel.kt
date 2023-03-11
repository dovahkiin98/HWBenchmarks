package net.inferno.hwbenchmarks.model

import kotlinx.serialization.Serializable

@Serializable
open class BenchmarkModel(
    val id: Int,
    val name: String,
    val benchmark: Int,
    val rank: Int,
    val type: Type,
) {

    enum class Type {
        CPUS,
        GPUS,
        ;

        override fun toString() = super.toString().lowercase()
    }
}