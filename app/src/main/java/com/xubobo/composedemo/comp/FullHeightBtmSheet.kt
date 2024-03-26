package com.xubobo.composedemo.comp

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class States {
    EXPANDED,
    COLLAPSED
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FullHeightBottomSheet(
    header: @Composable () -> Unit,
    body: @Composable () -> Unit
) {
    val density = LocalDensity.current


    val scrollState = rememberScrollState()

    BoxWithConstraints {
        val constraintsScope = this
        val maxHeight = with(LocalDensity.current) {
            constraintsScope.maxHeight.toPx()
        }

        val state = remember {
            AnchoredDraggableState(
                initialValue = States.EXPANDED,
                anchors = DraggableAnchors {
                    States.EXPANDED at 0f
                    States.COLLAPSED at maxHeight
                },
                positionalThreshold = { distance: Float -> distance * 0.4f },
                velocityThreshold = { with(density) { 200.dp.toPx() } },
                animationSpec = tween(),
            )
        }


        val connection = remember {
            object : NestedScrollConnection {

                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0) {
                        state.dispatchRawDelta(delta).toOffset()
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return state.dispatchRawDelta(delta).toOffset()
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    val TAG = "here"
                    Log.e(TAG, "onPreFling: velocity: ${available.y}  scrollValue: ${scrollState.value}", )
                    return if (available.y < 0 && scrollState.value == 0) {
                        state.settle(available.y)
                        available
                    } else {
                        Velocity.Zero
                    }
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    state.settle(available.y)
                    return super.onPostFling(consumed, available)
                }

                private fun Float.toOffset() = Offset(0f, this)
            }
        }

        Box(
            Modifier
                .offset {
                    IntOffset(
                        0,
                        state
                            .requireOffset()
                            .roundToInt()
                    )
                }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Vertical,
                )
                .nestedScroll(connection)

        ) {
            Column(
                Modifier
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                header()
                Box(
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    body()
                }
            }
        }
    }
}