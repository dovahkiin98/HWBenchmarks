package net.inferno.hwbenchmarks.ui.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import net.inferno.hwbenchmarks.model.BenchmarkModel
import net.inferno.hwbenchmarks.ui.main.getPageIcon
import net.inferno.hwbenchmarks.ui.main.getPageTitle
import net.inferno.hwbenchmarks.ui.main.pages

@Composable
fun BenchmarksListContainer(
    navController: NavController,
) {
    var selectedPage by rememberSaveable { mutableStateOf(BenchmarkModel.Type.CPUS) }

    BoxWithConstraints {
        val isLandscape = this.maxWidth > 400.dp

        Scaffold(
            bottomBar = {
                if (!isLandscape) {
                    NavigationBar(
//                        windowInsets = WindowInsets.navigationBars,
                    ) {
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
                }
            },
            contentWindowInsets = WindowInsets.navigationBars,
        ) { paddingValues ->
            Row {
                if (isLandscape) {
                    NavigationRail(
//                        windowInsets = WindowInsets.systemBars,
                        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                    ) {
                        pages.map { page ->
                            NavigationRailItem(
                                icon = { Icon(page.getPageIcon(), null) },
                                label = { Text(page.getPageTitle()) },
                                selected = selectedPage == page,
                                onClick = {
                                    selectedPage = page
                                },
                            )
                        }
                    }
                }

                Crossfade(
                    targetState = selectedPage,
                    label = "page",
                    modifier = Modifier
                        .padding(paddingValues)
                        .weight(1f)
                ) {
                    BenchmarksListUI(
                        navController,
                        benchmarksType = it,
                    )
                }
            }
        }
    }
}