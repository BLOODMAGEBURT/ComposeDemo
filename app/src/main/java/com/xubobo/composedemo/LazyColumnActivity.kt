package com.xubobo.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBoxDefaults.positionalThreshold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.xubobo.composedemo.ui.theme.ComposeDemoTheme
import com.xubobo.composedemo.util.toDp
import com.xubobo.composedemo.util.toPxf
import kotlinx.coroutines.delay
import timber.log.Timber

class LazyColumnActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            ComposeDemoTheme {

                val headerHeight = 100.dp
                val positionalThreshold = (headerHeight / 2).toPxf()
                val headerHeightPx = headerHeight.toPxf()
                var headerOffsetY by remember {
                    mutableFloatStateOf(0f)
                }

                val animatedHeaderOffsetY by animateFloatAsState(
                    targetValue = headerOffsetY,
                    label = "offsetY"
                )

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val models = remember {
                        (1..100).map {
                            "item $it"
                        }
                    }
//                    val pullToRefreshState = rememberPullToRefreshState()
//
//                    if (pullToRefreshState.isRefreshing) {
//                        LaunchedEffect(key1 = Unit) {
//                            delay(1500)
//                            pullToRefreshState.endRefresh()
//                        }
//                    }


                    val connection = remember {
                        object : NestedScrollConnection {
                            override fun onPreScroll(
                                available: Offset,
                                source: NestedScrollSource
                            ): Offset {
                                val delta = available.y
                                if (headerOffsetY == 0f) {
                                    return Offset.Zero
                                }
                                val newOffset = headerOffsetY + delta
                                val previousOffset = headerOffsetY
                                headerOffsetY = newOffset.coerceIn(0f, headerHeightPx)

                                val consumed = headerOffsetY - previousOffset

                                return Offset(0f, consumed)
                            }

                            override fun onPostScroll(
                                consumed: Offset,
                                available: Offset,
                                source: NestedScrollSource
                            ): Offset {
                                val delta = available.y
                                val newOffset = headerOffsetY + delta
                                val previousOffset = headerOffsetY
                                headerOffsetY = newOffset.coerceIn(0f, headerHeightPx)
                                val consumedPx = headerOffsetY - previousOffset

                                return Offset(0f, consumedPx)
                            }

//                            override suspend fun onPreFling(available: Velocity): Velocity {
//                                Timber.e("onPreFling: ${available.y}")
//                                if (headerOffsetY > positionalThreshold) {
////                                    startRefresh()
//                                    headerOffsetY = headerHeightPx
//                                } else {
////                                    animateTo(0f)
//                                    headerOffsetY = 0f
//                                }
//
//                                val velocity = available.y
//                                val consumed = when {
//                                    // We are flinging without having dragged the pull refresh (for example a fling inside
//                                    // a list) - don't consume
//                                    headerOffsetY == 0f -> 0f
//                                    // If the velocity is negative, the fling is upwards, and we don't want to prevent the
//                                    // the list from scrolling
//                                    velocity < 0f -> 0f
//                                    // We are showing the indicator, and the fling is downwards - consume everything
//                                    else -> velocity
//                                }
//
//                                return Velocity(0f, consumed)
//                            }

                            override suspend fun onPostFling(
                                consumed: Velocity,
                                available: Velocity
                            ): Velocity {
                                Timber.d("onPostFling consumed:$consumed, available:${available.y}")

                                if (available.y == 0f) {
                                    if (headerOffsetY > positionalThreshold) {
//                                        startRefresh()
                                        headerOffsetY = headerHeightPx
                                    } else {
//                                        animateTo(0f)
                                        headerOffsetY = 0f
                                    }
                                }

                                return super.onPostFling(consumed, available)
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(connection)
                    ) {
                        Box(
                            modifier = Modifier
                                .offset {
                                    IntOffset(
                                        0,
                                        -headerHeight.roundToPx() + animatedHeaderOffsetY.toInt()
                                    )
                                }
                                .height(headerHeight)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.errorContainer)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = animatedHeaderOffsetY.toDp())
                        ) {
                            items(models) {
                                Greeting(name = it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
            .height(30.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}
