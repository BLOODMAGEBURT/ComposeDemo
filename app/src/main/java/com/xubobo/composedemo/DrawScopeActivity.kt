package com.xubobo.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.xubobo.composedemo.ui.theme.ComposeDemoTheme

class DrawScopeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        DrawWithContentDemo(
                            Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                        )
                    }
                }
            }
        }
    }


}


@Composable
fun DrawWithContentDemo(modifier: Modifier) {

    var pointerOffset by remember {
        mutableStateOf(Offset(0f, 0f))
    }

    val animOffset by animateOffsetAsState(
        targetValue = Offset(
            pointerOffset.x,
            pointerOffset.y
        ), label = "offset"
    )

    Surface(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    pointerOffset += dragAmount
                }
            }
            .onSizeChanged {
                pointerOffset = Offset(it.width / 2f, it.height / 2f)
            }
            .drawWithContent {

                drawContent()
                drawLine(
                    Color.Red,
                    center,
                    Offset(center.x + 70.dp.toPx(), center.y)
                )
                drawRect(
                    Brush.radialGradient(
                        listOf(Color.Transparent, Color.Black),
                        center = animOffset,
                        radius = 70.dp.toPx()
                    )
                )

            },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Text(text = "拖动我，找吕布", modifier = Modifier.align(Alignment.Center))
            Text(
                text = "吕布",
                Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp, top = 20.dp)
            )
            Text(
                text = "诸葛四郎",
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp, top = 12.dp)
            )
            Text(
                text = "曹操",
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp, end = 12.dp)
            )
        }
    }
}