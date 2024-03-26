package com.xubobo.composedemo.comp

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredMenuListDemo() {
    val density = LocalDensity.current

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            anchors = DraggableAnchors {
                DragAnchors.Start at 0f
                DragAnchors.End at 0f
            },
            positionalThreshold = { distance: Float -> distance * 0.3f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
    }

    var menuHeight by remember { mutableIntStateOf(0) }
    var menuHeightDp by remember { mutableStateOf(0.dp) }
    val coroutineScope = rememberCoroutineScope()
    val localDensity = LocalDensity.current
    val listState = rememberLazyListState()

    val connection = remember {
        object : NestedScrollConnection {

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = available.y
                // if remaining velocity on bottom scroll -> forward to anchoredDraggable
                if (delta < 0) {
                    return state.dispatchRawDelta(-delta).toYOffset()
                }
                return super.onPostScroll(consumed, available, source)
            }

            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val end = listState.isScrolledToEnd()

                // prevent showing dummy list item
                // consume remaining scroll if we are past last real item
                if (available.y < 0 && end) {
                    state.dispatchRawDelta(-available.y)
                    return available
                }

                // pass scroll if list is on it's end - or sheet is expanded
                if (end || state.currentValue == DragAnchors.End) {
                    val consumed = state.dispatchRawDelta(-available.y)
                    // return the consumed scroll (need to flip value again as we use reverse in AnchoredDraggable)
                    return consumed.absoluteValue.toYOffset()
                }

                return super.onPreScroll(available, source)
            }

            override suspend fun onPostFling(
                consumed: androidx.compose.ui.unit.Velocity,
                available: androidx.compose.ui.unit.Velocity
            ): androidx.compose.ui.unit.Velocity {
                state.settle(-available.y)
                return super.onPostFling(consumed, available)
            }

            private fun Float.toYOffset() = Offset(0f, this)
        }
    }

    Box(Modifier.fillMaxSize()) {
        Menu({
            (menuHeight - state.requireOffset().roundToInt())
        }, onHeightChange = { height ->
            menuHeight = height
            menuHeightDp = localDensity.run { height.toDp() }

            state.updateAnchors(
                DraggableAnchors {
                    DragAnchors.Start at 0f
                    DragAnchors.End at height.toFloat()
                }
            )
        })

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(connection)
                .anchoredDraggable(state, Orientation.Vertical, reverseDirection = true)
                .offset {
                    IntOffset(
                        y = -state
                            .requireOffset()
                            .roundToInt(),
                        x = 0,
                    )
                }) {
            items(20) {
                Box(
                    contentAlignment = Alignment.Center, modifier =
                    Modifier
                        .height(70.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                ) {
                    Text(it.toString())
                }
            }
            item {
                Box(
                    Modifier
                        .height(10.dp)
                        .background(Color.Red)
                        .fillMaxWidth()
                )
            }
        }

    }
}

@Composable
fun BoxScope.Menu(getYOffset: () -> Int, onHeightChange: (Int) -> Unit) {
    var height by remember { mutableIntStateOf(0) }
    Box(
        Modifier
            .offset {
                IntOffset(
                    y = getYOffset(),
                    x = 0,
                )
            }
            .onSizeChanged {
                height = it.height
                onHeightChange(it.height)
            }
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Menu",
                style = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Center),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. ",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(onClick = {}) {
                Text("Button")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuPreview() {
    Box {
        Menu({ 0 }, {})
    }
}

enum class DragAnchors {
    Start,
    End,
}

fun LazyListState.isScrolledToEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1