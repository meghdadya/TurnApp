package com.sample.turnapp.core.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
data class ClyBorders(
    val brand: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    val orange: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    val red: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    val small: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    val medium: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    val large: BorderStroke = BorderStroke(0.dp, Color.Transparent)
)

internal val LocalClyBorders = staticCompositionLocalOf { ClyBorders() }

internal fun getClyBorders(clyDimens: ClycDimens, clyColors: ClyColors) =
    ClyBorders(
        brand = BorderStroke(clyDimens.border, clyColors.brandColor),
        orange = BorderStroke(clyDimens.border, clyColors.orange),
        red = BorderStroke(clyDimens.border, clyColors.textRed),
        small = BorderStroke(clyDimens.border, clyColors.separatorPrimary),
        medium = BorderStroke(clyDimens.border2, clyColors.separatorPrimary),
        large = BorderStroke(clyDimens.border4, clyColors.separatorPrimary)
    )