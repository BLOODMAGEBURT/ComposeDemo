package com.xubobo.composedemo.comp

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import com.xubobo.composedemo.util.LoadImage
import kotlin.math.PI
import kotlin.math.abs


@Composable
fun ZoomImg(
    data: Any,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {

    var scaleState by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    val scaleAnim by animateFloatAsState(targetValue = scaleState, label = "scale")

    LoadImage(
        data = data,
        modifier = modifier
            .clipToBounds()
            .pointerInput(Unit) {
                detectTransformGestures(
                    canConsumeGesture = { _, zoom -> zoom != 1f },
                    onGestureEnd = {
                        if (scaleState < 1f) {
                            scaleState = 1f
                            offset = Offset.Zero
                        }
                    }) { _, _, zoom, _ ->
                    val scaleTemp = scaleState * zoom
                    scaleState = scaleTemp.coerceIn(0.5f, 3f)
//                    offset = if (scaleTemp == 1f) Offset.Zero else offset + pan
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = {
                    scaleState = when {
                        scaleState < 2f -> 2f
                        scaleState < 4f -> 4f
                        else -> {
                            offset = Offset.Zero
                            1f
                        }
                    }
                }) {
                    onClick()
                }
            }
            .graphicsLayer {
                scaleX = scaleAnim
                scaleY = scaleAnim
                translationX = offset.x
                translationY = offset.y
            },
        clip = false,
        contentScale = ContentScale.Inside
    )

}


suspend fun PointerInputScope.detectTransformGestures(
    panZoomLock: Boolean = false,
    canConsumeGesture: (pan: Offset, zoom: Float) -> Boolean,
    onGestureEnd: () -> Unit = {},
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float) -> Unit,
) {
    awaitEachGesture {
        var rotation = 0f
        var zoom = 1f
        var pan = Offset.Zero
        var pastTouchSlop = false
        val touchSlop = viewConfiguration.touchSlop
        var lockedToPanZoom = false

        awaitFirstDown(requireUnconsumed = false)
        do {
            val event = awaitPointerEvent()
            val canceled = event.changes.fastAny { it.isConsumed }
            if (!canceled) {
                val zoomChange = event.calculateZoom()
                val rotationChange = event.calculateRotation()
                val panChange = event.calculatePan()

                if (!pastTouchSlop) {
                    zoom *= zoomChange
                    rotation += rotationChange
                    pan += panChange

                    val centroidSize = event.calculateCentroidSize(useCurrent = false)
                    val zoomMotion = abs(1 - zoom) * centroidSize
                    val rotationMotion = abs(rotation * PI.toFloat() * centroidSize / 180f)
                    val panMotion = pan.getDistance()

                    if (zoomMotion > touchSlop ||
                        rotationMotion > touchSlop ||
                        panMotion > touchSlop
                    ) {
                        pastTouchSlop = true
                        lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
                    }
                }

                if (pastTouchSlop) {
                    val centroid = event.calculateCentroid(useCurrent = false)
                    val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
                    if (effectiveRotation != 0f ||
                        zoomChange != 1f ||
                        panChange != Offset.Zero
                    ) {
                        if (canConsumeGesture(panChange, zoomChange)) {
                            onGesture(centroid, panChange, zoomChange, effectiveRotation)
                            event.changes.fastForEach {
                                if (it.positionChanged()) {
                                    it.consume()
                                }
                            }
                        }
                    }
                }
            }
        } while (!canceled && event.changes.fastAny { it.pressed })
        onGestureEnd()
    }
}