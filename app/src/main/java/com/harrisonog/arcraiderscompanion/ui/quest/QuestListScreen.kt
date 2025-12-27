package com.harrisonog.arcraiderscompanion.ui.quest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harrisonog.arcraiderscompanion.domain.model.Quest
import com.harrisonog.arcraiderscompanion.domain.model.QuestStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestListScreen(
    viewModel: QuestListViewModel = hiltViewModel(),
    onNavigateToQuestDetail: (String) -> Unit,
    onNavigateToItemList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilterMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quests") },
                actions = {
                    IconButton(onClick = onNavigateToItemList) {
                        Icon(
                            imageVector = Icons.Default.Inventory,
                            contentDescription = "Items"
                        )
                    }
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Quests") },
                            onClick = {
                                viewModel.onEvent(QuestListEvent.FilterByStatus(null))
                                showFilterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Not Started") },
                            onClick = {
                                viewModel.onEvent(QuestListEvent.FilterByStatus(QuestStatus.NOT_STARTED))
                                showFilterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("In Progress") },
                            onClick = {
                                viewModel.onEvent(QuestListEvent.FilterByStatus(QuestStatus.IN_PROGRESS))
                                showFilterMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Completed") },
                            onClick = {
                                viewModel.onEvent(QuestListEvent.FilterByStatus(QuestStatus.COMPLETED))
                                showFilterMenu = false
                            }
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
                uiState.isLoading && uiState.quests.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null && uiState.quests.isEmpty() -> {
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
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.onEvent(QuestListEvent.RefreshQuests) }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.quests.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No quests found",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.onEvent(QuestListEvent.RefreshQuests) }) {
                            Text("Refresh")
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.quests,
                            key = { it.id }
                        ) { quest ->
                            QuestItem(
                                quest = quest,
                                onClick = { onNavigateToQuestDetail(quest.id) }
                            )
                        }
                    }
                }
            }

            if (uiState.isRefreshing) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
fun QuestItem(
    quest: Quest,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    text = quest.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                QuestStatusBadge(status = quest.status)
            }

            if (quest.description != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = quest.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (quest.objectives.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                val completedCount = quest.completedObjectives.size
                val totalCount = quest.objectives.size
                LinearProgressIndicator(
                    progress = { if (totalCount > 0) completedCount.toFloat() / totalCount else 0f },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Objectives: $completedCount / $totalCount",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (quest.xpReward != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "XP: ${quest.xpReward}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun QuestStatusBadge(
    status: QuestStatus,
    modifier: Modifier = Modifier
) {
    val (text, color) = when (status) {
        QuestStatus.NOT_STARTED -> "Not Started" to MaterialTheme.colorScheme.outline
        QuestStatus.IN_PROGRESS -> "In Progress" to MaterialTheme.colorScheme.primary
        QuestStatus.COMPLETED -> "Completed" to MaterialTheme.colorScheme.tertiary
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}
