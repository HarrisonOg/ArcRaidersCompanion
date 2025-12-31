package com.harrisonog.arcraiderscompanion.ui.workshop

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
import com.harrisonog.arcraiderscompanion.domain.model.UpgradeStatus
import com.harrisonog.arcraiderscompanion.ui.quest.RequiredItemRowWithInventory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkshopDetailScreen(
    viewModel: WorkshopDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToItem: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upgrade Details") },
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
                uiState.upgrade != null -> {
                    WorkshopDetailContent(
                        uiState = uiState,
                        onStartUpgrade = {
                            viewModel.onEvent(WorkshopDetailEvent.StartUpgrade)
                        },
                        onCompleteUpgrade = {
                            viewModel.onEvent(WorkshopDetailEvent.CompleteUpgrade)
                        },
                        onUpdateItemQuantity = { itemId, itemName, quantity, imageUrl ->
                            viewModel.onEvent(
                                WorkshopDetailEvent.UpdateItemQuantity(
                                    itemId, itemName, quantity, imageUrl
                                )
                            )
                        },
                        onNavigateToItem = onNavigateToItem
                    )
                }
            }
        }
    }
}

@Composable
fun WorkshopDetailContent(
    uiState: WorkshopDetailUiState,
    onStartUpgrade: () -> Unit,
    onCompleteUpgrade: () -> Unit,
    onUpdateItemQuantity: (String, String, Int, String?) -> Unit,
    onNavigateToItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val upgrade = uiState.upgrade ?: return

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Upgrade Header
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = upgrade.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                UpgradeStatusBadge(status = upgrade.status)

                if (upgrade.description != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = upgrade.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Quick Actions
        Card(modifier = Modifier.fillMaxWidth()) {
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

                when (upgrade.status) {
                    UpgradeStatus.LOCKED -> {
                        Text(
                            text = "Complete previous level to unlock this upgrade",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    UpgradeStatus.NOT_STARTED -> {
                        Button(
                            onClick = onStartUpgrade,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Start Upgrade")
                        }
                    }
                    UpgradeStatus.IN_PROGRESS -> {
                        Button(
                            onClick = onCompleteUpgrade,
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.requiredItemsWithInventory.all { it.isComplete }
                        ) {
                            Text("Complete Upgrade")
                        }
                        if (!uiState.requiredItemsWithInventory.all { it.isComplete }) {
                            Text(
                                text = "Collect all required items to complete",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    UpgradeStatus.COMPLETED -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Upgrade Complete!",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }

        // Required Items
        if (uiState.requiredItemsWithInventory.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
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
                    uiState.requiredItemsWithInventory.forEach { item ->
                        RequiredItemRowWithInventory(
                            item = item,
                            onQuantityChange = { newQuantity ->
                                onUpdateItemQuantity(
                                    item.itemId,
                                    item.itemName,
                                    newQuantity,
                                    item.imageUrl
                                )
                            },
                            onClick = { onNavigateToItem(item.itemId) }
                        )
                    }
                }
            }
        }

        // Rewards
        if (upgrade.rewards.isNotEmpty() || upgrade.unlocks != null) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Rewards & Unlocks",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (upgrade.unlocks != null) {
                        Text(
                            text = upgrade.unlocks,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    upgrade.rewards.forEach { reward ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${reward.itemName} (${reward.type})",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
