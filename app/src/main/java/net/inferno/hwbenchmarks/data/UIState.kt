package net.inferno.hwbenchmarks.data

sealed class UIState<T> {
    class Loading<T> : UIState<T>()

    class Error<T>(
        val error: Exception,
        val isNetwork: Boolean = false,
    ) : UIState<T>()

    class Success<T>(val data: T) : UIState<T>()
}