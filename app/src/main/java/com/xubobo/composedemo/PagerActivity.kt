package com.xubobo.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.xubobo.composedemo.ui.theme.ComposeDemoTheme
import com.xubobo.composedemo.util.HeightSpacer
import com.xubobo.composedemo.util.LoadImage
import kotlin.math.absoluteValue

class PagerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        HeightSpacer(value = 12.dp)

                        HorizontalPagerDemo()

                    }
                }
            }
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HorizontalPagerDemo() {

    val urls = listOf(
        R.drawable.pexels,
        "https://images.pexels.com/photos/414612/pexels-photo-414612.jpeg?cs=srgb&dl=pexels-james-wheeler-414612.jpg&fm=jpg",
        R.drawable.pexels,
        R.drawable.pexels,
        R.drawable.pexels,
        R.drawable.pexels,
        R.drawable.pexels,
        R.drawable.pexels,
    )

    val pagerState = rememberPagerState {
        urls.size
    }


    val threePagesPerViewport = object : PageSize {
        override fun Density.calculateMainAxisPageSize(
            availableSpace: Int,
            pageSpacing: Int
        ): Int {
            return ((availableSpace - 2 * pageSpacing) / 1.5).toInt()
        }
    }

    val fling = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(5),
    )

    HorizontalPager(
        state = pagerState,
        pageSize = threePagesPerViewport,
        pageSpacing = 12.dp,
        flingBehavior = fling,
        contentPadding = PaddingValues(horizontal = 12.dp),
        key = { it },
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    ) {
        CardItem(data = urls[it], modifier = Modifier.graphicsLayer {
            val pageOffset = (
                    (pagerState.currentPage - it) + pagerState
                        .currentPageOffsetFraction
                    ).absoluteValue

            val scale = lerp(
                start = 0.8f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )

            // We animate the alpha, between 50% and 100%
            alpha = lerp(
                start = 0.5f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
            scaleX = scale
            scaleY = scale
        })
    }

}


@Composable
fun CardItem(data: Any, modifier: Modifier = Modifier) {

    Box(modifier = modifier) {
        LoadImage(
            data = data, modifier = Modifier
        )
    }
}