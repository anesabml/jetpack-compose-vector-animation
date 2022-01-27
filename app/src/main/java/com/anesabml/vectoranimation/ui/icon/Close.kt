package com.anesabml.vectoranimation.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Square
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Close: ImageVector
    get() {
        if (_close != null) {
            return _close!!
        }
        _close = Builder(
            name = "Close",
            defaultWidth = 120.0.dp,
            defaultHeight = 120.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f,
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color(0xFF999999)),
                strokeLineWidth = 2.0f,
                strokeLineCap = Square,
                strokeLineJoin = Miter,
                strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(6.4f, 6.4f)
                lineTo(17.6f, 17.6f)
                moveTo(6.4f, 17.6f)
                lineTo(17.6f, 6.4f)
            }
        }
            .build()
        return _close!!
    }

private var _close: ImageVector? = null
