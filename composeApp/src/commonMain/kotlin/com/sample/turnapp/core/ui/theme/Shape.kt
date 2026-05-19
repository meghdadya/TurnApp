package com.sample.turnapp.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.pow
import kotlin.math.sqrt

@Immutable
data class ClyShapes(
    val roundedXSmall: Shape = RoundedCornerShape(0.dp),
    val roundedSmall: Shape = RoundedCornerShape(0.dp),
    val roundedMedium: Shape = RoundedCornerShape(0.dp),
    val roundedLarge: Shape = RoundedCornerShape(0.dp),
    val roundedInsidePage: Shape = RoundedCornerShape(0.dp),
    val topRoundedTabIndicator: Shape = RoundedCornerShape(0.dp),
    val trapezoidOnLeft: Shape = TrapezoidShape(),
    val trapezoidOnRight: Shape = TrapezoidShape(),
    val bottomRounded: Shape = RoundedCornerShape(0.dp)
)

internal val LocalClyShapes = staticCompositionLocalOf { ClyShapes() }

internal fun getClyShapes(dimens: ClycDimens) =
    ClyShapes(
        roundedXSmall = RoundedCornerShape(dimens.percentageRadius),
        roundedSmall = RoundedCornerShape(dimens.buttonRadius),
        roundedMedium = RoundedCornerShape(dimens.buttonRadiusMedium),
        roundedLarge = RoundedCornerShape(dimens.buttonRadiusLarge),
        roundedInsidePage = RoundedCornerShape(
            topStart = dimens.insideFrameRadius,
            topEnd = dimens.insideFrameRadius
        ),
        topRoundedTabIndicator = RoundedCornerShape(
            topStart = dimens.tabIndicatorRadius,
            topEnd = dimens.tabIndicatorRadius
        ),
        trapezoidOnLeft = TrapezoidShape(
            cornersOffset = TrapezoidShape.CornersOffset(
                topLeftOffset = Offset(x = 24f, y = 0f),
                bottomLeftOffset = Offset(x = -24f, y = 0f)
            ),
            cornerRadius = 4f
        ),
        trapezoidOnRight = TrapezoidShape(
            cornersOffset = TrapezoidShape.CornersOffset(
                topRightOffset = Offset(x = 24f, y = 0f),
                bottomRightOffset = Offset(x = -24f, y = 0f)
            ),
            cornerRadius = 4f
        ),
        bottomRounded = RoundedCornerShape(
            bottomStart = dimens.buttonRadiusMedium,
            bottomEnd = dimens.buttonRadiusMedium
        )
    )

/**
 * Draws a trapezoid shape on the desired compose object.
 * All corners are calculated from the width/height of the shape
 * applied with each corner's offset. Each offset
 * holds [Offset.x] and [Offset.y] for
 * horizontal and vertical offsetting respectively.
 *
 * @param cornersOffset Represents each corners offset that takes into account when measuring
 * each corner's exact position.
 */
class TrapezoidShape(
    private val cornersOffset: CornersOffset = CornersOffset(),
    private val cornerRadius: Float = 0f
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val corners = arrayListOf(
            // top left corner
            Offset(
                x = 0f + cornersOffset.topLeftOffset.x,
                y = 0f + cornersOffset.topLeftOffset.y
            ),
            // top right corner
            Offset(
                x = size.width + cornersOffset.topRightOffset.x,
                y = 0f + cornersOffset.topRightOffset.y
            ),
            // bottom right corner
            Offset(
                x = size.width + cornersOffset.bottomRightOffset.x,
                y = size.height + cornersOffset.bottomRightOffset.y
            ),
            // bottom left corner
            Offset(
                x = 0f + cornersOffset.bottomLeftOffset.x,
                y = size.height + cornersOffset.bottomLeftOffset.y
            )
        )

        return Outline.Generic(
            Path().apply {
                // top left corner
                val p1 = pointOnCircle(cornerRadius, corners[0], corners[3])
                val p2 = pointOnCircle(cornerRadius, corners[0], corners[1])
                moveTo(x = p1.x, y = p1.y)
                quadraticTo(
                    x1 = corners[0].x, y1 = corners[0].y, x2 = p2.x, y2 = p2.y
                )
                // top right corner
                val p3 = pointOnCircle(cornerRadius, corners[1], corners[0])
                val p4 = pointOnCircle(cornerRadius, corners[1], corners[2])
                lineTo(x =p3.x, y = p3.y)
                quadraticTo(
                    x1 = corners[1].x, y1 = corners[1].y, x2 = p4.x, y2 = p4.y
                )
                // bottom right corner
                val p5 = pointOnCircle(cornerRadius, corners[2], corners[1])
                val p6 = pointOnCircle(cornerRadius, corners[2], corners[3])
                lineTo(x =p5.x, y = p5.y)
                quadraticTo(
                    x1 = corners[2].x, y1 = corners[2].y, x2 = p6.x, y2 = p6.y
                )
                // bottom left corner
                val p7 = pointOnCircle(cornerRadius, corners[3], corners[2])
                val p8 = pointOnCircle(cornerRadius, corners[3], corners[0])
                lineTo(x =p7.x, y = p7.y)
                quadraticTo(
                    x1 = corners[3].x, y1 = corners[3].y, x2 = p8.x, y2 = p8.y
                )
                close()
            }
        )
    }

    private fun pointOnCircle(
        radius: Float,
        circleCenter: Offset,
        relativeOffset: Offset
    ): Offset {
        val s = relativeOffset.x - circleCenter.x
        val t = relativeOffset.y - circleCenter.y
        val sqrt = sqrt(s.pow(2) + t.pow(2))
        val x = circleCenter.x + s * radius / (sqrt)
        val y = circleCenter.y + t * radius / (sqrt)
        return Offset(x, y)
    }

    /**
     * Holder of the corners' scale factors. Each corner has
     * its own [ScaleFactor] holding vertical and horizontal scales.
     */
    data class CornersOffset(
        val topLeftOffset: Offset = Offset.Zero,
        val topRightOffset: Offset = Offset.Zero,
        val bottomLeftOffset: Offset = Offset.Zero,
        val bottomRightOffset: Offset = Offset.Zero
    )
}