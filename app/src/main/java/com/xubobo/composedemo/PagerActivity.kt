package com.xubobo.composedemo

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.palette.graphics.Palette
import com.xubobo.composedemo.pager.PagerWithTransition
import com.xubobo.composedemo.ui.theme.ComposeDemoTheme
import com.xubobo.composedemo.util.HeightSpacer
import com.xubobo.composedemo.util.LoadImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                            .verticalScroll(rememberScrollState())
                    ) {
                        HeightSpacer(value = 12.dp)

                        HorizontalPagerDemo()
                        DribbleInspirationPager()

//                        PagerWithTransition(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(300.dp)
//                        )
                        PagerInCenter()
                    }
                }
            }
        }
    }


}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PagerInCenter() {

    val context = LocalContext.current
    val pageCount = 1000
    val img = remember {
        listOf(
            R.drawable.pexels,
            R.drawable.ava,
            R.drawable.xiaohe
        )
    }

    val bgColorDefault = Color.Transparent

    val imageSize = remember(img) {
        img.size
    }

    val bgColors = remember {
        mutableListOf<Color>().apply {
            repeat(pageCount) {
                add(bgColorDefault)
            }
        }
    }

    val pagerState = rememberPagerState(
        initialPage = pageCount / 2
    ) {
        pageCount
    }
    var bgColor by remember {
        mutableStateOf(bgColorDefault)
    }

    val animatedBg by animateColorAsState(targetValue = bgColor, label = "bgColor")

    LaunchedEffect(key1 = pagerState.currentPage) {
        val currentPage = pagerState.currentPage % imageSize
        launch(Dispatchers.Default) {
            val selectedColor = bgColors[currentPage]
            if (selectedColor == bgColorDefault) {
                Palette.from(BitmapFactory.decodeResource(context.resources, img[currentPage]))
                    .generate { palette ->
                        val domainColor =
                            palette?.lightMutedSwatch?.let { Color(it.rgb) } ?: bgColorDefault
                        bgColors[currentPage] = domainColor
                        bgColor = domainColor
                    }
            } else {
                bgColor = selectedColor
            }
            // 预加载下一页
            val nextPage = (currentPage + 1) % imageSize
            val nextColor = bgColors[nextPage]
            if (nextColor == bgColorDefault) {
                Palette.from(BitmapFactory.decodeResource(context.resources, img[nextPage]))
                    .generate { palette ->
                        val domainColor =
                            palette?.lightMutedSwatch?.let { Color(it.rgb) } ?: bgColorDefault
                        bgColors[nextPage] = domainColor
                    }
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(animatedBg),
        pageSpacing = 10.dp,
        contentPadding = PaddingValues(30.dp)
    ) { page ->
        LoadImage(data = img[page % imageSize], modifier = Modifier.fillMaxSize())
    }
}


/**
 *https://dribbble.com/shots/17117814--Drag-This-audio-player-prototype?source=post_page-----12b3b69af2cc--------------------------------
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DribbleInspirationPager() {
    val img = remember {
        listOf(
            R.drawable.pexels,
            R.drawable.ava,
            R.drawable.xiaohe
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colorScheme.errorContainer)
    ) {
        val pagerState = rememberPagerState {
            img.size
        }

        HorizontalPager(
            state = pagerState,
//            pageSize = PageSize.Fixed(100.dp),
//            pageSpacing = 12.dp
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                SongInformationCard(
                    modifier = Modifier
                        .padding(32.dp)
                        .align(Alignment.Center),
                    pagerState = pagerState,
                    page = page,
                    data = img[page]
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongInformationCard(modifier: Modifier, pagerState: PagerState, page: Int, data: Any) {

    val offsetFraction = pagerState.getOffsetFractionForPage(page).absoluteValue.coerceIn(0f, 1f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(16.dp, ambientColor = Color.LightGray),
        shape = RoundedCornerShape(32.dp),
        colors = elevatedCardColors(containerColor = Color.White)
    ) {
        Column {
            LoadImage(
                data = data,
                modifier = Modifier
                    .padding(32.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
                    .graphicsLayer {
                        val scale = lerp(1f, 1.75f, offsetFraction)
                        // apply the scale equally to both X and Y, to not distort the image
                        scaleX = scale
                        scaleY = scale
                    },
                clip = false
            )
            DragToListen(offsetFraction)
        }

    }

}

@Composable
fun DragToListen(offsetFraction: Float) {
    Box(
        modifier = Modifier
            .height(100.dp * (1 - offsetFraction))
            .fillMaxWidth()
            .graphicsLayer {
                alpha = 1 - offsetFraction
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "")

            Text("DRAG TO LISTEN")
            Spacer(modifier = Modifier.size(4.dp))
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HorizontalPagerDemo() {

    val urls = listOf(
        R.drawable.pexels,
        "https://images.pexels.com/photos/414612/pexels-photo-414612.jpeg?cs=srgb&dl=pexels-james-wheeler-414612.jpg&fm=jpg",
        "https://picsum.photos/900/600",
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

            val pageOffsetFraction = pagerState.getOffsetFractionForPage(it).absoluteValue

            val scale = lerp(
                start = 0.8f,
                stop = 1f,
                fraction = 1f - pageOffsetFraction.coerceIn(0f, 1f)
            )

            // We animate the alpha, between 50% and 100%
            alpha = lerp(
                start = 0.5f,
                stop = 1f,
                fraction = 1f - pageOffsetFraction.coerceIn(0f, 1f)
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