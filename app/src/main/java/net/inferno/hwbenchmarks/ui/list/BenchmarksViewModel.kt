package net.inferno.hwbenchmarks.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.inferno.hwbenchmarks.data.Repository
import net.inferno.hwbenchmarks.data.UIState
import net.inferno.hwbenchmarks.model.BenchmarkModel
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class BenchmarksViewModel @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    lateinit var benchmarksType: BenchmarkModel.Type

    private val _uiState = MutableStateFlow<UIState<List<BenchmarkModel>>>(UIState.Loading())

    val uiState = _uiState.asStateFlow()

    var sortValue = SORT_VALUE_NAME
        set(value) {
            field = value
            applyFilters(searchTextValue)
        }
    var searchTextValue by mutableStateOf("")

    private val _benchmarks = mutableListOf<BenchmarkModel>()

    init {
        requestData()
    }

    fun requestData() {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            delay(200)

            _uiState.emit(UIState.Loading())

            try {
                val benchmarks = repository.getBenchmarks(benchmarksType)

                _benchmarks.clear()
                _benchmarks.addAll(benchmarks)

                _uiState.emit(UIState.Success(_benchmarks))
            } catch (e: Exception) {
                if (e.isCancellationException) return@launch

                if (e.isInternetException) _uiState.emit(UIState.Error(e, true))
                else _uiState.emit(UIState.Error(e))
            }
        }
    }

    fun applyFilters(searchValue: String) {
        searchTextValue = searchValue.trimEnd('\n')

        val filteredBenchmarks = _benchmarks.filter {
            it.name.contains(searchTextValue.trim(), true)
        }.sortedWith { o1, o2 ->
            when (sortValue) {
                SORT_VALUE_NAME -> o1.name.compareTo(o2.name)
                SORT_VALUE_SCORE -> o2.benchmark.compareTo(o1.benchmark)
                else -> o1.rank.compareTo(o2.rank)
            }
        }

        _uiState.value = UIState.Success(filteredBenchmarks)
    }

    companion object {
        const val SORT_VALUE_NAME = 0xA
        const val SORT_VALUE_SCORE = 0xB
    }

    private val Throwable.isInternetException
        get() = this::class in arrayOf(
            UnknownHostException::class,
            SocketTimeoutException::class,
            TimeoutCancellationException::class
        )

    private val Throwable.isCancellationException
        get() = this is CancellationException
                && this !is TimeoutCancellationException
}