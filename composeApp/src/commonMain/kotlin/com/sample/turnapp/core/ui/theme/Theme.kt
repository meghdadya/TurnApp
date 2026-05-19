package com.sample.turnapp.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun TurnAppTheme(
    systemDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val providedTypo = LocalClyTypography provides clyTypography
    val providedColors = LocalClyColors provides if (systemDarkTheme) clyColorsDark else clyColorsLight
    val providedDimens = LocalClycDimens provides clycDimens
    val providedBorders = LocalClyBorders provides getClyBorders(
        clyDimens = clycDimens,
        clyColors = if (systemDarkTheme) clyColorsDark else clyColorsLight
    )
    val providedShapes = LocalClyShapes provides getClyShapes(clycDimens)

    CompositionLocalProvider(
        providedTypo,
        providedColors,
        providedDimens,
        providedBorders,
        providedShapes,
        content = content
    )
}

/**
 * Every composable component should use following object in order to
 * access theme.
 *
 * Examples of usage:
 *  - ClycTheme.typography.title
 *  - ClycTheme.dimens.dimen4dp
 *  - ClycTheme.colors.primary
 */
object TurnAppTheme {
    val typography: ClyTypography
        @Composable
        get() = LocalClyTypography.current
    val dimens: ClycDimens
        @Composable
        get() = LocalClycDimens.current
    val colors: ClyColors
        @Composable
        get() = LocalClyColors.current
    val borders: ClyBorders
        @Composable
        get() = LocalClyBorders.current
    val shapes: ClyShapes
        @Composable
        get() = LocalClyShapes.current

    val disabledAlpha: Float
        get() = 0.5f
}