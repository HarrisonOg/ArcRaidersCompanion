package com.harrisonog.arcraiderscompanion.ui.mapevent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harrisonog.arcraiderscompanion.domain.model.EventTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    viewModel: EventDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.eventGroup?.name ?: "Event Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.error ?: "Unknown error",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                uiState.eventGroup != null -> {
                    EventDetailContent(
                        eventGroup = uiState.eventGroup!!
                    )
                }
            }
        }
    }
}

@Composable
fun EventDetailContent(
    eventGroup: EventGroup,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Summary section (if available)
        if (!eventGroup.description.isNullOrBlank()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Summary",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = eventGroup.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Map appearances header
        item {
            Text(
                text = "Map Appearances",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Map occurrences
        items(eventGroup.mapOccurrences) { mapOccurrence ->
            MapOccurrenceCard(
                mapOccurrence = mapOccurrence
            )
        }
    }
}

@Composable
fun MapOccurrenceCard(
    mapOccurrence: MapOccurrence,
    modifier: Modifier = Modifier
) {
    val hasUpcomingEvent = mapOccurrence.times.any { it.isUpcoming() }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = if (hasUpcomingEvent) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mapOccurrence.mapName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (hasUpcomingEvent) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.tertiary
                    ) {
                        Text(
                            text = "Upcoming",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }
            }

            if (mapOccurrence.times.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                val rows = (mapOccurrence.times.size + 1) / 2
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height((rows * 44).dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false
                ) {
                    items(mapOccurrence.times) { eventTime ->
                        EventTimeChip(
                            eventTime = eventTime,
                            isUpcoming = eventTime.isUpcoming()
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "No scheduled times",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun EventTimeChip(
    eventTime: EventTime,
    isUpcoming: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = if (isUpcoming) {
            MaterialTheme.colorScheme.tertiary
        } else {
            MaterialTheme.colorScheme.secondaryContainer
        }
    ) {
        Text(
            text = eventTime.formatForDisplay(),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isUpcoming) {
                MaterialTheme.colorScheme.onTertiary
            } else {
                MaterialTheme.colorScheme.onSecondaryContainer
            }
        )
    }
}
