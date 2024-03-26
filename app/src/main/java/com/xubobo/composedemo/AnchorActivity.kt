package com.xubobo.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.xubobo.composedemo.comp.AnchoredMenuListDemo
import com.xubobo.composedemo.comp.FullHeightBottomSheet
import com.xubobo.composedemo.ui.theme.ComposeDemoTheme

class AnchorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    AnchoredMenuListDemo()
                    FullHeightBottomSheet(header = { Text("Header") }) {
                        Column {
                            repeat(30) {
                                Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
                                    Text(text = "Content")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}