package com.xubobo.composedemo.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.imageLoader
import com.xubobo.composedemo.R

internal inline fun <reified T : Activity> Context.startActivityX(block: Intent.() -> Unit = {}) {

    val intent = Intent(this, T::class.java)
    if (this !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.block()
    startActivity(intent)

}


@Composable
fun ColumnScope.HeightSpacer(value: Dp) = Spacer(modifier = Modifier.height(value))


@Stable
fun Float.toDp(density: Density): Dp = with(density) { this@toDp.toDp() }

@Stable
fun Dp.toPxf(density: Density): Float = with(density) { this@toPxf.toPx() }

@Stable
@Composable
fun Dp.toPxf(): Float = toPxf(LocalDensity.current)

@Stable
@Composable
fun Float.toDp() = this.toDp(LocalDensity.current)


@Composable
fun LoadImage(
    data: Any,
    modifier: Modifier,
    clip: Boolean = true,
    contentScale: ContentScale = ContentScale.Crop,
    imageLoader: ImageLoader = LocalContext.current.imageLoader
) {
    AsyncImage(
        model = data,
//        placeholder = painterResource(id = R.mipmap.ic_launcher),
//        error = painterResource(id = R.mipmap.ic_launcher),
        contentScale = contentScale,
        modifier = if (clip) modifier.clip(RoundedCornerShape(3.dp)) else modifier,
        contentDescription = null,
        imageLoader = imageLoader,
    )
}


/**
 * 使控件带有按压效果
 * @param minScale:最小的缩放值(0f-1f)
 */
fun Modifier.pressEffect(minScale: Float = 0.7f): Modifier = composed {
    var scale by remember {
        mutableFloatStateOf(1.0f)
    }
    val scaleState by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(), label = "scale"
    )
    this.then(
        Modifier
            .pointerInput(Unit) {
                awaitEachGesture {
                    //手指离开控件，恢复初始状态
                    scale = 1.0f
                    val event = awaitPointerEvent(PointerEventPass.Main)
                    if (event.type == PointerEventType.Press) {//检测到按压操作，执行缩放
                        scale = minScale
                    }
                }

            }
            .scale(scaleState))
}


/**
 * 定义动画的方向
 */
enum class ShakeDirection {
    HORIZONTAL,
    VERTICAL
}

/**
 * 控件抖动效果，多用于错误提示
 * @param animate:动画触发开关，此值改变后触发一次动画
 * @param maxDp:抖动的最大距离
 * @param duration:动画持续时间
 * @param direction:动画方向
 * @see ShakeDirection
 */
fun Modifier.shakingEffect(
    animate: Boolean = false,
    maxDp: Dp = 10.dp,
    duration: Int = 300,
    direction: ShakeDirection = ShakeDirection.HORIZONTAL
): Modifier = composed {
    var shake by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = animate, block = {
        shake = animate
    })
    val transition = updateTransition(targetState = shake, label = "shake")
    val shakeOffset by transition.animateDp(
        transitionSpec = {
            keyframes {
                durationMillis = duration
                0.dp atFraction 0f
                (-maxDp) atFraction 0.083f
                0.dp atFraction 0.167f
                maxDp atFraction 0.25f
                0.dp atFraction 0.334f
                (-maxDp.value * 0.8f).dp atFraction 0.417f
                0.dp atFraction 0.5f
                (maxDp.value * 0.8f).dp atFraction 0.583f
                0.dp atFraction 0.667f
                (-maxDp.value * 0.5f).dp atFraction 0.75f
                0.dp atFraction 0.833f
                (maxDp.value * 0.5f).dp atFraction 0.917f
                0.dp atFraction 1f
            }
        }, label = "shakeOffset"
    ) {
        if (it)
            0.dp
        else
            0.dp
    }

    this.then(
        Modifier.offset(
            x = if (direction == ShakeDirection.HORIZONTAL) shakeOffset else 0.dp,
            y = if (direction == ShakeDirection.VERTICAL) shakeOffset else 0.dp
        )
    )
}


/**
 * 整体内容仿心跳效果，请在设置背景之前调用
 * @param scaleFactor:缩放因数，介于0-1之间
 * @param duration:单次跳动持续时间
 * @param delayMillis:两次跳动之间的时间间隔
 */
fun Modifier.heartBeatOfContent(
    scaleFactor: Float = 0.8f,
    duration: Int = 800,
    delayMillis: Int = 300
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "infinite")
    val scale by transition.animateFloat(
        initialValue = scaleFactor,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = duration,
                delayMillis = delayMillis,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )
    this.then(Modifier.scale(scale))
}


/**
 * 发光边框效果
 * @param shape:边框形状
 * @param borderColor:边框颜色
 * @param borderWidth:边框宽度
 * @param radius:blur半径
 */
fun Modifier.luminousBorder(
    shape: Shape,
    borderColor: Color = Color.Red,
    borderWidth: Dp = 4.dp,
    radius: Float = 10f,
): Modifier {
    return then(
        Modifier
            .drawWithCache {
                val strokeWidthPx = borderWidth.toPx()
                val outline = shape.createOutline(size, layoutDirection, this)
                onDrawWithContent {
                    drawIntoCanvas {
                        drawContent()
                        val paint = Paint().apply {
                            this.color = borderColor
                            strokeWidth = strokeWidthPx
                            isAntiAlias = true
                            style = PaintingStyle.Stroke
                            blendMode = BlendMode.Src
                            this.asFrameworkPaint().maskFilter =
                                BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
                        }
                        it.drawOutline(outline, paint)
                    }
                }
            })
}