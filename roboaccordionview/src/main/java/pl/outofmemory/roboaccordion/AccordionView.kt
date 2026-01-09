package pl.outofmemory.roboaccordion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * A modern, Expressive Material 3 Accordion component.
 *
 * @param items List of data items to display.
 * @param headerContent Composable lambda for the header.
 * @param bodyContent Composable lambda for the expanded body.
 * @param expandedIndex The index of the currently expanded item, or null if none.
 * @param onExpandedIndexChange Callback when the expanded index changes.
 */
@Composable
fun <T> AccordionView(
    items: List<T>,
    headerContent: @Composable (item: T, isExpanded: Boolean) -> Unit,
    bodyContent: @Composable (item: T) -> Unit,
    modifier: Modifier = Modifier,
    expandedIndex: Int? = null,
    onExpandedIndexChange: (Int?) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Column(
        modifier = modifier
            .background(containerColor)
            .padding(16.dp)
            .animateContentSize()
    ) {
        items.forEachIndexed { index, item ->
            val isExpanded = index == expandedIndex
            
            AccordionItem(
                item = item,
                isExpanded = isExpanded,
                onHeaderClick = {
                    if (isExpanded) {
                        onExpandedIndexChange(null) // Collapse if already expanded
                    } else {
                        onExpandedIndexChange(index) // Expand this one
                    }
                },
                headerContent = headerContent,
                bodyContent = bodyContent,
                contentColor = contentColor
            )
            
            if (index < items.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun <T> AccordionItem(
    item: T,
    isExpanded: Boolean,
    onHeaderClick: () -> Unit,
    headerContent: @Composable (item: T, isExpanded: Boolean) -> Unit,
    bodyContent: @Composable (item: T) -> Unit,
    contentColor: Color
) {
    // Material 3 Expressive shapes: distinct rounded corners
    val shape = if (isExpanded) RoundedCornerShape(20.dp) else RoundedCornerShape(12.dp)
    val containerColor = if (isExpanded) 
        MaterialTheme.colorScheme.surfaceContainerHigh 
    else 
        MaterialTheme.colorScheme.surfaceContainer

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .animateContentSize(), // Smooth shape/size change
        color = containerColor,
        contentColor = contentColor,
        shape = shape,
        tonalElevation = if (isExpanded) 6.dp else 1.dp
    ) {
        Column {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onHeaderClick)
                    .padding(16.dp)
            ) {
                headerContent(item, isExpanded)
            }

            // Body
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .padding(bottom = 8.dp)
                ) {
                    bodyContent(item)
                }
            }
        }
    }
}
