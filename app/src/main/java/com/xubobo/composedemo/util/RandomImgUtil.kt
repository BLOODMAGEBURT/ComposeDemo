package com.xubobo.composedemo.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

private val rangeForRandom = (0..100000)

private fun randomImageUrl(
    seed: Int = rangeForRandom.random(),
    width: Int = 600,
    height: Int = width,
): String {
    return "https://picsum.photos/seed/$seed/$width/$height"
}

/**
 * Remember a URL generate by [randomImageUrl].
 */
@Composable
fun rememberRandomImageUrl(
    seed: Int = rangeForRandom.random(),
    width: Int = 600,
    height: Int = width,
): String = remember { randomImageUrl(seed, width, height) }