package com.sample.turnapp.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import org.jetbrains.compose.resources.Font
import turnapp.composeapp.generated.resources.HarmonyOS_Sans_Bold
import turnapp.composeapp.generated.resources.HarmonyOS_Sans_Light
import turnapp.composeapp.generated.resources.HarmonyOS_Sans_Medium
import turnapp.composeapp.generated.resources.HarmonyOS_Sans_Regular
import turnapp.composeapp.generated.resources.Res

data class ClyTextStyle(
    private val boldFontFamily: FontFamily,
    private val mediumFontFamily: FontFamily,
    private val regularFontFamily: FontFamily,
    private val lightFontFamily: FontFamily,
    private val textSize: TextUnit
) {
    val bold: TextStyle =
        TextStyle(
//            fontWeight = FontWeight.Bold, // W700
            fontFamily = boldFontFamily,
            fontSize = textSize
        )
    val medium: TextStyle =
        TextStyle(
//            fontWeight = FontWeight.Medium, // W500
            fontFamily = mediumFontFamily,
            fontSize = textSize
        )
    val regular: TextStyle =
        TextStyle(
//            fontWeight = FontWeight.Normal, // W400
            fontFamily = regularFontFamily,
            fontSize = textSize
        )
    val light: TextStyle =
        TextStyle(
//            fontWeight = FontWeight.Light, // W300
            fontFamily = lightFontFamily,
            fontSize = textSize
        )

    companion object {
        @Stable
        val Default = ClyTextStyle(
            FontFamily.Default,
            FontFamily.Default,
            FontFamily.Default,
            FontFamily.Default,
            TextUnit.Unspecified
        )
    }
}

/**
 * App's custom typography used in components.
 * This class's implementation is provided through [clyTypography] and
 * held through [LocalClyTypography].
 *
 * @see TurnAppTheme For accessing typographies
 */
@Immutable
data class ClyTypography(
    val h5: ClyTextStyle = ClyTextStyle.Default,
    val h6: ClyTextStyle = ClyTextStyle.Default,
    val title: ClyTextStyle = ClyTextStyle.Default,
    val subtitle: ClyTextStyle = ClyTextStyle.Default,
    val body1: ClyTextStyle = ClyTextStyle.Default,
    val body2: ClyTextStyle = ClyTextStyle.Default,
    val caption: ClyTextStyle = ClyTextStyle.Default,
    val overLine: ClyTextStyle = ClyTextStyle.Default,
)

/**
 * Default [ClyTextStyle] for all app's custom [ClyTextStyle]s.
 * @see clyTypography To see all the implementations
 */
private val defaultClyTextStyle: ClyTextStyle
    @Composable get() = ClyTextStyle(
        boldFontFamily = FontFamily(Font(Res.font.HarmonyOS_Sans_Bold)),
        mediumFontFamily = FontFamily(Font(Res.font.HarmonyOS_Sans_Medium)),
        regularFontFamily = FontFamily(Font(Res.font.HarmonyOS_Sans_Regular)),
        lightFontFamily = FontFamily(Font(Res.font.HarmonyOS_Sans_Light)),
        textSize = TextUnit.Unspecified
    )

/**
 * Holder of [ClyTypography] which is provided using [clyTypography] through
 * app's theme: [TurnAppTheme]
 */
internal val LocalClyTypography = staticCompositionLocalOf { ClyTypography() }

internal val clyTypography: ClyTypography
    @Composable get() {
        val def = defaultClyTextStyle
        return ClyTypography(
            h5 = def.copy(textSize = textSize32),
            h6 = def.copy(textSize = textSize24),
            title = def.copy(textSize = textSize20),
            subtitle = def.copy(textSize = textSize18),
            body1 = def.copy(textSize = textSize16),
            body2 = def.copy(textSize = textSize14),
            caption = def.copy(textSize = textSize12),
            overLine = def.copy(textSize = textSize10),
        )
    }