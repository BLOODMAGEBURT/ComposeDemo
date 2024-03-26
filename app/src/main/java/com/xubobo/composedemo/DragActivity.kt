package com.xubobo.composedemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.xubobo.composedemo.comp.DragSortableLazyColumn
import com.xubobo.composedemo.ui.theme.ComposeDemoTheme
import com.xubobo.composedemo.util.HeightSpacer
import com.xubobo.composedemo.util.toPxf
import kotlin.math.roundToInt

const val TAG: String = "here drag"

class DragActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HeightSpacer(value = 12.dp)
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    MaterialTheme.colorScheme.errorContainer,
                                    MaterialTheme.shapes.medium
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            DragOrientationText()
                        }
                        DragPointBox()
                        SoundSwitch()

                        VerticalSwitch()

                        DragSortableLazyColumn()
                    }
                }
            }
        }
    }


}


enum class VerticalSwitchState {
    UP,
    DOWN
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun VerticalSwitch() {
    val density = LocalDensity.current

    val anchoredState = remember {
        AnchoredDraggableState(
            initialValue = VerticalSwitchState.UP,
            anchors = DraggableAnchors {
                VerticalSwitchState.UP at 0f
                VerticalSwitchState.DOWN at 50.dp.toPxf(density)
            },
            positionalThreshold = { distance: Float -> distance * 0.4f },
            velocityThreshold = { 150.dp.toPxf(density) },
            animationSpec = tween()
        )
    }

    Box(
        modifier = Modifier
            .height(80.dp)
            .width(30.dp)
            .border(1.dp, MaterialTheme.colorScheme.errorContainer, CircleShape)
    ) {
        Canvas(
            modifier = Modifier
                .size(30.dp)
                .offset {
                    IntOffset(
                        0,
                        anchoredState
                            .requireOffset()
                            .roundToInt()
                    )
                }
                .anchoredDraggable(anchoredState, orientation = Orientation.Vertical)
        ) {
            drawCircle(Color.Cyan)
        }
    }
}

@Composable
private fun DragOrientationText() {

    var offsetX by remember {
        mutableFloatStateOf(0f)
    }

    val state = rememberDraggableState { delta ->
        Log.e(TAG, "DragOrientationText: $delta")
        offsetX += delta
    }



    Text(
        text = "Drag",
        modifier = Modifier
            .offset { IntOffset(offsetX.toInt(), 0) }
            .draggable(
                state,
                orientation = Orientation.Horizontal
            )

    )
}

@Composable
fun DragPointBox() {

    var offsetX by remember {
        mutableFloatStateOf(0f)
    }
    var offsetY by remember {
        mutableFloatStateOf(0f)
    }

    var showText by remember {
        mutableStateOf(false)
    }

    val animOffset by animateIntOffsetAsState(
        targetValue = if (showText) IntOffset(
            offsetX.toInt(),
            offsetY.toInt()
        ) else IntOffset(0, 0), label = "offset"
    )

    Box(
        modifier = Modifier
            .offset { animOffset }
            .size(100.dp)
            .background(MaterialTheme.colorScheme.errorContainer, MaterialTheme.shapes.medium)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { showText = true },
                    onDragCancel = {
                        showText = false
                        offsetX = 0f
                        offsetY = 0f
                    },
                    onDragEnd = {
                        showText = false
                        offsetX = 0f
                        offsetY = 0f
                    }
                ) { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (showText) {
            Text(text = "拖动我", color = MaterialTheme.colorScheme.onErrorContainer)
        }
    }

}


enum class DragValue { Start, Center, End }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SoundSwitch(modifier: Modifier = Modifier) {

    val density = LocalDensity.current


    val end = 100.dp.toPxf()
    Log.e(TAG, "SoundSwitch: 100.dp = $end")

    val anchors =
        DraggableAnchors {
            DragValue.Start at 0.dp.toPxf(density)
            DragValue.Center at 37.5.dp.toPxf(density)
            DragValue.End at with(density) { 75.dp.toPx() }
        }


    val state = remember {
        AnchoredDraggableState(
            initialValue = DragValue.Start,
            anchors = anchors,
            positionalThreshold = { totalDistance -> totalDistance * 0.5f },
            velocityThreshold = { 200.dp.toPxf(density) },
            animationSpec = spring()
        )
    }


    Box(
        modifier = Modifier
            .size(100.dp, 25.dp)
            .border(
                1.dp, MaterialTheme.colorScheme.errorContainer,
                CircleShape
            )
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        state
                            .requireOffset()
                            .roundToInt(), 0
                    )
                }
                .anchoredDraggable(state, orientation = Orientation.Horizontal)
                .size(25.dp)
                .background(
                    MaterialTheme.colorScheme.onErrorContainer,
                    CircleShape
                )
        )
    }
}


