package net.inferno.hwbenchmarks.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.inferno.hwbenchmarks.BuildConfig
import net.inferno.hwbenchmarks.R

@Composable
fun ErrorView(
    retry: () -> Unit,
    modifier: Modifier = Modifier,
    error: Exception? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_error),
                contentDescription = "Error",
                colorFilter = ColorFilter.tint(LocalContentColor.current),
                modifier = Modifier
                    .size(144.dp)
            )

            Text(
                text = stringResource(id = R.string.request_error),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
            )

            if (BuildConfig.DEBUG && error != null) {
                Text(
                    text = error.toString(),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

            Button(
                onClick = retry,
            ) {
                Text(stringResource(id = R.string.retry))
            }
        }
    }
}

@Composable
fun NetworkErrorView(
    retry: () -> Unit,
    modifier: Modifier = Modifier,
    error: Exception? = null,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_wifi_off),
                contentDescription = "Network Error",
                colorFilter = ColorFilter.tint(LocalContentColor.current),
                modifier = Modifier
                    .size(144.dp)
            )

            Text(
                text = stringResource(id = R.string.request_network_error),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
            )

            if (BuildConfig.DEBUG && error != null) {
                Text(
                    text = error.toString(),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }

            Button(
                onClick = retry,
            ) {
                Text(stringResource(id = R.string.retry))
            }
        }
    }
}

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(32.dp)
            .fillMaxSize(),
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}