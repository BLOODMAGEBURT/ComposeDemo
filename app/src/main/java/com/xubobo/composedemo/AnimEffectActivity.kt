package com.xubobo.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.xubobo.composedemo.ui.theme.ComposeDemoTheme
import com.xubobo.composedemo.util.heartBeatOfContent
import com.xubobo.composedemo.util.pressEffect
import com.xubobo.composedemo.util.shakingEffect

class AnimEffectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {

                    var shake by remember {
                        mutableStateOf(false)
                    }

                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(onClick = { }, modifier = Modifier.pressEffect()) {
                            Text(text = "press me scale")
                        }

                        Button(
                            onClick = { shake = !shake },
                            modifier = Modifier.shakingEffect(animate = shake)
                        ) {
                            Text(text = "press me shake")
                        }

                        Button(onClick = { }, modifier = Modifier.heartBeatOfContent()) {
                            Text(text = "click me")
                        }
                    }
                }
            }
        }
    }
}

