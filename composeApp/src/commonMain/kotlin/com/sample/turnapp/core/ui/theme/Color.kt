package com.sample.turnapp.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * COLOR SHADES
 */

// GRAY
private val gray00 = Color(0xFFFFFFFF)
private val gray10 = Color(0xFFF7F7F7)
private val gray20 = Color(0xFFF0F0F0)
private val gray30 = Color(0xFFEAEAEA)
private val gray40 = Color(0xFFD6D6D6)
private val gray50 = Color(0xFFBFBFBF)
private val gray60 = Color(0xFF999999)
private val gray65 = Color(0xFF7C7C7C)
private val gray70 = Color(0xFF6B6B6B)
private val gray75 = Color(0xFF474747)
private val gray80 = Color(0xFF3D3D3D)
private val gray85 = Color(0xFF242424)
private val gray90 = Color(0xFF1F1F1F)
private val gray95 = Color(0xFF121212)
private val gray100 = Color(0xFF000000)

// GREEN
private val green00 = Color(0xFFDFF6EC)
private val green40 = Color(0xFF06B988)
private val green50 = Color(0xFF0FBA90)
private val green70 = Color(0xFF19BCA0)
private val green100 = Color(0xFF102821)

// BLUE
private val blue00 = Color(0xFFECF0FF)
private val blue40 = Color(0xFF6284FD)
private val blue70 = Color(0xFF002ED0)
private val blue100 = Color(0xFF131B35)

// RED
private val red00 = Color(0xFFFFE5E5)
private val red60 = Color(0xFFF14D52)
private val red70 = Color(0xFFD1425E)
private val red100 = Color(0xFF241717)

// YELLOW
private val yellow00 = Color(0xFFFFF9E1)
private val yellow100 = Color(0xFF1D1A0E)

// ORANGE
private val orange40 = Color(0xFFFFB004)
private val orange50 = Color(0xFFFCA034)
private val orange90 = Color(0xFF553700)

/**
 * Semantic Status Colors
 */
private val activeLight = gray95
private val activeDark = gray90

private val deletedLight = red60
private val deletedDark = red60

private val restoreLight = green40
private val restoreDark = green70

private val primaryActionLight = orange40
private val primaryActionDark = orange40

/**
 * App custom colors
 */
@Immutable
data class ClyColors(

    // Backgrounds
    val backgroundPrimary: Color = Color.Transparent,
    val backgroundOnSecondary: Color = Color.Transparent,
    val backgroundSecondary: Color = Color.Transparent,
    val backgroundElevatedItems: Color = Color.Transparent,
    val backgroundOnElevatedItems: Color = Color.Transparent,
    val backgroundButtonTertiary: Color = Color.Transparent,
    val backgroundButtonPrimary: Color = Color.Transparent,
    val backgroundGreenContainer: Color = Color.Transparent,
    val backgroundRedContainer: Color = Color.Transparent,
    val backgroundYellowContainer: Color = Color.Transparent,
    val backgroundBlueContainer: Color = Color.Transparent,
    val backgroundTooltip: Color = Color.Transparent,
    val backgroundGreenButton: Color = Color.Transparent,
    val backgroundRedButton: Color = Color.Transparent,
    val backgroundPopup: Color = Color.Transparent,
    val backgroundTabContainer: Color = Color.Transparent,
    val backgroundTabSelected: Color = Color.Transparent,
    val backgroundNavbar: Color = Color.Transparent,

    // Texts
    val textPrimary: Color = Color.Transparent,
    val textSecondary: Color = Color.Transparent,
    val textTertiary: Color = Color.Transparent,
    val textPlaceholder: Color = Color.Transparent,
    val textGreen: Color = Color.Transparent,
    val textRed: Color = Color.Transparent,
    val textBlue: Color = Color.Transparent,
    val textYellow: Color = Color.Transparent,
    val textColoredButton: Color = Color.Transparent,
    val textButtonPrimary: Color = Color.Transparent,
    val textButtonSecondary: Color = Color.Transparent,
    val textButtonTertiary: Color = Color.Transparent,

    // Brand / Generic
    val brandColor: Color = Color.Transparent,
    val orange: Color = Color.Transparent,
    val white: Color = Color.Transparent,
    val black: Color = Color.Transparent,

    // Status / Actions
    val active: Color = Color.Transparent,
    val deleted: Color = Color.Transparent,
    val restore: Color = Color.Transparent,
    val primaryAction: Color = Color.Transparent,

    // Others
    val separatorPrimary: Color = Color.Transparent,
    val icons: Color = Color.Transparent,
    val transparent: Color = Color.Transparent
)

/**
 * CompositionLocal holder
 */
internal val LocalClyColors = staticCompositionLocalOf { ClyColors() }

/**
 * Light Theme
 */
internal val clyColorsLight
    get() = ClyColors(

        // Backgrounds
        backgroundPrimary = gray00,
        backgroundOnSecondary = gray00,
        backgroundSecondary = gray10,
        backgroundElevatedItems = gray00,
        backgroundOnElevatedItems = gray10,
        backgroundButtonTertiary = gray20,
        backgroundButtonPrimary = green50,
        backgroundGreenContainer = green00,
        backgroundRedContainer = red00,
        backgroundYellowContainer = yellow00,
        backgroundBlueContainer = blue00,
        backgroundTooltip = gray80,
        backgroundGreenButton = green70,
        backgroundRedButton = red70,
        backgroundPopup = gray00,
        backgroundTabContainer = gray10,
        backgroundTabSelected = gray00,
        backgroundNavbar = gray00,

        // Texts
        textPrimary = gray95,
        textSecondary = gray70,
        textTertiary = gray60,
        textPlaceholder = gray50,
        textGreen = green40,
        textRed = red70,
        textBlue = blue70,
        textYellow = orange90,
        textColoredButton = gray00,
        textButtonPrimary = gray00,
        textButtonSecondary = gray95,
        textButtonTertiary = gray65,

        // Generic
        brandColor = green50,
        orange = orange50,
        white = gray00,
        black = gray100,

        // Status / Actions
        active = activeLight,
        deleted = deletedLight,
        restore = restoreLight,
        primaryAction = primaryActionLight,

        // Others
        separatorPrimary = gray20,
        icons = gray40
    )

/**
 * Dark Theme
 */
internal val clyColorsDark
    get() = ClyColors(

        // Backgrounds
        backgroundPrimary = gray95,
        backgroundOnSecondary = gray80,
        backgroundSecondary = gray90,
        backgroundElevatedItems = gray85,
        backgroundOnElevatedItems = gray80,
        backgroundButtonTertiary = gray85,
        backgroundButtonPrimary = green50,
        backgroundGreenContainer = green100,
        backgroundRedContainer = red100,
        backgroundYellowContainer = yellow100,
        backgroundBlueContainer = blue100,
        backgroundTooltip = gray80,
        backgroundGreenButton = green70,
        backgroundRedButton = red70,
        backgroundPopup = gray95,
        backgroundTabContainer = gray90,
        backgroundTabSelected = gray80,
        backgroundNavbar = gray90,

        // Texts
        textPrimary = gray10,
        textSecondary = gray65,
        textTertiary = gray70,
        textPlaceholder = gray80,
        textGreen = green40,
        textRed = red60,
        textBlue = blue40,
        textYellow = orange40,
        textColoredButton = gray00,
        textButtonPrimary = gray00,
        textButtonSecondary = gray10,
        textButtonTertiary = gray50,

        // Generic
        brandColor = green50,
        orange = orange50,
        white = gray00,
        black = gray100,

        // Status / Actions
        active = activeDark,
        deleted = deletedDark,
        restore = restoreDark,
        primaryAction = primaryActionDark,

        // Others
        separatorPrimary = gray85,
        icons = gray75
    )