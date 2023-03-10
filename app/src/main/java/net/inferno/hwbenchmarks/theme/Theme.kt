package net.inferno.hwbenchmarks.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import net.inferno.hwbenchmarks.R

@Composable
fun AppTheme(
    isAndroid12: Boolean = Build.VERSION.SDK_INT >= 31,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colors(isAndroid12),
    ) {
        content()
    }
}

@SuppressLint("ConflictingOnColor")
@Composable
fun colors(
    isAndroid12: Boolean = Build.VERSION.SDK_INT >= 31,
): ColorScheme {
    val context = LocalContext.current

    return if (isAndroid12) {
        dynamicDarkColorScheme(context)
    } else {
        darkColorScheme(
            primary = colorResource(R.color.colorPrimary),
            tertiary = colorResource(R.color.colorPrimaryDark),
            secondary = colorResource(R.color.colorPrimary),
            onPrimary = Color.White,
        )
    }
}