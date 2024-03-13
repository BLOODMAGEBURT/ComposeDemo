package com.xubobo.composedemo.comp

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.xubobo.composedemo.util.toPxf

@Composable
fun DragSortableLazyColumn() {
    val itemsList =
        remember { mutableStateListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5") }

    val px50 = 50.dp.toPxf()
    var zIndex by remember { mutableFloatStateOf(1f) }

    LazyColumn {
        itemsIndexed(items = itemsList) { index, item ->
            var draggedItemIndex by remember { mutableIntStateOf(-1) }
            val dragOffset = remember { mutableStateOf(Offset.Zero) }

            val modifier = Modifier
                .offset {
                    IntOffset(0, dragOffset.value.y.toInt())
                }
                .fillMaxWidth()
                .height(50.dp)
                .padding(8.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            zIndex = 2f
                            draggedItemIndex = index
                            dragOffset.value = Offset.Zero
                        },
                        onDragCancel = {
                            zIndex = 1f
                        },
                        onDragEnd = {
                            zIndex = 1f
                            if (draggedItemIndex != -1) {
                                val newIndex =
                                    calculateNewIndex(
                                        index,
                                        dragOffset.value.y,
                                        itemsList.size,
                                        px50
                                    )
                                itemsList.add(newIndex, itemsList.removeAt(draggedItemIndex))
                                draggedItemIndex = -1
                                dragOffset.value = Offset.Zero
                            }
                        }) { _, dragAmount ->
                        draggedItemIndex = index
                        dragOffset.value += dragAmount
                    }
                }

            Box(
                modifier = modifier
            ) {
                Text(
                    text = item,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

private fun calculateNewIndex(
    currentIndex: Int,
    offsetY: Float,
    itemCount: Int,
    heightPx: Float
): Int {
    val newIndex = (currentIndex.toFloat() + offsetY / heightPx).toInt()
    return when {
        newIndex < 0 -> 0
        newIndex >= itemCount -> itemCount - 1
        else -> newIndex
    }
}
