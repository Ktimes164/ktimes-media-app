package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.DefaultData
import com.example.data.MediaItem
import com.example.data.models.AdOrder
import com.example.ui.MediaViewModel
import com.example.ui.theme.AppBackground
import com.example.ui.theme.CardBackgroundLight
import com.example.ui.theme.CardBorderLight
import com.example.ui.theme.CharcoalBlack
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.StudioWhite
import com.example.ui.theme.SurfaceVariantLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VibrantOrange
import com.example.utils.IntentUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminGarageScreen(
    viewModel: MediaViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val coroutineScope = rememberCoroutineScope()

    val items by viewModel.filteredItems.collectAsStateWithLifecycle()
    val orders by viewModel.ordersList.collectAsStateWithLifecycle()
    val adminMessage by viewModel.adminMessage.collectAsStateWithLifecycle()

    var activeAdminTab by remember { mutableStateOf("PORTFOLIO") } // "PORTFOLIO" or "CRM"

    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<MediaItem?>(null) }
    var itemToDelete by remember { mutableStateOf<MediaItem?>(null) }

    var showJsonDialog by remember { mutableStateOf(false) }
    var jsonText by remember { mutableStateOf("") }
    var isImportMode by remember { mutableStateOf(false) }

    LaunchedEffect(adminMessage) {
        adminMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearAdminMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "KTIMES ADMIN CONSOLE",
                            color = StudioWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "🔥 Firebase Auth & Firestore Connected",
                            color = ElectricYellow,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = StudioWhite
                        )
                    }
                },
                actions = {
                    if (activeAdminTab == "PORTFOLIO") {
                        // Export JSON Action
                        IconButton(onClick = {
                            coroutineScope.launch {
                                jsonText = viewModel.exportJson(items)
                                isImportMode = false
                                showJsonDialog = true
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = "Export JSON",
                                tint = ElectricYellow
                            )
                        }

                        // Import JSON Action
                        IconButton(onClick = {
                            jsonText = ""
                            isImportMode = true
                            showJsonDialog = true
                        }) {
                            Icon(
                                imageVector = Icons.Default.FileUpload,
                                contentDescription = "Import JSON",
                                tint = ElectricYellow
                            )
                        }

                        // Reset Portfolio Action
                        IconButton(onClick = { viewModel.resetToDefaults() }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Defaults",
                                tint = ElectricYellow
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryPurple)
            )
        },
        floatingActionButton = {
            if (activeAdminTab == "PORTFOLIO") {
                FloatingActionButton(
                    onClick = {
                        itemToEdit = null
                        showAddDialog = true
                    },
                    containerColor = VibrantOrange,
                    contentColor = StudioWhite
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add New Portfolio Sample"
                    )
                }
            }
        },
        containerColor = AppBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Admin Top Segments
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceVariantLight)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (activeAdminTab == "PORTFOLIO") PrimaryPurple else Color.Transparent)
                        .clickable { activeAdminTab = "PORTFOLIO" }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Portfolio Manager (${items.size})",
                        color = if (activeAdminTab == "PORTFOLIO") StudioWhite else CharcoalBlack,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (activeAdminTab == "CRM") PrimaryPurple else Color.Transparent)
                        .clickable { activeAdminTab = "CRM" }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CRM & Leads (${orders.size})",
                        color = if (activeAdminTab == "CRM") StudioWhite else CharcoalBlack,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (activeAdminTab == "PORTFOLIO") {
                // Quick Status Header
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, CardBorderLight, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = CardBackgroundLight)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Active Portfolio: ${items.size} Samples",
                                color = CharcoalBlack,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Live synced to Room Database & Firestore",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = {
                                itemToEdit = null
                                showAddDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("+ Add Sample", color = StudioWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Portfolio Items Admin List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items, key = { it.id }) { item ->
                        AdminItemCard(
                            item = item,
                            onEdit = {
                                itemToEdit = item
                                showAddDialog = true
                            },
                            onDelete = {
                                itemToDelete = item
                            }
                        )
                    }
                }
            } else {
                // CRM & Leads Pipeline Screen (Firestore Live)
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = CardBackgroundLight),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Client Leads & Orders Pipeline",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = CharcoalBlack
                                    )
                                    Text(
                                        text = "Real-time sync via Firebase Firestore",
                                        fontSize = 11.sp,
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CrimsonRed)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${orders.size} Leads",
                                        color = StudioWhite,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    if (orders.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("अद्याप कोणतीही नवीन लीड आलेली नाही.", color = TextMuted)
                            }
                        }
                    } else {
                        items(orders, key = { it.id }) { order ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(1.dp, CardBorderLight, RoundedCornerShape(14.dp)),
                                colors = CardDefaults.cardColors(containerColor = CardBackgroundLight)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = order.clientName,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 15.sp,
                                                color = CharcoalBlack
                                            )
                                            Text(
                                                text = "📱 ${order.clientPhone} • ${order.serviceType}",
                                                fontSize = 12.sp,
                                                color = TextMuted
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(
                                                    when (order.status) {
                                                        "Delivered" -> Color(0xFF2E7D32)
                                                        "Approval" -> VibrantOrange
                                                        "Production" -> PrimaryPurple
                                                        else -> ElectricYellow
                                                    }
                                                )
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = order.status,
                                                color = if (order.status == "New Lead" || order.status == "Quotation Sent") PrimaryPurple else StudioWhite,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }

                                    if (order.details.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "टीप: ${order.details}",
                                            fontSize = 12.sp,
                                            color = CharcoalBlack
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Progress Bar
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Pipeline Progress: ${order.progress}%", fontSize = 11.sp, color = TextMuted)
                                        Text("Budget: ${order.budget}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CrimsonRed)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { order.progress / 100f },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(6.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = PrimaryPurple,
                                        trackColor = SurfaceVariantLight
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        // Advance Stage Button
                                        Button(
                                            onClick = { viewModel.advanceOrderStage(order) },
                                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1.2f).height(38.dp)
                                        ) {
                                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Advance Stage", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        // Call Button
                                        Button(
                                            onClick = { IntentUtils.makePhoneCall(context, order.clientPhone) },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(0.8f).height(38.dp)
                                        ) {
                                            Icon(Icons.Default.PhoneInTalk, contentDescription = "Call", modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Call", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }

                                        // WhatsApp Button
                                        Button(
                                            onClick = {
                                                val msg = "नमस्कार ${order.clientName}, मी Ktimes Media कडून तुमच्या '${order.serviceType}' च्या ऑर्डरबाबत बोलत आहे."
                                                IntentUtils.openWhatsAppDirectMessage(context, msg, order.clientPhone)
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(0.9f).height(38.dp)
                                        ) {
                                            Text("WhatsApp", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Dialog Form
    if (showAddDialog) {
        AddEditItemDialog(
            existingItem = itemToEdit,
            onDismiss = { showAddDialog = false },
            onSave = { savedItem ->
                viewModel.saveItem(savedItem)
                showAddDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            title = { Text("Delete Portfolio Sample?", color = PrimaryPurple, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to remove '${item.title}' (${item.sampleId})?", color = CharcoalBlack) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteItem(item)
                    itemToDelete = null
                }) {
                    Text("Delete", color = CrimsonRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = CardBackgroundLight
        )
    }

    // JSON Export / Import Dialog
    if (showJsonDialog) {
        AlertDialog(
            onDismissRequest = { showJsonDialog = false },
            title = {
                Text(
                    text = if (isImportMode) "Import Portfolio JSON" else "Export Portfolio JSON",
                    color = PrimaryPurple,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = if (isImportMode)
                            "Paste valid JSON list of media items below to load into app:"
                        else
                            "Copy this JSON representation of your portfolio:",
                        color = TextMuted,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = jsonText,
                        onValueChange = { jsonText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        readOnly = !isImportMode,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceVariantLight,
                            unfocusedContainerColor = SurfaceVariantLight,
                            focusedTextColor = CharcoalBlack,
                            unfocusedTextColor = CharcoalBlack
                        )
                    )
                }
            },
            confirmButton = {
                if (isImportMode) {
                    Button(
                        onClick = {
                            if (jsonText.isNotBlank()) {
                                viewModel.importJson(jsonText) { success ->
                                    if (success) showJsonDialog = false
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Text("Import & Replace", color = StudioWhite, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(jsonText))
                            Toast.makeText(context, "Copied JSON to clipboard!", Toast.LENGTH_SHORT).show()
                            showJsonDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy JSON", color = StudioWhite, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showJsonDialog = false }) {
                    Text("Close", color = TextMuted)
                }
            },
            containerColor = CardBackgroundLight
        )
    }
}

@Composable
fun AdminItemCard(
    item: MediaItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, CardBorderLight, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(CrimsonRed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.sampleId,
                            color = StudioWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.category,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${item.mediaType}",
                        color = PrimaryPurple,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.title,
                    color = CharcoalBlack,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = item.priceOrEstimate,
                    color = PrimaryPurple,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = PrimaryPurple)
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = CrimsonRed)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemDialog(
    existingItem: MediaItem?,
    onDismiss: () -> Unit,
    onSave: (MediaItem) -> Unit
) {
    var sampleId by remember { mutableStateOf(existingItem?.sampleId ?: "KTM-SAMPLE-${(100..999).random()}") }
    var title by remember { mutableStateOf(existingItem?.title ?: "") }
    var category by remember { mutableStateOf(existingItem?.category ?: DefaultData.categories[1]) }
    var mediaType by remember { mutableStateOf(existingItem?.mediaType ?: "AUDIO") }
    var mediaUrl by remember { mutableStateOf(existingItem?.mediaUrl ?: "") }
    var thumbnailUrl by remember { mutableStateOf(existingItem?.thumbnailUrl ?: "") }
    var description by remember { mutableStateOf(existingItem?.description ?: "") }
    var priceOrEstimate by remember { mutableStateOf(existingItem?.priceOrEstimate ?: "Starting ₹1,999") }
    var tags by remember { mutableStateOf(existingItem?.tags ?: "") }
    var whatsappMsg by remember { mutableStateOf(existingItem?.whatsappMsg ?: "") }
    var isFeatured by remember { mutableStateOf(existingItem?.isFeatured ?: false) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var mediaTypeExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existingItem == null) "Add New Media Sample" else "Edit Portfolio Sample",
                color = PrimaryPurple,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = sampleId,
                    onValueChange = { sampleId = it },
                    label = { Text("Sample ID (e.g. KTM-AUD-105)", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category", color = TextMuted) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceVariantLight,
                            unfocusedContainerColor = SurfaceVariantLight,
                            focusedTextColor = CharcoalBlack,
                            unfocusedTextColor = CharcoalBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        DefaultData.categories.filter { it != "All" }.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat, color = CharcoalBlack) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                // Media Type Dropdown
                ExposedDropdownMenuBox(
                    expanded = mediaTypeExpanded,
                    onExpandedChange = { mediaTypeExpanded = !mediaTypeExpanded }
                ) {
                    OutlinedTextField(
                        value = mediaType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Media Format (AUDIO, VIDEO, GRAPHIC)", color = TextMuted) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = mediaTypeExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceVariantLight,
                            unfocusedContainerColor = SurfaceVariantLight,
                            focusedTextColor = CharcoalBlack,
                            unfocusedTextColor = CharcoalBlack
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = mediaTypeExpanded,
                        onDismissRequest = { mediaTypeExpanded = false }
                    ) {
                        listOf("AUDIO", "VIDEO", "GRAPHIC").forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type, color = CharcoalBlack) },
                                onClick = {
                                    mediaType = type
                                    mediaTypeExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = mediaUrl,
                    onValueChange = { mediaUrl = it },
                    label = { Text("Media Stream / File URL (.mp3 / .mp4 / image)", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = thumbnailUrl,
                    onValueChange = { thumbnailUrl = it },
                    label = { Text("Thumbnail Image URL (Optional)", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = priceOrEstimate,
                    onValueChange = { priceOrEstimate = it },
                    label = { Text("Price or Quote (e.g. Starting ₹1,999)", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = tags,
                    onValueChange = { tags = it },
                    label = { Text("Tags (comma separated)", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = whatsappMsg,
                    onValueChange = { whatsappMsg = it },
                    label = { Text("Custom WhatsApp Message (Optional)", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Highlight as Hot/Featured Spot", color = CharcoalBlack, fontSize = 13.sp)
                    Switch(
                        checked = isFeatured,
                        onCheckedChange = { isFeatured = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = PrimaryPurple, checkedTrackColor = ElectricYellow)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && mediaUrl.isNotBlank()) {
                        val item = MediaItem(
                            id = existingItem?.id ?: 0,
                            sampleId = sampleId.ifBlank { "KTM-SMP-${(100..999).random()}" },
                            title = title,
                            category = category,
                            mediaType = mediaType,
                            mediaUrl = mediaUrl,
                            thumbnailUrl = thumbnailUrl,
                            description = description,
                            priceOrEstimate = priceOrEstimate,
                            tags = tags,
                            isFeatured = isFeatured,
                            whatsappMsg = whatsappMsg
                        )
                        onSave(item)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save", tint = StudioWhite)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Save Sample", color = StudioWhite, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = CardBackgroundLight
    )
}

