package com.harrisonog.arcraiderscompanion.ui.quest

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.harrisonog.arcraiderscompanion.domain.model.Quest
import com.harrisonog.arcraiderscompanion.domain.model.QuestObjective
import com.harrisonog.arcraiderscompanion.domain.model.QuestStatus
import com.harrisonog.arcraiderscompanion.domain.model.RequiredItem
import com.harrisonog.arcraiderscompanion.domain.model.Reward

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestDetailScreen(
    viewModel: QuestDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToItem: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quest Details") },
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
                uiState.quest != null -> {
                    QuestDetailContent(
                        quest = uiState.quest!!,
                        onToggleObjective = { objectiveId ->
                            viewModel.onEvent(QuestDetailEvent.ToggleObjective(objectiveId))
                        },
                        onUpdateStatus = { status ->
                            viewModel.onEvent(QuestDetailEvent.UpdateStatus(status))
                        },
                        onNavigateToItem = onNavigateToItem
                    )
                }
            }
        }
    }
}

@Composable
fun QuestDetailContent(
    quest: Quest,
    onToggleObjective: (String) -> Unit,
    onUpdateStatus: (QuestStatus) -> Unit,
    onNavigateToItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Quest Name and Status
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = quest.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                QuestStatusBadge(status = quest.status)
            }
        }

        // Description
        if (quest.description != null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = quest.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Quest Status Actions
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (quest.status == QuestStatus.NOT_STARTED) {
                        Button(
                            onClick = { onUpdateStatus(QuestStatus.IN_PROGRESS) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Start Quest")
                        }
                    }
                    if (quest.status == QuestStatus.IN_PROGRESS) {
                        Button(
                            onClick = { onUpdateStatus(QuestStatus.COMPLETED) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Mark Complete")
                        }
                    }
                    if (quest.status == QuestStatus.COMPLETED) {
                        OutlinedButton(
                            onClick = { onUpdateStatus(QuestStatus.NOT_STARTED) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reset Quest")
                        }
                    }
                }
            }
        }

        // Objectives
        if (quest.objectives.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Objectives",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    quest.objectives.sortedBy { it.orderIndex }.forEach { objective ->
                        ObjectiveItem(
                            objective = objective,
                            isCompleted = quest.completedObjectives.contains(objective.id),
                            onToggle = { onToggleObjective(objective.id) }
                        )
                    }
                }
            }
        }

        // Required Items
        if (quest.requiredItems.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Required Items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    quest.requiredItems.forEach { item ->
                        RequiredItemRow(
                            item = item,
                            onClick = { onNavigateToItem(item.itemId) }
                        )
                    }
                }
            }
        }

        // Rewards
        if (quest.rewards.isNotEmpty() || quest.xpReward != null) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Rewards",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (quest.xpReward != null) {
                        Text(
                            text = "XP: ${quest.xpReward}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    quest.rewards.forEach { reward ->
                        RewardRow(reward = reward)
                    }
                }
            }
        }

        // Additional Info
        if (quest.mapLocation != null || quest.questChain != null || quest.prerequisiteQuests.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Additional Info",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (quest.mapLocation != null) {
                        Text(
                            text = "Location: ${quest.mapLocation}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (quest.questChain != null) {
                        Text(
                            text = "Quest Chain: ${quest.questChain}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    if (quest.prerequisiteQuests.isNotEmpty()) {
                        Text(
                            text = "Prerequisites: ${quest.prerequisiteQuests.size} quest(s)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ObjectiveItem(
    objective: QuestObjective,
    isCompleted: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isCompleted,
            onCheckedChange = { onToggle() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = objective.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun RequiredItemRow(
    item: RequiredItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${item.itemName} x${item.quantity}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun RewardRow(
    reward: Reward,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${reward.itemName} x${reward.quantity} (${reward.type})",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}
