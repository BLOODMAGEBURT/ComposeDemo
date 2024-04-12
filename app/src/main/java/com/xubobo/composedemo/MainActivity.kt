package com.xubobo.composedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xubobo.composedemo.ui.theme.ComposeDemoTheme
import com.xubobo.composedemo.util.startActivityX

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme


                val actions = remember {
                    listOf(
                        "Item Drag",
                        "Item CustomText",
                        "Item Pager",
                        "Item Anim",
                        "Item Draw",
                        "Item Anchor",
                        "Item Zoom",
                        "Item LazyColumn",
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        items(actions, key = { it }) {
                            ItemMain(text = it) { text ->
                                onItemClick(text)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onItemClick(text: String) {

        when (text) {
            "Item Drag" -> startActivityX<DragActivity>()
            "Item CustomText" -> startActivityX<CustomTextActivity>()
            "Item Pager" -> startActivityX<PagerActivity>()
            "Item Anim" -> startActivityX<AnimEffectActivity>()
            "Item Draw" -> startActivityX<DrawScopeActivity>()
            "Item Anchor" -> startActivityX<AnchorActivity>()
            "Item Zoom" -> startActivityX<ZoomActivity>()
            "Item LazyColumn" -> startActivityX<LazyColumnActivity>()
            else -> {}
        }

    }
}


@Composable
fun ItemMain(text: String, onClick: (String) -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(lineHeight = 36.sp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(text) })
    )
}