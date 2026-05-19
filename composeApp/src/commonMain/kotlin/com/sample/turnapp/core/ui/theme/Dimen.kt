package com.sample.turnapp.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val dimen1dp get() = 1.dp
private val dimen2dp get() = 2.dp
private val dimen4dp get() = 4.dp
private val dimen8dp get() = 8.dp
private val dimen12dp get() = 12.dp
private val dimen16dp get() = 16.dp
private val dimen20dp get() = 20.dp
private val dimen24dp get() = 24.dp
private val dimen28dp get() = 28.dp
private val dimen32dp get() = 32.dp
private val dimen40dp get() = 40.dp
private val dimen72dp get() = 72.dp

/**
 * App's custom dimensions used in components.
 * This class's implementation is provided through [clycDimens] and
 * held through [LocalClycDimens].
 *
 * @see ClyTheme For accessing dimensions
 */
@Immutable
data class ClycDimens(
    val padding2XSmall: Dp = 0.dp,
    val paddingXSmall: Dp = 0.dp,
    val paddingSmall: Dp = 0.dp,
    val paddingSemiSmall: Dp = 0.dp,
    val paddingMedium: Dp = 0.dp,
    val paddingSemiLarge: Dp = 0.dp,
    val paddingLarge: Dp = 0.dp,
    val buttonRadius: Dp = 0.dp,
    val buttonRadiusMedium: Dp = 0.dp,
    val buttonRadiusLarge: Dp = 0.dp,
    val iconWidthXSmall: Dp = 0.dp,
    val iconWidthSmall: Dp = 0.dp,
    val iconWidthSemiSmall: Dp = 0.dp,
    val iconWidthMedium: Dp = 0.dp,
    val iconWidthLarge: Dp = 0.dp,
    val iconWidthXLarge: Dp = 0.dp,
    val textFieldToLabel: Dp = 0.dp,
    val titleToStatusBar: Dp = 0.dp,
    val upToCTA: Dp = 0.dp,
    val bottomToCTA: Dp = 0.dp,
    val percentageRadius: Dp = 0.dp,
    val percentageWidth: Dp = 0.dp,
    val percentageHeight: Dp = 0.dp,
    val insideFrameRadius: Dp = 0.dp,
    val border: Dp = 0.dp,
    val border2: Dp = 0.dp,
    val border4: Dp = 0.dp,
    val tabIndicatorRadius: Dp = 0.dp,
    val tabIndicatorHeight: Dp = 0.dp,

    )

/**
 * Holder of [ClycDimens] which is provided using [clycDimens] through
 * app's theme: [ClyTheme]
 */
internal val LocalClycDimens = staticCompositionLocalOf { ClycDimens() }

internal val clycDimens
    get() = ClycDimens(
        padding2XSmall = dimen2dp,
        paddingXSmall = dimen4dp,
        paddingSmall = dimen8dp,
        paddingSemiSmall = dimen12dp,
        paddingMedium = dimen16dp,
        paddingSemiLarge = dimen20dp,
        paddingLarge = dimen24dp,
        buttonRadius = dimen4dp,
        buttonRadiusMedium = dimen8dp,
        buttonRadiusLarge = dimen32dp,
        iconWidthXSmall = dimen12dp,
        iconWidthSmall = dimen16dp,
        iconWidthSemiSmall = dimen20dp,
        iconWidthMedium = dimen24dp,
        iconWidthLarge = dimen32dp,
        iconWidthXLarge = dimen40dp,
        textFieldToLabel = dimen4dp,
        titleToStatusBar = dimen24dp,
        upToCTA = dimen24dp,
        bottomToCTA = dimen32dp,
        percentageRadius = dimen2dp,
        percentageWidth = 72.dp,
        percentageHeight = 18.dp,
        insideFrameRadius = dimen16dp,
        border = dimen1dp,
        border2 = dimen2dp,
        border4 = dimen4dp,
        tabIndicatorRadius = dimen8dp,
        tabIndicatorHeight = dimen4dp
    )

/**
 * Text dimensions in SP
 */
val textSize10 @Composable get() = 10.dpToSp()
val textSize11 @Composable get() = 11.dpToSp()
val textSize12 @Composable get() = 12.dpToSp()
val textSize13 @Composable get() = 13.dpToSp()
val textSize14 @Composable get() = 14.dpToSp()
val textSize15 @Composable get() = 15.dpToSp()
val textSize16 @Composable get() = 16.dpToSp()
val textSize18 @Composable get() = 18.dpToSp()
val textSize20 @Composable get() = 20.dpToSp()
val textSize24 @Composable get() = 24.dpToSp()
val textSize32 @Composable get() = 32.dpToSp()

@Composable
fun Int.dpToSp() = with(LocalDensity.current) { dp.toSp() }
