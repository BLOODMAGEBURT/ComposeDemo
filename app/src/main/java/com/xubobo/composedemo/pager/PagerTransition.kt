package com.xubobo.composedemo.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerCubeInDepthTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    cameraDistance = 32f
    // Calculate the absolute offset for the current page from the
    // scroll position.
    val pageOffset = pagerState.getOffsetFractionForPage(page)

    if (pageOffset < -1f) {
        // page is far off screen
        alpha = 0f
    } else if (pageOffset <= 0) {
        // page is to the right of the selected page or the selected page
        alpha = 1f
        transformOrigin = TransformOrigin(0f, 0.5f)
        rotationY = -90f * pageOffset.absoluteValue

    } else if (pageOffset <= 1) {
        // page is to the left of the selected page
        alpha = 1f
        transformOrigin = TransformOrigin(1f, 0.5f)
        rotationY = 90f * pageOffset.absoluteValue
    } else {
        alpha = 0f
    }

    if (pageOffset.absoluteValue <= 0.5) {
        scaleY = 0.4f.coerceAtLeast(1 - pageOffset.absoluteValue)
    } else if (pageOffset.absoluteValue <= 1) {
        scaleY = 0.4f.coerceAtLeast(1 - pageOffset.absoluteValue)
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.pagerHingeTransition(page: Int, pagerState: PagerState) = graphicsLayer {

    val offsetFraction = pagerState.getOffsetFractionForPage(page)
    translationX = offsetFraction * size.width

    transformOrigin = TransformOrigin(0f, 0.5f)

    if (offsetFraction < -1f) {
        // page is far off screen
        alpha = 0f
    } else if (offsetFraction <= 0) {
        // page is to the right of the selected page or the selected page
        alpha = 1f - offsetFraction.absoluteValue
        rotationZ = 0f

    } else if (offsetFraction <= 1) {
        // page is to the left of the selected page
        alpha = 1f - offsetFraction.absoluteValue
        rotationZ = 90f * offsetFraction.absoluteValue
    } else {
        alpha = 0f
    }


}

