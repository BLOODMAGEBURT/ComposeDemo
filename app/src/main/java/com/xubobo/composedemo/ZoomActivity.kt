package com.xubobo.composedemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.xubobo.composedemo.comp.ZoomImg
import com.xubobo.composedemo.ui.theme.ComposeDemoTheme
import kotlin.math.absoluteValue

class ZoomActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val img = remember {
                        listOf(
                            R.drawable.pexels,
                            R.drawable.ava,
                            R.drawable.xiaohe
                        )
                    }
                    Box(modifier = Modifier.fillMaxSize()) {

                        val pagerState = rememberPagerState { 3 }

                        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
                            ZoomImg(
                                data = img[it],
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        val pageOffset = pagerState.getOffsetFractionForPage(it)
                                        Log.e(TAG, "page:$it ------pagerOffset: $pageOffset ")
                                        translationX = pageOffset * size.width
                                        // apply an alpha to fade the current page in and the old page out
                                        alpha = (1 - pageOffset.absoluteValue).coerceIn(0f, 1f)
                                    },
                                onClick = {
                                    Toast.makeText(
                                        this@ZoomActivity,
                                        "click",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                })
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 30.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val normalColor = MaterialTheme.colorScheme.errorContainer
                            val selectedColor = MaterialTheme.colorScheme.primary

                            repeat(3) {
                                val selected = pagerState.currentPage == it

                                val pageOffsetFraction =
                                    pagerState.getOffsetFractionForPage(it).absoluteValue

//                                val fraction = (1 - pageOffsetFraction).coerceIn(0f, 1f)
                                val fraction = pageOffsetFraction.coerceIn(0f, 1f)

                                Canvas(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .scale(lerp(1.5f, 1f, fraction))
                                ) {
                                    drawCircle(
                                        color = if (selected) {
                                            selectedColor
                                        } else {
                                            normalColor
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
