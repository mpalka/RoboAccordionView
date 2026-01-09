package pl.outofmemory.roboaccordionview.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.outofmemory.roboaccordion.AccordionView

class MainActivity : ComponentActivity() {

    private val capitals = listOf(
        "Athens", "Berlin", "London",
        "Helsinki", "Copenhagen", "Warsaw",
        "Stockholm", "Oslo", "Prague",
        "Budapest", "Paris", "Moscow",
        "Kiev", "Bratislava", "Rome"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DemoScreen(
                        modifier = Modifier.padding(innerPadding),
                        capitals = capitals
                    )
                }
            }
        }
    }
}

data class DemoSection(val id: Int, val title: String, val color: Color)

@Composable
fun DemoScreen(modifier: Modifier = Modifier, capitals: List<String>) {
    // Replicating the 3 sections from the legacy demo
    val sections = listOf(
        DemoSection(0, "Header 0", Color(0xFF8B0000)), // Dark Red
        DemoSection(1, "Header 1", Color(0xFF006400)), // Dark Green
        DemoSection(2, "Header 2", Color(0xFF00008B))  // Dark Blue
    )

    var expandedIndex by remember { mutableStateOf<Int?>(0) }

    // Custom Toggle Policy Logic (Replicated from legacy)
    // Legacy policy: 0 -> 1 -> 2 -> 1 -> -1 (or similar cycle)
    // Here we implement a simple expand logic, but we could enforce the cycle if strictly needed.
    // For a modern UX, standard accordion behavior is usually preferred, but we'll stick to simple single-choice.

    AccordionView(
        modifier = modifier,
        items = sections,
        expandedIndex = expandedIndex,
        onExpandedIndexChange = { newIndex ->
            // Simulate the "CustomAccordionTogglePolicy" if we wanted strictly that flow,
            // but standard behavior is usually what users want in a modernization.
            // We'll stick to standard expand/collapse for better UX.
            expandedIndex = newIndex
        },
        headerContent = { item, isExpanded ->
            // Legacy had specific background colors for headers
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(item.color)
                    .padding(16.dp)
            ) {
                Text(
                    text = item.title,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        bodyContent = { item ->
            when (item.id) {
                0 -> {
                    Text(
                        text = "Content 0",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                1 -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF90EE90)) // Light Green
                            .padding(16.dp)
                    ) {
                        Text("Content 1")
                    }
                }
                2 -> {
                    // List of capitals
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Fixed height for scrolling content inside accordion
                            .background(Color(0xFFADD8E6)) // Light Blue
                    ) {
                        LazyColumn {
                            items(capitals) { capital ->
                                Text(
                                    text = capital,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .clickable { /* Handle click */ }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
