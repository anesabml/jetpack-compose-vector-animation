package com.anesabml.vectoranimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anesabml.vectoranimation.ui.theme.VectorAnimationTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VectorAnimationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AnimatedVectorDemo()
                }
            }
        }
    }
}

val checkPathNodes = listOf(
    PathNode.MoveTo(4.8f, 13.4f),
    PathNode.LineTo(9.0f, 17.6f),
    PathNode.MoveTo(10.4f, 16.2f),
    PathNode.LineTo(19.6f, 7.0f),
)

val closePathNodes = listOf(
    PathNode.MoveTo(6.4f, 6.4f),
    PathNode.LineTo(17.6f, 17.6f),
    PathNode.MoveTo(6.4f, 17.6f),
    PathNode.LineTo(17.6f, 6.4f),
)

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun AnimatedVectorDemo() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val image = AnimatedImageVector.animatedVectorResource(R.drawable.avd_check_to_close)
        var atEnd by remember { mutableStateOf(false) }
        Image(
            painter = rememberAnimatedVectorPainter(image, atEnd),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clickable {
                    atEnd = !atEnd
                },
            contentScale = ContentScale.Crop
        )

        var toggle by remember { mutableStateOf(false) }
        Image(
            painter = createCloseCheckVectorPainter(toggle),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clickable {
                    toggle = !toggle
                },
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun createCloseCheckVectorPainter(toggle: Boolean): Painter {
    return rememberVectorPainter(
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    ) { _, _ ->
        val transition = updateTransition(targetState = toggle, label = "CheckClose")
        val duration = 1000

        val rotation by transition.animateFloat(
            label = "Rotation",
            transitionSpec = { tween(durationMillis = duration) }
        ) { state ->
            if (state) 0f else 180f
        }

        val fraction by transition.animateFloat(
            label = "PathNodes",
            transitionSpec = { tween(durationMillis = duration) }
        ) { state ->
            if (state) 0f else 1f
        }

        val pathNodes = lerp(checkPathNodes, closePathNodes, fraction)

        Group(
            name = "GroupCheckClose",
            rotation = rotation,
            translationX = 0.0f,
            translationY = 0.0f,
            pivotX = 12.0f,
            pivotY = 12.0f,
        ) {
            Path(
                pathData = pathNodes,
                fill = null,
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 2.0f,
                strokeLineCap = StrokeCap.Square,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 4.0f,
                pathFillType = PathFillType.NonZero
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VectorAnimationTheme {
        AnimatedVectorDemo()
    }
}

fun lerp(start: List<PathNode>, stop: List<PathNode>, fraction: Float): List<PathNode> {
    return start.zip(stop) { a, b -> lerp(a, b, fraction) }
}

/**
 * Linearly interpolate between [start] and [stop] with [fraction] between them.
 */
private fun lerp(start: PathNode, stop: PathNode, fraction: Float): PathNode {
    return when (start) {
        is PathNode.RelativeMoveTo -> {
            require(stop is PathNode.RelativeMoveTo)
            PathNode.RelativeMoveTo(
                lerp(start.dx, stop.dx, fraction),
                lerp(start.dy, stop.dy, fraction)
            )
        }
        is PathNode.MoveTo -> {
            require(stop is PathNode.MoveTo)
            PathNode.MoveTo(
                lerp(start.x, stop.x, fraction),
                lerp(start.y, stop.y, fraction)
            )
        }
        is PathNode.RelativeLineTo -> {
            require(stop is PathNode.RelativeLineTo)
            PathNode.RelativeLineTo(
                lerp(start.dx, stop.dx, fraction),
                lerp(start.dy, stop.dy, fraction)
            )
        }
        is PathNode.LineTo -> {
            require(stop is PathNode.LineTo)
            PathNode.LineTo(
                lerp(start.x, stop.x, fraction),
                lerp(start.y, stop.y, fraction)
            )
        }
        is PathNode.RelativeHorizontalTo -> {
            require(stop is PathNode.RelativeHorizontalTo)
            PathNode.RelativeHorizontalTo(
                lerp(start.dx, stop.dx, fraction)
            )
        }
        is PathNode.HorizontalTo -> {
            require(stop is PathNode.HorizontalTo)
            PathNode.HorizontalTo(
                lerp(start.x, stop.x, fraction)
            )
        }
        is PathNode.RelativeVerticalTo -> {
            require(stop is PathNode.RelativeVerticalTo)
            PathNode.RelativeVerticalTo(
                lerp(start.dy, stop.dy, fraction)
            )
        }
        is PathNode.VerticalTo -> {
            require(stop is PathNode.VerticalTo)
            PathNode.VerticalTo(
                lerp(start.y, stop.y, fraction)
            )
        }
        is PathNode.RelativeCurveTo -> {
            require(stop is PathNode.RelativeCurveTo)
            PathNode.RelativeCurveTo(
                lerp(start.dx1, stop.dx1, fraction),
                lerp(start.dy1, stop.dy1, fraction),
                lerp(start.dx2, stop.dx2, fraction),
                lerp(start.dy2, stop.dy2, fraction),
                lerp(start.dx3, stop.dx3, fraction),
                lerp(start.dy3, stop.dy3, fraction)
            )
        }
        is PathNode.CurveTo -> {
            require(stop is PathNode.CurveTo)
            PathNode.CurveTo(
                lerp(start.x1, stop.x1, fraction),
                lerp(start.y1, stop.y1, fraction),
                lerp(start.x2, stop.x2, fraction),
                lerp(start.y2, stop.y2, fraction),
                lerp(start.x3, stop.x3, fraction),
                lerp(start.y3, stop.y3, fraction)
            )
        }
        is PathNode.RelativeReflectiveCurveTo -> {
            require(stop is PathNode.RelativeReflectiveCurveTo)
            PathNode.RelativeReflectiveCurveTo(
                lerp(start.dx1, stop.dx1, fraction),
                lerp(start.dy1, stop.dy1, fraction),
                lerp(start.dx2, stop.dx2, fraction),
                lerp(start.dy2, stop.dy2, fraction)
            )
        }
        is PathNode.ReflectiveCurveTo -> {
            require(stop is PathNode.ReflectiveCurveTo)
            PathNode.ReflectiveCurveTo(
                lerp(start.x1, stop.x1, fraction),
                lerp(start.y1, stop.y1, fraction),
                lerp(start.x2, stop.x2, fraction),
                lerp(start.y2, stop.y2, fraction)
            )
        }
        is PathNode.RelativeQuadTo -> {
            require(stop is PathNode.RelativeQuadTo)
            PathNode.RelativeQuadTo(
                lerp(start.dx1, stop.dx1, fraction),
                lerp(start.dy1, stop.dy1, fraction),
                lerp(start.dx2, stop.dx2, fraction),
                lerp(start.dy2, stop.dy2, fraction)
            )
        }
        is PathNode.QuadTo -> {
            require(stop is PathNode.QuadTo)
            PathNode.QuadTo(
                lerp(start.x1, stop.x1, fraction),
                lerp(start.y1, stop.y1, fraction),
                lerp(start.x2, stop.x2, fraction),
                lerp(start.y2, stop.y2, fraction)
            )
        }
        is PathNode.RelativeReflectiveQuadTo -> {
            require(stop is PathNode.RelativeReflectiveQuadTo)
            PathNode.RelativeReflectiveQuadTo(
                lerp(start.dx, stop.dx, fraction),
                lerp(start.dy, stop.dy, fraction)
            )
        }
        is PathNode.ReflectiveQuadTo -> {
            require(stop is PathNode.ReflectiveQuadTo)
            PathNode.ReflectiveQuadTo(
                lerp(start.x, stop.x, fraction),
                lerp(start.y, stop.y, fraction)
            )
        }
        is PathNode.RelativeArcTo -> {
            require(stop is PathNode.RelativeArcTo)
            PathNode.RelativeArcTo(
                lerp(start.horizontalEllipseRadius, stop.horizontalEllipseRadius, fraction),
                lerp(start.verticalEllipseRadius, stop.verticalEllipseRadius, fraction),
                lerp(start.theta, stop.theta, fraction),
                start.isMoreThanHalf,
                start.isPositiveArc,
                lerp(start.arcStartDx, stop.arcStartDx, fraction),
                lerp(start.arcStartDy, stop.arcStartDy, fraction)
            )
        }
        is PathNode.ArcTo -> {
            require(stop is PathNode.ArcTo)
            PathNode.ArcTo(
                lerp(start.horizontalEllipseRadius, stop.horizontalEllipseRadius, fraction),
                lerp(start.verticalEllipseRadius, stop.verticalEllipseRadius, fraction),
                lerp(start.theta, stop.theta, fraction),
                start.isMoreThanHalf,
                start.isPositiveArc,
                lerp(start.arcStartX, stop.arcStartX, fraction),
                lerp(start.arcStartY, stop.arcStartY, fraction)
            )
        }
        PathNode.Close -> PathNode.Close
    }
}

/**
 * Linearly interpolate between [start] and [stop] with [fraction] between them.
 */
private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}