package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
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
    val allOrders by viewModel.ordersList.collectAsStateWithLifecycle()
    val filteredOrders by viewModel.crmFilteredOrders.collectAsStateWithLifecycle()
    val crmSearchQuery by viewModel.crmSearchQuery.collectAsStateWithLifecycle()
    val crmSelectedStage by viewModel.crmSelectedStage.collectAsStateWithLifecycle()
    val adminMessage by viewModel.adminMessage.collectAsStateWithLifecycle()

    var activeAdminTab by remember { mutableStateOf("CRM") } // "PORTFOLIO" or "CRM" (defaults to CRM for client pipeline)

    // Portfolio state
    var showAddDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<MediaItem?>(null) }
    var itemToDelete by remember { mutableStateOf<MediaItem?>(null) }

    // CRM Lead state
    var showAddLeadDialog by remember { mutableStateOf(false) }
    var leadToEdit by remember { mutableStateOf<AdOrder?>(null) }
    var leadToDelete by remember { mutableStateOf<AdOrder?>(null) }
    var leadToChangeStage by remember { mutableStateOf<AdOrder?>(null) }

    // JSON export/import state
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
                            text = "🔥 Firebase Auth & Firestore Live",
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
            } else {
                FloatingActionButton(
                    onClick = {
                        showAddLeadDialog = true
                    },
                    containerColor = VibrantOrange,
                    contentColor = StudioWhite
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add New CRM Lead"
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
                        .background(if (activeAdminTab == "CRM") PrimaryPurple else Color.Transparent)
                        .clickable { activeAdminTab = "CRM" }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CRM Leads Pipeline (${allOrders.size})",
                        color = if (activeAdminTab == "CRM") StudioWhite else CharcoalBlack,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

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
                    // 1. Pipeline Metrics Overview Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = CardBackgroundLight),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderLight)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Advertising CRM Pipeline",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 15.sp,
                                            color = CharcoalBlack
                                        )
                                        Text(
                                            text = "ऑटोमॅटिक लीड ट्रॅकिंग व ऑर्डर्स",
                                            fontSize = 11.sp,
                                            color = Color(0xFF2E7D32),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Button(
                                        onClick = { showAddLeadDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("New Lead", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Metric Counters Grid
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    PipelineMetricChip(
                                        title = "नवीन लीड्स",
                                        count = allOrders.count { it.status == "New Lead" },
                                        bgColor = CrimsonRed,
                                        modifier = Modifier.weight(1f)
                                    )
                                    PipelineMetricChip(
                                        title = "प्रॉडक्शन",
                                        count = allOrders.count { it.status == "Production" },
                                        bgColor = PrimaryPurple,
                                        modifier = Modifier.weight(1f)
                                    )
                                    PipelineMetricChip(
                                        title = "मंजुरी",
                                        count = allOrders.count { it.status == "Approval" },
                                        bgColor = VibrantOrange,
                                        modifier = Modifier.weight(1f)
                                    )
                                    PipelineMetricChip(
                                        title = "वितरित",
                                        count = allOrders.count { it.status == "Delivered" },
                                        bgColor = Color(0xFF2E7D32),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    // 2. Search & Stage Filter Bar
                    item {
                        Column {
                            OutlinedTextField(
                                value = crmSearchQuery,
                                onValueChange = { viewModel.onCrmSearchQueryChanged(it) },
                                placeholder = {
                                    Text("क्लायंटचे नाव, मोबाईल, व्यवसाय किंवा सेवा शोधा...", color = TextMuted, fontSize = 12.sp)
                                },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = PrimaryPurple)
                                },
                                trailingIcon = {
                                    if (crmSearchQuery.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.onCrmSearchQueryChanged("") }) {
                                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted)
                                        }
                                    }
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = StudioWhite,
                                    unfocusedContainerColor = CardBackgroundLight,
                                    focusedBorderColor = PrimaryPurple,
                                    unfocusedBorderColor = CardBorderLight,
                                    focusedTextColor = CharcoalBlack,
                                    unfocusedTextColor = CharcoalBlack
                                ),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Stage Filter Chips
                            val stages = listOf(
                                "ALL" to "All (${allOrders.size})",
                                "New Lead" to "नवीन (${allOrders.count { it.status == "New Lead" }})",
                                "Requirement Received" to "गरजा प्राप्त (${allOrders.count { it.status == "Requirement Received" }})",
                                "Quotation Sent" to "कोटेशन पाठवले (${allOrders.count { it.status == "Quotation Sent" }})",
                                "Production" to "प्रॉडक्शन चालू (${allOrders.count { it.status == "Production" }})",
                                "Approval" to "मंजुरी बाकी (${allOrders.count { it.status == "Approval" }})",
                                "Delivered" to "पूर्ण / वितरित (${allOrders.count { it.status == "Delivered" }})"
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                stages.forEach { (stageKey, stageLabel) ->
                                    val isSelected = crmSelectedStage == stageKey
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { viewModel.onCrmStageSelected(stageKey) },
                                        label = {
                                            Text(
                                                text = stageLabel,
                                                color = if (isSelected) StudioWhite else CharcoalBlack,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 11.sp
                                            )
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = PrimaryPurple,
                                            containerColor = CardBackgroundLight
                                        ),
                                        border = FilterChipDefaults.filterChipBorder(
                                            enabled = true,
                                            selected = isSelected,
                                            borderColor = if (isSelected) PrimaryPurple else CardBorderLight,
                                            selectedBorderColor = PrimaryPurple
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 3. Leads List
                    if (filteredOrders.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.Business,
                                        contentDescription = "No Leads",
                                        tint = PrimaryPurple,
                                        modifier = Modifier.size(44.dp)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "कोणत्याही लीड्स आढळल्या नाहीत",
                                        color = CharcoalBlack,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "नवीन लीड नोंदवण्यासाठी '+ New Lead' वर क्लिक करा",
                                        color = TextMuted,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    } else {
                        items(filteredOrders, key = { it.id }) { order ->
                            AdminLeadCard(
                                order = order,
                                onAdvance = { viewModel.advanceOrderStage(order) },
                                onChangeStage = { leadToChangeStage = order },
                                onEdit = { leadToEdit = order },
                                onDelete = { leadToDelete = order }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Dialog Form for Portfolio Samples
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

    // Add Manual Lead Dialog
    if (showAddLeadDialog) {
        AddEditLeadDialog(
            existingLead = null,
            onDismiss = { showAddLeadDialog = false },
            onSave = { lead ->
                viewModel.createManualLead(
                    clientName = lead.clientName,
                    clientPhone = lead.clientPhone,
                    serviceType = lead.serviceType,
                    budget = lead.budget,
                    details = lead.details,
                    businessName = lead.businessName,
                    deadline = lead.deadline,
                    location = lead.location
                )
                showAddLeadDialog = false
            }
        )
    }

    // Edit Existing Lead Dialog
    leadToEdit?.let { existing ->
        AddEditLeadDialog(
            existingLead = existing,
            onDismiss = { leadToEdit = null },
            onSave = { updatedLead ->
                viewModel.updateFullOrder(updatedLead)
                leadToEdit = null
            }
        )
    }

    // Change Stage Dialog
    leadToChangeStage?.let { order ->
        ChangeStageDialog(
            currentStage = order.status,
            onDismiss = { leadToChangeStage = null },
            onSelectStage = { newStage, newProgress ->
                viewModel.updateOrderStatus(order.id, newStage, newProgress)
                leadToChangeStage = null
            }
        )
    }

    // Delete Lead Confirmation Dialog
    leadToDelete?.let { order ->
        AlertDialog(
            onDismissRequest = { leadToDelete = null },
            title = { Text("लीड हटवायची आहे का?", color = CrimsonRed, fontWeight = FontWeight.Bold) },
            text = { Text("तुम्हाला '${order.clientName}' (${order.serviceType}) ची लीड डिलीट करायची आहे का?", color = CharcoalBlack) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteOrder(order.id)
                    leadToDelete = null
                }) {
                    Text("Delete", color = CrimsonRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { leadToDelete = null }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = CardBackgroundLight
        )
    }

    // Delete Portfolio Sample Confirmation Dialog
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
fun PipelineMetricChip(
    title: String,
    count: Int,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor.copy(alpha = 0.12f))
            .border(1.dp, bgColor.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(vertical = 8.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$count",
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = bgColor
            )
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = CharcoalBlack
            )
        }
    }
}

@Composable
fun AdminLeadCard(
    order: AdOrder,
    onAdvance: () -> Unit,
    onChangeStage: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val stageBgColor = when (order.status) {
        "Delivered" -> Color(0xFF2E7D32)
        "Approval" -> Color(0xFFF57F17)
        "Production" -> PrimaryPurple
        "Quotation Sent" -> Color(0xFF0288D1)
        "Requirement Received" -> VibrantOrange
        else -> CrimsonRed
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, CardBorderLight, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundLight)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Client Name, Order ID, Stage Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = order.clientName.ifBlank { "अनामिक क्लायंट" },
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = CharcoalBlack
                        )
                        if (order.businessName.isNotBlank()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${order.businessName})",
                                fontSize = 12.sp,
                                color = TextMuted,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        // Order ID Chip (Tap to copy)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SurfaceVariantLight)
                                .clickable {
                                    clipboardManager.setText(AnnotatedString(order.id))
                                    Toast.makeText(context, "Order ID ${order.id} copied!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = order.id.take(12),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple
                            )
                        }

                        if (order.location.isNotBlank()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(12.dp), tint = TextMuted)
                            Text(text = order.location, fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }

                // Stage Badge (Clickable to change stage)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(stageBgColor)
                        .clickable { onChangeStage() }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = order.status,
                            color = StudioWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = "Change",
                            tint = StudioWhite,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Service, Budget & Deadline Info Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceVariantLight.copy(alpha = 0.6f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎯 ${order.serviceType}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
                Text(
                    text = "बजेट: ${order.budget}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CrimsonRed
                )
                if (order.deadline.isNotBlank()) {
                    Text(
                        text = "⏳ ${order.deadline}",
                        fontSize = 11.sp,
                        color = CharcoalBlack
                    )
                }
            }

            if (order.details.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(StudioWhite)
                        .border(0.5.dp, CardBorderLight, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "📝 माहिती / स्क्रिप्ट: ${order.details}",
                        fontSize = 12.sp,
                        color = CharcoalBlack,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar & Percentage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("पायरी प्रगती (Progress): ${order.progress}%", fontSize = 11.sp, color = TextMuted)
                Text(
                    text = when (order.status) {
                        "New Lead" -> "Next: गरजा प्राप्त"
                        "Requirement Received" -> "Next: कोटेशन"
                        "Quotation Sent" -> "Next: प्रॉडक्शन"
                        "Production" -> "Next: मंजुरी"
                        "Approval" -> "Next: पूर्ण डिलिव्हरी"
                        else -> "पूर्ण झाले"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { order.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = stageBgColor,
                trackColor = SurfaceVariantLight
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Advance Stage
                if (order.status != "Delivered") {
                    Button(
                        onClick = onAdvance,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(38.dp)
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Advance", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Call
                Button(
                    onClick = { IntentUtils.makePhoneCall(context, order.clientPhone) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp),
                    modifier = Modifier
                        .weight(0.8f)
                        .height(38.dp)
                ) {
                    Icon(Icons.Default.PhoneInTalk, contentDescription = "Call", modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Call", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // WhatsApp
                Button(
                    onClick = {
                        val msg = """
                            नमस्कार ${order.clientName},
                            Ktimes Media कडून आपल्या जाहिरात ऑर्डरबाबत अपडेट:
                            🎯 सेवा: ${order.serviceType}
                            📋 सद्यस्थिती: *${order.status}*
                            ⏳ प्रगती: ${order.progress}%
                            
                            अधिक माहितीसाठी संपर्क साधा. धन्यवाद!
                        """.trimIndent()
                        IntentUtils.openWhatsAppDirectMessage(context, msg, order.clientPhone)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp),
                    modifier = Modifier
                        .weight(0.9f)
                        .height(38.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_whatsapp),
                        contentDescription = "WhatsApp",
                        tint = StudioWhite,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Chat", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Edit Button
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = PrimaryPurple)
                }

                // Delete Button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CrimsonRed)
                }
            }
        }
    }
}

@Composable
fun ChangeStageDialog(
    currentStage: String,
    onDismiss: () -> Unit,
    onSelectStage: (String, Int) -> Unit
) {
    val stages = listOf(
        Triple("New Lead", 10, "नवीन लीड - नुकतीच विचारणा आली आहे"),
        Triple("Requirement Received", 25, "गरजा प्राप्त - स्क्रिप्ट/माहिती मिळाली"),
        Triple("Quotation Sent", 40, "कोटेशन पाठवले - बजेट चर्चा चालू"),
        Triple("Production", 65, "प्रॉडक्शन चालू - रेकॉर्डिंग व एडिटिंग सुरू"),
        Triple("Approval", 85, "मंजुरी बाकी - क्लायंटकडे सॅम्पल पाठवले"),
        Triple("Delivered", 100, "पूर्ण / वितरित - फायनल मीडिया दिला व पेमेंट पूर्ण")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "पायरी बदला (Change Stage)",
                color = PrimaryPurple,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stages.forEach { (stageName, progress, desc) ->
                    val isSelected = currentStage == stageName
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectStage(stageName, progress) }
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) PrimaryPurple else CardBorderLight,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) PrimaryPurple.copy(alpha = 0.1f) else StudioWhite
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = PrimaryPurple,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stageName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isSelected) PrimaryPurple else CharcoalBlack
                                    )
                                    Text(
                                        text = "$progress%",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = PrimaryPurple
                                    )
                                }
                                Text(
                                    text = desc,
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = CardBackgroundLight
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditLeadDialog(
    existingLead: AdOrder?,
    onDismiss: () -> Unit,
    onSave: (AdOrder) -> Unit
) {
    var clientName by remember { mutableStateOf(existingLead?.clientName ?: "") }
    var clientPhone by remember { mutableStateOf(existingLead?.clientPhone ?: "") }
    var businessName by remember { mutableStateOf(existingLead?.businessName ?: "") }
    var serviceType by remember { mutableStateOf(existingLead?.serviceType ?: "ऑडिओ जाहिरात (Audio Ad)") }
    var budget by remember { mutableStateOf(existingLead?.budget ?: "₹1,999") }
    var deadline by remember { mutableStateOf(existingLead?.deadline ?: "३ दिवस") }
    var location by remember { mutableStateOf(existingLead?.location ?: "सातारा") }
    var details by remember { mutableStateOf(existingLead?.details ?: "") }
    var status by remember { mutableStateOf(existingLead?.status ?: "New Lead") }
    var progress by remember { mutableStateOf(existingLead?.progress ?: 10) }

    var serviceExpanded by remember { mutableStateOf(false) }
    val services = listOf(
        "ऑडिओ जाहिरात (Audio Ad)",
        "व्हिडिओ जाहिरात (Video Ad / TVC)",
        "निवडणूक प्रचार (Election Campaign)",
        "सोशल मीडिया ग्राफिक्स (Graphics)",
        "पूर्ण 360° ब्रँडिंग (360 Branding)",
        "व्हॉइसओव्हर आणि डबिंग (Voiceover)"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (existingLead == null) "नवीन लीड नोंदवा (Add New Lead)" else "लीड संपादित करा (Edit Lead)",
                color = PrimaryPurple,
                fontSize = 16.sp,
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
                    value = clientName,
                    onValueChange = { clientName = it },
                    label = { Text("क्लायंटचे नाव (Client Name)*", color = TextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = clientPhone,
                    onValueChange = { clientPhone = it },
                    label = { Text("मोबाईल नंबर (WhatsApp/Phone)*", color = TextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = businessName,
                    onValueChange = { businessName = it },
                    label = { Text("व्यवसायाचे / दुकानाचे नाव (Business Name)", color = TextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Service Dropdown
                ExposedDropdownMenuBox(
                    expanded = serviceExpanded,
                    onExpandedChange = { serviceExpanded = !serviceExpanded }
                ) {
                    OutlinedTextField(
                        value = serviceType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("जाहिरात सेवा (Service Type)", color = TextMuted) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = serviceExpanded) },
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
                        expanded = serviceExpanded,
                        onDismissRequest = { serviceExpanded = false }
                    ) {
                        services.forEach { srv ->
                            DropdownMenuItem(
                                text = { Text(srv, color = CharcoalBlack) },
                                onClick = {
                                    serviceType = srv
                                    serviceExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = budget,
                        onValueChange = { budget = it },
                        label = { Text("बजेट (Budget)", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceVariantLight,
                            unfocusedContainerColor = SurfaceVariantLight,
                            focusedTextColor = CharcoalBlack,
                            unfocusedTextColor = CharcoalBlack
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = deadline,
                        onValueChange = { deadline = it },
                        label = { Text("मुदत (Deadline)", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SurfaceVariantLight,
                            unfocusedContainerColor = SurfaceVariantLight,
                            focusedTextColor = CharcoalBlack,
                            unfocusedTextColor = CharcoalBlack
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("गाव / शहर (Location)", color = TextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("स्क्रिप्ट / जाहिरातीची माहिती (Notes)", color = TextMuted) },
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceVariantLight,
                        unfocusedContainerColor = SurfaceVariantLight,
                        focusedTextColor = CharcoalBlack,
                        unfocusedTextColor = CharcoalBlack
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (clientName.isNotBlank() && clientPhone.isNotBlank()) {
                        val result = existingLead?.copy(
                            clientName = clientName,
                            clientPhone = clientPhone,
                            businessName = businessName,
                            serviceType = serviceType,
                            budget = budget,
                            deadline = deadline,
                            location = location,
                            details = details,
                            status = status,
                            progress = progress
                        ) ?: AdOrder(
                            id = "",
                            clientName = clientName,
                            clientPhone = clientPhone,
                            businessName = businessName,
                            serviceType = serviceType,
                            budget = budget,
                            deadline = deadline,
                            location = location,
                            details = details,
                            status = status,
                            progress = progress
                        )
                        onSave(result)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save", tint = StudioWhite)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Save Lead", color = StudioWhite, fontWeight = FontWeight.Bold)
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
                        DefaultData.categories.filter { it != "सर्व नमुने (All)" }.forEach { cat ->
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
