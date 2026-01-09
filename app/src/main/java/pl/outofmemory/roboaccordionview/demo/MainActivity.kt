package pl.outofmemory.roboaccordionview.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
                MainScreen(capitals = capitals)
            }
        }
    }
}

@Composable
fun MainScreen(capitals: List<String>) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Standard", "Fixed Height", "Full Height")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> StandardDemo(capitals)
                1 -> FixedHeightDemo(capitals)
                2 -> FullHeightDemo(capitals)
            }
        }
    }
}

@Composable
fun StandardDemo(capitals: List<String>) {
    val sections = getDemoSections()
    var expandedIndex by remember { mutableStateOf<Int?>(0) }
    
    // Wrap in a verticalScroll so the whole accordion can scroll if it gets long
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        AccordionView(
            items = sections,
            expandedIndex = expandedIndex,
            onExpandedIndexChange = { expandedIndex = it },
            headerContent = { item, isExpanded -> HeaderItem(item) },
            bodyContent = { item -> BodyItem(item, capitals) }
        )
    }
}

@Composable
fun FixedHeightDemo(capitals: List<String>) {
    val sections = getDemoSections()
    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("This container has a fixed height of 400dp.", modifier = Modifier.padding(bottom = 8.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary)
                .verticalScroll(rememberScrollState()) // Allow scrolling inside the fixed box
                .padding(4.dp)
        ) {
            AccordionView(
                items = sections,
                expandedIndex = expandedIndex,
                onExpandedIndexChange = { expandedIndex = it },
                headerContent = { item, isExpanded -> HeaderItem(item) },
                bodyContent = { item -> BodyItem(item, capitals) }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Text("Content below logic...")
    }
}

@Composable
fun FullHeightDemo(capitals: List<String>) {
    val sections = getDemoSections()
    var expandedIndex by remember { mutableStateOf<Int?>(null) }
    // Match the default container color of AccordionView (Surface)
    val containerColor = MaterialTheme.colorScheme.surface

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("This container fills the entire remaining height.", modifier = Modifier.padding(bottom = 8.dp))

        // Wrapper Box acts as the "Full Height" background for the accordion
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Fill remaining space
                .border(2.dp, MaterialTheme.colorScheme.secondary)
                .background(containerColor) // Visually extend accordion background
                .verticalScroll(rememberScrollState())
        ) {
            AccordionView(
                modifier = Modifier.fillMaxWidth(), // Fill width
                items = sections,
                expandedIndex = expandedIndex,
                onExpandedIndexChange = { expandedIndex = it },
                headerContent = { item, isExpanded -> HeaderItem(item) },
                bodyContent = { item -> BodyItem(item, capitals) },
                containerColor = Color.Transparent // Let wrapper background show through
            )
        }
    }
}

// Reuse logic
data class DemoSection(val id: Int, val title: String, val color: Color)

fun getDemoSections() = listOf(
    DemoSection(0, "Header 0", Color(0xFF8B0000)),
    DemoSection(1, "Header 1", Color(0xFF006400)),
    DemoSection(2, "Header 2", Color(0xFF00008B))
)

@Composable
fun HeaderItem(item: DemoSection) {
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
}

@Composable
fun BodyItem(item: DemoSection, capitals: List<String>) {
    when (item.id) {
        0 -> Text("Content 0", modifier = Modifier.padding(16.dp))
        1 -> Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF90EE90))
                .padding(16.dp)
        ) { Text("Content 1") }
        2 -> Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFFADD8E6))
        ) {
            LazyColumn {
                items(capitals) { capital ->
                    Text(
                        text = capital,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable { }
                    )
                }
            }
        }
    }
}
