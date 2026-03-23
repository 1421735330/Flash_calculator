package com.example.flashgncalculator.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flashgncalculator.domain.FlashOption
import com.example.flashgncalculator.ui.theme.AppBlack
import com.example.flashgncalculator.ui.theme.AppWhite
import com.example.flashgncalculator.ui.theme.MutedWhite
import com.example.flashgncalculator.ui.theme.PickerBorder
import com.example.flashgncalculator.ui.theme.PickerHighlight
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun WheelPickerColumn(
    modifier: Modifier = Modifier,
    title: String,
    options: List<FlashOption>,
    selectedOption: FlashOption,
    onSelected: (FlashOption) -> Unit
) {
    val itemHeight = 54.dp
    val visibleRowCount = 5
    val selectedIndex = options.indexOfFirst { it.label == selectedOption.label }.coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val centeredIndex by rememberCenteredIndex(listState = listState, selectedIndex = selectedIndex)

    LaunchedEffect(selectedIndex) {
        if (!listState.isScrollInProgress && listState.firstVisibleItemIndex != selectedIndex) {
            listState.scrollToItem(selectedIndex)
        }
    }

    LaunchedEffect(listState, centeredIndex, selectedOption) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { isScrolling -> !isScrolling }
            .collect {
                val newIndex = centeredIndex.coerceIn(0, options.lastIndex)
                if (options[newIndex] != selectedOption) {
                    onSelected(options[newIndex])
                }
                listState.animateScrollToItem(newIndex)
            }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = AppWhite,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight * visibleRowCount)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(AppBlack, Color.Transparent, Color.Transparent, AppBlack),
                            startY = 0f,
                            endY = size.height
                        )
                    )
                }
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth()
                    .height(itemHeight)
                    .clip(RoundedCornerShape(18.dp))
                    .background(PickerHighlight)
                    .border(
                        width = 1.dp,
                        color = PickerBorder,
                        shape = RoundedCornerShape(18.dp)
                    )
            )

            LazyColumn(
                state = listState,
                flingBehavior = flingBehavior,
                contentPadding = PaddingValues(vertical = itemHeight * 2),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(options) { index, option ->
                    val isSelected = index == centeredIndex
                    Text(
                        text = option.label,
                        color = if (isSelected) AppWhite else MutedWhite,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(itemHeight)
                            .alpha(if (isSelected) 1f else 0.62f)
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberCenteredIndex(
    listState: LazyListState,
    selectedIndex: Int
): State<Int> {
    return derivedStateOf {
        val visibleItems = listState.layoutInfo.visibleItemsInfo
        if (visibleItems.isEmpty()) {
            selectedIndex
        } else {
            val viewportCenter = (listState.layoutInfo.viewportStartOffset + listState.layoutInfo.viewportEndOffset) / 2
            visibleItems.minByOrNull { item ->
                kotlin.math.abs((item.offset + item.size / 2) - viewportCenter)
            }?.index ?: selectedIndex
        }
    }
}
