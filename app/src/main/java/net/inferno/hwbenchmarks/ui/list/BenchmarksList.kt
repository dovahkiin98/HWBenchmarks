package net.inferno.hwbenchmarks.ui.list

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.inferno.hwbenchmarks.R
import net.inferno.hwbenchmarks.data.UIState
import net.inferno.hwbenchmarks.model.BenchmarkModel
import net.inferno.hwbenchmarks.theme.AppTheme
import net.inferno.hwbenchmarks.view.ErrorView
import net.inferno.hwbenchmarks.view.LoadingView
import net.inferno.hwbenchmarks.view.NetworkErrorView

@Composable
fun BenchmarksListUI(
    navController: NavController,
    benchmarksType: BenchmarkModel.Type,
    modifier: Modifier = Modifier,
    viewModel: BenchmarksViewModel = hiltViewModel(key = benchmarksType.toString()),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.benchmarksType = benchmarksType
    }

    BenchmarksList(
        uiState = uiState,
        benchmarksType = benchmarksType,
        sortValue = viewModel.sortValue,
        searchTextValue = viewModel.searchTextValue,
        onSetSortValue = {
            viewModel.sortValue = it
        },
        modifier = modifier,
        onRetry = {
            viewModel.requestData()
        },
        onCloseSearch = {
            viewModel.applyFilters("")
        },
        onApplySearch = {
            viewModel.applyFilters(it)
        },
        onX = {
            navController.navigate("test")
        }
    )
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun BenchmarksList(
    uiState: UIState<List<BenchmarkModel>>,
    benchmarksType: BenchmarkModel.Type,
    modifier: Modifier = Modifier,
    sortValue: Int = BenchmarksViewModel.SORT_VALUE_NAME,
    searchTextValue: String = "",
    lazyScrollState: LazyListState = rememberLazyListState(),
    onRetry: () -> Unit = {},
    onCloseSearch: () -> Unit = {},
    onSetSortValue: (Int) -> Unit = {},
    onApplySearch: (String) -> Unit = {},
    onX: () -> Unit = {},
) {
    var searchStarted by rememberSaveable { mutableStateOf(false) }
    val searchBoxFocus = remember { FocusRequester() }

    val coroutineScope = rememberCoroutineScope()
    val softwareKeyboardController = LocalSoftwareKeyboardController.current!!

    val closeSearch = {
        onCloseSearch()
        searchStarted = false
        softwareKeyboardController.hide()
    }

    var showSortPopup by remember { mutableStateOf(false) }

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    DisposableEffect(Unit) {
        onDispose {
            closeSearch()
        }
    }

    Scaffold(
        topBar = {
            if (!searchStarted) {
                TopAppBar(
                    title = {
                        Text(
                            text = benchmarksType.getPageTitle(),
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    },
                    actions = {
                        PlainTooltipBox(
                            tooltip = {
                                Text("Sort")
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .tooltipAnchor(),
                            ) {
                                IconButton(
                                    onClick = {
                                        showSortPopup = true
                                    },
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_sort),
                                        "Sort",
                                    )
                                }

                                DropdownMenu(
                                    expanded = showSortPopup,
                                    onDismissRequest = {
                                        showSortPopup = false
                                    },
                                    offset = DpOffset(
                                        x = 0.dp,
                                        y = (-48).dp,
                                    ),
                                    modifier = Modifier
                                        .requiredWidthIn(200.dp)
                                ) {
                                    ListItem(
                                        headlineContent = {
                                            Text("Sort By Name")
                                        },
                                        leadingContent = {
                                            RadioButton(
                                                selected = sortValue == BenchmarksViewModel.SORT_VALUE_NAME,
                                                onClick = null,
                                            )
                                        },
                                        modifier = Modifier
                                            .clickable {
                                                onSetSortValue(BenchmarksViewModel.SORT_VALUE_NAME)

                                                showSortPopup = false
                                            }
                                    )

                                    ListItem(
                                        headlineContent = {
                                            Text("Sort By Score")
                                        },
                                        leadingContent = {
                                            RadioButton(
                                                selected = sortValue == BenchmarksViewModel.SORT_VALUE_SCORE,
                                                onClick = null,
                                            )
                                        },
                                        modifier = Modifier
                                            .clickable {
                                                onSetSortValue(BenchmarksViewModel.SORT_VALUE_SCORE)

                                                showSortPopup = false
                                            }
                                    )
                                }

                            }
                        }

                        PlainTooltipBox(
                            tooltip = {
                                Text("Search")
                            }
                        ) {
                            IconButton(
                                onClick = {
                                    searchStarted = true
                                    coroutineScope.launch {
                                        delay(100)
                                        searchBoxFocus.requestFocus()
                                    }
                                },
                                modifier = Modifier
                                    .tooltipAnchor(),
                            ) {
                                Icon(Icons.Default.Search, "Search")
                            }
                        }
                    },
                    scrollBehavior = topAppBarScrollBehavior,
                    modifier = Modifier
                        .clickable {
                            onX()
                        }
                )
            } else {
                SearchBoxTopAppBar(
                    searchTextValue = searchTextValue,
                    onApplySearch = onApplySearch,
                    focusRequester = searchBoxFocus,
                    onCloseSearch = closeSearch,
                )
            }
        },
        contentWindowInsets = WindowInsets.statusBars,
    ) { paddingValues ->
        Box(
            modifier = modifier
                .padding(paddingValues)
        ) {
            when (uiState) {
                is UIState.Loading -> LoadingView()

                is UIState.Success -> LazyColumn(
                    contentPadding = PaddingValues(4.dp),
                    state = lazyScrollState,
                    modifier = Modifier
                        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                ) {
                    val benchmarks = uiState.data

                    items(benchmarks, key = { it.id }) {
                        BenchmarkItem(
                            it,
                            modifier = Modifier
                                .padding(4.dp)
                                .animateItemPlacement()
                        )
                    }
                }

                is UIState.Error -> {
                    if (uiState.isNetwork) {
                        NetworkErrorView(onRetry, error = uiState.error, modifier = modifier)
                    } else {
                        ErrorView(onRetry, error = uiState.error, modifier = modifier)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBoxTopAppBar(
    searchTextValue: String,
    onApplySearch: (String) -> Unit,
    focusRequester: FocusRequester,
    onCloseSearch: () -> Unit,
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current!!

    TopAppBar(
        title = {
            BasicTextField(
                searchTextValue,
                onValueChange = { text ->
                    onApplySearch(text)
                },
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                textStyle = TextStyle(
                    color = LocalContentColor.current,
                    fontSize = 18.sp,
                ),
                singleLine = true,
                decorationBox = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            if (searchTextValue.isBlank()) {
                                Text(
                                    "Search...",
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = ContentAlpha.medium,
                                    ),
                                    fontSize = 18.sp,
                                )
                            }

                            it()
                        }

                        if (searchTextValue.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    onApplySearch("")
                                },
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    null,
                                )
                            }
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusRequester.freeFocus()
                        softwareKeyboardController.hide()
                    },
                ),
                modifier = Modifier
                    .focusRequester(focusRequester),
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                onCloseSearch()
            }) {
                Icon(Icons.Default.ArrowBack, null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        ),
    )
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Success",
)
@Composable
fun BenchmarksListPreviewSuccess() {
    AppTheme {
        BenchmarksList(
            UIState.Success(
                listOf(
                    BenchmarkModel(
                        id = 2,
                        name = "Intel i5 11600K",
                        rank = 2,
                        benchmark = 2000,
                        type = BenchmarkModel.Type.CPUS,
                    ),
                    BenchmarkModel(
                        id = 1,
                        name = "Intel i7 11700K",
                        rank = 1,
                        benchmark = 3000,
                        type = BenchmarkModel.Type.CPUS,
                    )
                )
            ),
            benchmarksType = BenchmarkModel.Type.CPUS,
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Loading",
)
@Composable
fun BenchmarksListPreviewLoading() {
    AppTheme {
        BenchmarksList(
            UIState.Loading(),
            benchmarksType = BenchmarkModel.Type.CPUS,
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Failure",
)
@Composable
fun BenchmarksListPreviewFailure() {
    AppTheme {
        BenchmarksList(
            UIState.Error(Exception("Test Exception"), isNetwork = false),
            benchmarksType = BenchmarkModel.Type.CPUS,
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "FailureNetwork",
)
@Composable
fun BenchmarksListPreviewFailureNetwork() {
    AppTheme {
        BenchmarksList(
            UIState.Error(Exception("Network Exception"), isNetwork = true),
            benchmarksType = BenchmarkModel.Type.CPUS,
        )
    }
}

@Composable
fun BenchmarkModel.Type.getPageTitle() = when (this) {
    BenchmarkModel.Type.CPUS -> stringResource(id = R.string.nav_single_cpu)
    BenchmarkModel.Type.GPUS -> stringResource(id = R.string.nav_gpu)
}
