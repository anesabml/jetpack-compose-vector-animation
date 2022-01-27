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

val Check: ImageVector
    get() {
        if (_check != null) {
            return _check!!
        }
        _check = Builder(
            name = "Check",
            defaultWidth = 120.0.dp,
            defaultHeight = 120.0.dp,
            viewportWidth = 24.0f,
            viewportHeight = 24.0f
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
                moveTo(4.8f, 13.4f)
                lineTo(9.0f, 17.6f)
                moveTo(10.4f, 16.2f)
                lineTo(19.6f, 7.0f)
            }
        }
            .build()
        return _check!!
    }

private var _check: ImageVector? = null
