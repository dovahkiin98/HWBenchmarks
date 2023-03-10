package net.inferno.hwbenchmarks.model

import kotlinx.serialization.Serializable

@Serializable
open class BenchmarkModel(
    val name: String,
    val link: String,
    val benchmark: Int,
    val rank: Int,
) {

    enum class Type {
        CPUS,
        GPUS,
        ;

        override fun toString() = super.toString().lowercase()
    }
}