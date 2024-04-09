package com.xubobo.composedemo.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.xubobo.composedemo.util.LoadImage
import com.xubobo.composedemo.util.rememberRandomImageUrl

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerWithTransition(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        beyondBoundsPageCount = 2
    ) { page ->
        Box(
            Modifier
                .pagerCubeInDepthTransition(page, pagerState)
                .fillMaxSize()
        ) {
            LoadImage(
                data = rememberRandomImageUrl(),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                clip = false
            )
        }
    }


}


