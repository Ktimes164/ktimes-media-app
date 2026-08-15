package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.models.MarketplaceAddon
import com.example.data.models.MarketplaceCategory
import com.example.data.models.MarketplaceData
import com.example.data.models.MarketplaceService
import com.example.ui.MediaViewModel
import com.example.ui.theme.CardBackgroundLight
import com.example.ui.theme.CardBorderLight
import com.example.ui.theme.CharcoalBlack
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight
import com.example.ui.theme.StudioWhite
import com.example.ui.theme.SurfaceVariantLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VibrantOrange
import com.example.ui.theme.WhatsAppGreen
import com.example.utils.IntentUtils

@Composable
fun MarketplaceScreen(
    viewModel: MediaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(MarketplaceCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedServiceForOrder by remember { mutableStateOf<MarketplaceService?>(null) }

    val allServices = MarketplaceData.services
    val filteredServices = remember(selectedCategory, searchQuery) {
        allServices.filter { service ->
            val matchesCat = selectedCategory == MarketplaceCategory.ALL || service.category == selectedCategory
            val matchesQuery = searchQuery.isBlank() ||
                    service.title.contains(searchQuery, ignoreCase = true) ||
                    service.marathiTitle.contains(searchQuery, ignoreCase = true) ||
                    service.shortDescription.contains(searchQuery, ignoreCase = true) ||
                    service.deliverables.any { it.contains(searchQuery, ignoreCase = true) }
            matchesCat && matchesQuery
        }
    }

    // Interactive Order Placement Modal
    if (selectedServiceForOrder != null) {
        MarketplaceOrderDialog(
            service = selectedServiceForOrder!!,
            viewModel = viewModel,
            onDismiss = { selectedServiceForOrder = null },
            onOrderPlaced = {
                selectedServiceForOrder = null
                viewModel.onTabSelected("orders")
            }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Hero Banner: Marketplace Introduction
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(PrimaryPurple)
                    .border(1.dp, CrimsonRed, RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CrimsonRed)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "KTIMES MARKETPLACE",
                                color = StudioWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(ElectricYellow)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "Fast Delivery",
                                tint = PrimaryPurple,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "२४ तास सुपरफास्ट",
                                color = PrimaryPurple,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "जाहिरात व डिजिटल मीडिया मार्केटप्लेस",
                        color = StudioWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "व्हिडिओ, ऑडिओ, बॅनर्स, AI व्हिडिओ आणि सोशल मीडिया सेवा थेट स्टुडिओमधून ऑर्डर करा.",
                        color = StudioWhite.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "एकूण ${allServices.size} स्टुडिओ सेवा उपलब्ध",
                            color = ElectricYellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = {
                                IntentUtils.openWhatsAppDirectMessage(
                                    context,
                                    "नमस्कार Ktimes Media, मला तुमच्या जाहिरात मार्केटप्लेस सेवांबद्दल चौकशी करायची आहे."
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_whatsapp),
                                contentDescription = "WhatsApp",
                                tint = StudioWhite,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("स्टुडिओ संपर्क", color = StudioWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 2. Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "व्हिडिओ, जिंन्गल, ३D लोगो, AI अँकर किंवा सोशल मीडिया शोधा...",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = PrimaryPurple)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
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
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        // 3. Category Filter Chips (5 Pillars: Video, Audio, Banner, AI Video, Social Media)
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(MarketplaceCategory.values()) { category ->
                    val isSelected = selectedCategory == category
                    val count = if (category == MarketplaceCategory.ALL) allServices.size
                    else allServices.count { it.category == category }

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${category.marathiLabel} ($count)",
                                    color = if (isSelected) StudioWhite else CharcoalBlack,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = when (category) {
                                MarketplaceCategory.VIDEO -> CrimsonRed
                                MarketplaceCategory.AUDIO -> PrimaryPurple
                                MarketplaceCategory.BANNER -> VibrantOrange
                                MarketplaceCategory.AI_VIDEO -> Color(0xFF00838F)
                                MarketplaceCategory.SOCIAL_MEDIA -> Color(0xFFC2185B)
                                else -> PrimaryPurple
                            },
                            containerColor = CardBackgroundLight
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = CardBorderLight,
                            selectedBorderColor = PrimaryPurple
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        // 4. Results Count Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectedCategory.marathiLabel} (${filteredServices.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalBlack
                )

                Text(
                    text = "सर्व्हिस निवडून ऑर्डर करा",
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }
        }

        // 5. Marketplace Service Cards List
        if (filteredServices.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "No services",
                            tint = PrimaryPurple,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "कोणतीही सेवा सापडली नाही",
                            color = CharcoalBlack,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "कृपया वेगळा शब्द किंवा वर्गवारी निवडा.",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(filteredServices, key = { it.id }) { service ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    MarketplaceServiceCard(
                        service = service,
                        onPlaceOrderClick = { selectedServiceForOrder = service },
                        onQuickInquire = {
                            val inqMsg = """
                                नमस्कार Ktimes Media! 👋
                                मला मार्केटप्लेस सेवेबद्दल माहिती हवी आहे:
                                📌 *सेवा*: ${service.marathiTitle} (${service.title})
                                🆔 *कोड*: ${service.serviceCode}
                                💰 *दर*: ${service.basePrice}
                                ⏳ *मुदत*: ${service.deliveryTime}

                                कृपया सॅम्पल्स आणि पुढील प्रक्रिया सांगावी.
                            """.trimIndent()
                            IntentUtils.openWhatsAppDirectMessage(context, inqMsg)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MarketplaceServiceCard(
    service: MarketplaceService,
    onPlaceOrderClick: () -> Unit,
    onQuickInquire: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = when (service.category) {
        MarketplaceCategory.VIDEO -> CrimsonRed
        MarketplaceCategory.AUDIO -> PrimaryPurple
        MarketplaceCategory.BANNER -> VibrantOrange
        MarketplaceCategory.AI_VIDEO -> Color(0xFF00838F)
        MarketplaceCategory.SOCIAL_MEDIA -> Color(0xFFC2185B)
        else -> PrimaryPurple
    }

    val categoryIcon: ImageVector = when (service.category) {
        MarketplaceCategory.VIDEO -> Icons.Default.Videocam
        MarketplaceCategory.AUDIO -> Icons.Default.Headphones
        MarketplaceCategory.BANNER -> Icons.Default.Brush
        MarketplaceCategory.AI_VIDEO -> Icons.Default.AutoAwesome
        MarketplaceCategory.SOCIAL_MEDIA -> Icons.Default.Public
        else -> Icons.Default.ShoppingBag
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, CardBorderLight, RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Category Badge + Code + Popular Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(categoryColor)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = categoryIcon,
                                contentDescription = null,
                                tint = StudioWhite,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = service.category.marathiLabel,
                                color = StudioWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = service.serviceCode,
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (service.badgeText.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(ElectricYellow)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = service.badgeText,
                            color = PrimaryPurple,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title & Pricing Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.marathiTitle,
                        color = CharcoalBlack,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = service.title,
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = service.basePrice,
                        color = CrimsonRed,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Time",
                            tint = PrimaryPurple,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = service.deliveryTime,
                            color = PrimaryPurple,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Short Description
            Text(
                text = service.shortDescription,
                color = CharcoalBlack.copy(alpha = 0.85f),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Deliverables Checklist
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceVariantLight)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "समाविष्ट वैशिष्ट्ये (Deliverables):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurple
                )
                service.deliverables.take(3).forEach { deliverable ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = deliverable,
                            fontSize = 11.sp,
                            color = CharcoalBlack,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (service.deliverables.size > 3) {
                    Text(
                        text = "+ आणखी ${service.deliverables.size - 3} अतिरिक्त वैशिष्ट्ये...",
                        fontSize = 10.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons: "Order Now" (Opens interactive Form) + "Quick Inquire" (WhatsApp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onPlaceOrderClick,
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "Place Order",
                        tint = StudioWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ऑर्डर करा (Form)", color = StudioWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onQuickInquire,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(44.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_whatsapp),
                        contentDescription = "WhatsApp",
                        tint = StudioWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("WhatsApp", color = StudioWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun MarketplaceOrderDialog(
    service: MarketplaceService,
    viewModel: MediaViewModel,
    onDismiss: () -> Unit,
    onOrderPlaced: () -> Unit
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var clientName by remember { mutableStateOf(currentUser?.displayName ?: "") }
    var businessName by remember { mutableStateOf(currentUser?.businessName ?: "") }
    var phone by remember { mutableStateOf(currentUser?.phoneNumber ?: "") }
    var targetDeadline by remember { mutableStateOf(service.deliveryTime) }
    var scriptNotes by remember { mutableStateOf("") }
    val selectedAddons = remember { mutableStateListOf<MarketplaceAddon>() }

    // Dynamic Price Calculation
    val totalEstimatedPrice = remember(selectedAddons.size) {
        service.priceNumber + selectedAddons.sumOf { it.price }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = StudioWhite,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CrimsonRed)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "मार्केटप्लेस ऑर्डर",
                            color = StudioWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "₹$totalEstimatedPrice",
                        color = CrimsonRed,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = service.marathiTitle,
                    color = CharcoalBlack,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "मूळ दर: ${service.basePrice} • मुदत: ${service.deliveryTime}",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Client Info Fields
                item {
                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("तुमचे नाव (Full Name)*", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = StudioWhite,
                            unfocusedContainerColor = SurfaceVariantLight,
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = CardBorderLight,
                            focusedTextColor = CharcoalBlack,
                            unfocusedTextColor = CharcoalBlack
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("मोबाईल / WhatsApp नंबर*", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = StudioWhite,
                            unfocusedContainerColor = SurfaceVariantLight,
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = CardBorderLight,
                            focusedTextColor = CharcoalBlack,
                            unfocusedTextColor = CharcoalBlack
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text("फर्म / दुकानाचे नाव (Business Name)", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = StudioWhite,
                            unfocusedContainerColor = SurfaceVariantLight,
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = CardBorderLight,
                            focusedTextColor = CharcoalBlack,
                            unfocusedTextColor = CharcoalBlack
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = targetDeadline,
                        onValueChange = { targetDeadline = it },
                        label = { Text("अपेक्षित मुदत (Target Delivery)", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = StudioWhite,
                            unfocusedContainerColor = SurfaceVariantLight,
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = CardBorderLight,
                            focusedTextColor = CharcoalBlack,
                            unfocusedTextColor = CharcoalBlack
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = scriptNotes,
                        onValueChange = { scriptNotes = it },
                        label = { Text("जाहिरात माहिती / स्क्रिप्ट नोट्स (Requirements)", color = TextMuted) },
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = StudioWhite,
                            unfocusedContainerColor = SurfaceVariantLight,
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = CardBorderLight,
                            focusedTextColor = CharcoalBlack,
                            unfocusedTextColor = CharcoalBlack
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Addons Selection
                if (service.addons.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceVariantLight)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "अतिरिक्त पर्याय (Custom Add-ons):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryPurple
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            service.addons.forEach { addon ->
                                val isChecked = selectedAddons.contains(addon)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (isChecked) selectedAddons.remove(addon)
                                            else selectedAddons.add(addon)
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { checked ->
                                                if (checked) selectedAddons.add(addon)
                                                else selectedAddons.remove(addon)
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = PrimaryPurple,
                                                uncheckedColor = TextMuted
                                            )
                                        )
                                        Text(
                                            text = addon.title,
                                            fontSize = 11.sp,
                                            color = CharcoalBlack,
                                            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }

                                    Text(
                                        text = addon.formattedPrice,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CrimsonRed
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (clientName.isBlank() || phone.isBlank()) {
                        Toast.makeText(context, "कृपया नाव आणि मोबाईल नंबर भरा", Toast.LENGTH_SHORT).show()
                    } else {
                        val addonsSummary = if (selectedAddons.isEmpty()) "None"
                        else selectedAddons.joinToString(", ") { "${it.title} (${it.formattedPrice})" }

                        val fullDetails = """
                            सेवा: ${service.marathiTitle} [${service.serviceCode}]
                            नोट्स: ${scriptNotes.ifBlank { "N/A" }}
                            अॅड-ऑन्स: $addonsSummary
                        """.trimIndent()

                        viewModel.submitAdOrder(
                            clientName = clientName,
                            clientPhone = phone,
                            businessName = businessName,
                            serviceType = "${service.category.marathiLabel}: ${service.marathiTitle}",
                            budget = "₹$totalEstimatedPrice",
                            deadline = targetDeadline,
                            details = fullDetails,
                            onOrderCreated = { generatedOrderId ->
                                val whatsappMsg = """
                                    नमस्कार Ktimes Media! 👋
                                    मी मार्केटप्लेसवरून नवीन ऑर्डर नोंदवली आहे:
                                    🆔 *लीड / ऑर्डर ID*: $generatedOrderId
                                    🎯 *सेवा*: ${service.marathiTitle} (${service.serviceCode})
                                    📂 *वर्गवारी*: ${service.category.marathiLabel}
                                    👤 *नाव*: $clientName
                                    🏢 *फर्म / दुकान*: ${businessName.ifBlank { "N/A" }}
                                    📱 *मोबाईल*: $phone
                                    💰 *एकूण अंदाजे दर*: ₹$totalEstimatedPrice
                                    ⏳ *मुदत (Target)*: $targetDeadline
                                    ➕ *अॅड-ऑन्स*: $addonsSummary
                                    📝 *माहिती / स्क्रिप्ट*: ${scriptNotes.ifBlank { "N/A" }}

                                    कृपया ऑर्डर तपासून CRM मध्ये पुढील प्रक्रिया सुरू करावी. धन्यवाद!
                                """.trimIndent()

                                IntentUtils.openWhatsAppDirectMessage(context, whatsappMsg)
                                Toast.makeText(
                                    context,
                                    "ऑर्डर यशस्वीरीत्या नोंदवली! ($generatedOrderId)",
                                    Toast.LENGTH_LONG
                                ).show()
                                onOrderPlaced()
                            }
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_whatsapp),
                    contentDescription = null,
                    tint = StudioWhite,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("ऑर्डर निश्चित करा व WhatsApp सुरू करा", color = StudioWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("रद्द करा (Cancel)", color = TextMuted)
            }
        }
    )
}
