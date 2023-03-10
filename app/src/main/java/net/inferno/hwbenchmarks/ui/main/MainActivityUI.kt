package net.inferno.hwbenchmarks.ui.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import net.inferno.hwbenchmarks.R
import net.inferno.hwbenchmarks.model.BenchmarkModel
import net.inferno.hwbenchmarks.theme.AppTheme
import net.inferno.hwbenchmarks.ui.list.BenchmarksListUI

@Composable
fun MainActivityUI() {
    var selectedPage by rememberSaveable { mutableStateOf(BenchmarkModel.Type.CPUS) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                pages.map { page ->
                    NavigationBarItem(
                        icon = { Icon(page.getPageIcon(), null) },
                        label = { Text(page.getPageTitle()) },
                        selected = selectedPage == page,
                        onClick = {
                            selectedPage = page
                        },
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.ime,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            Crossfade(
                targetState = selectedPage,
                label = "page",
            ) {
                BenchmarksListUI(
                    benchmarksType = it
                )
            }
        }
    }
}

@Preview
@Composable
fun MainActivityPreview() {
    AppTheme {
        MainActivityUI()
    }
}

val pages = BenchmarkModel.Type.values()


@Composable
fun BenchmarkModel.Type.getPageTitle() = when (this) {
    BenchmarkModel.Type.CPUS -> stringResource(id = R.string.nav_single_cpu)
    BenchmarkModel.Type.GPUS -> stringResource(id = R.string.nav_gpu)
}


@Composable
fun BenchmarkModel.Type.getPageIcon() = when (this) {
    BenchmarkModel.Type.CPUS -> painterResource(id = R.drawable.ic_cpu)
    BenchmarkModel.Type.GPUS -> painterResource(id = R.drawable.ic_gpu)
}