package net.inferno.hwbenchmarks.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.inferno.hwbenchmarks.R
import net.inferno.hwbenchmarks.model.BenchmarkModel
import net.inferno.hwbenchmarks.theme.AppTheme
import net.inferno.hwbenchmarks.ui.list.BenchmarksListContainer

@Composable
fun MainActivityUI() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "benchmarks",
    ) {
        composable("benchmarks") {
            BenchmarksListContainer(navController)
        }

        composable("test") {
            TestPage(navController)
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