package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush as GradientBrush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.DefaultData
import com.example.data.MediaItem
import com.example.ui.MediaViewModel
import com.example.ui.components.KtimesBottomNavigationBar
import com.example.ui.components.PortfolioItemCard
import com.example.ui.theme.AppBackground
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
import com.example.utils.IntentUtils

import com.example.data.models.AdOrder
import com.example.data.models.AppUser
import com.example.data.models.UserRole
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.LinearProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: MediaViewModel,
    onNavigateToAdmin: () -> Unit,
    onItemClick: (MediaItem) -> Unit
) {
    val context = LocalContext.current
    val items by viewModel.filteredItems.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedMediaType.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentUserRole.collectAsStateWithLifecycle()
    val authStatusMessage by viewModel.authStatusMessage.collectAsStateWithLifecycle()

    var showAuthModal by remember { mutableStateOf(false) }

    LaunchedEffect(authStatusMessage) {
        authStatusMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearAuthStatusMessage()
        }
    }

    if (showAuthModal) {
        AlertDialog(
            onDismissRequest = { showAuthModal = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Account",
                        tint = PrimaryPurple,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "User Profile & Role",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalBlack
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceVariantLight),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Current User: ${currentUser?.displayName ?: "Ganesh Jewellers"}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = CharcoalBlack
                            )
                            Text(
                                text = "Active Role: ${currentRole.name}",
                                fontSize = 12.sp,
                                color = if (currentRole == UserRole.ADMIN) CrimsonRed else PrimaryPurple,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "🔥 Firebase Auth & Firestore Connected",
                                fontSize = 11.sp,
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Text(
                        text = "Switch Application Role:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalBlack
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.switchUserRole(UserRole.CLIENT)
                                showAuthModal = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentRole == UserRole.CLIENT) PrimaryPurple else Color.LightGray
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Client Mode", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                viewModel.switchUserRole(UserRole.ADMIN)
                                showAuthModal = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentRole == UserRole.ADMIN) CrimsonRed else Color.LightGray
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Admin Mode", fontSize = 12.sp)
                        }
                    }

                    if (currentRole == UserRole.ADMIN) {
                        Button(
                            onClick = {
                                showAuthModal = false
                                onNavigateToAdmin()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Admin Console", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.signInWithGoogle(context)
                            showAuthModal = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CharcoalBlack),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Google Sign-In (Firebase)", fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAuthModal = false }) {
                    Text("Close", color = PrimaryPurple, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = StudioWhite,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Ktimes Media Stylized Logo Emblem
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(CrimsonRed)
                                .border(1.dp, ElectricYellow, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "K",
                                color = StudioWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            // Red Crimson Pill with Title
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CrimsonRed)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Ktimes Media",
                                    color = StudioWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "हमेशा समय के साथ..!",
                                color = ElectricYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                },
                actions = {
                    // Role Badge Pill
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (currentRole == UserRole.ADMIN) CrimsonRed else ElectricYellow)
                            .clickable { showAuthModal = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (currentRole == UserRole.ADMIN) "ADMIN" else "CLIENT",
                            color = if (currentRole == UserRole.ADMIN) StudioWhite else PrimaryPurple,
                            fontWeight = FontWeight.Black,
                            fontSize = 10.sp
                        )
                    }

                    // Notification Bell Icon
                    IconButton(onClick = {
                        Toast.makeText(context, "Ktimes Media: नवनवीन ऑफर्स आणि अपडेट्स चालू आहेत!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification Bell",
                            tint = ElectricYellow
                        )
                    }

                    // User profile initial 'S' / Admin icon
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ElectricYellow)
                            .clickable {
                                if (currentRole == UserRole.ADMIN) {
                                    onNavigateToAdmin()
                                } else {
                                    showAuthModal = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (currentRole == UserRole.ADMIN) "A" else "C",
                            color = PrimaryPurple,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryPurple
                )
            )
        },
        bottomBar = {
            KtimesBottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { tab -> viewModel.onTabSelected(tab) }
            )
        },
        containerColor = AppBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentTab) {
                "home" -> HomeScreenContent(
                    viewModel = viewModel,
                    onItemClick = onItemClick
                )
                "portfolio" -> PortfolioScreenContent(
                    viewModel = viewModel,
                    items = items,
                    selectedCategory = selectedCategory,
                    selectedType = selectedType,
                    searchQuery = searchQuery,
                    onItemClick = onItemClick
                )
                "rates" -> RatesScreenContent(
                    onSelectPackage = { pkgName ->
                        viewModel.createLeadFromPackage(pkgName, "₹१,९९९ - ₹४,९९९")
                        IntentUtils.openWhatsAppDirectMessage(
                            context,
                            "नमस्कार Ktimes Media, मला '$pkgName' या पॅकेजची ऑर्डर करायची आहे. कृपया अधिक माहिती पाठवा."
                        )
                    }
                )
                "orders" -> OrdersScreenContent(
                    viewModel = viewModel,
                    onWhatsAppOrder = { name, phone, service, details ->
                        // Save directly to Firestore
                        viewModel.submitAdOrder(
                            clientName = name,
                            clientPhone = phone,
                            serviceType = service,
                            budget = "₹1,499 - ₹4,999",
                            details = details
                        )

                        val message = """
                            नमस्कार Ktimes Media, मी ॲपवरून नवीन ऑर्डर नोंदवत आहे:
                            👤 *नाव*: $name
                            📱 *मोबाईल*: $phone
                            🎯 *सेवा*: $service
                            📝 *माहिती/संदेश*: $details
                        """.trimIndent()
                        IntentUtils.openWhatsAppDirectMessage(context, message)
                    }
                )
                else -> HomeScreenContent(
                    viewModel = viewModel,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    viewModel: MediaViewModel,
    onItemClick: (MediaItem) -> Unit
) {
    val items by viewModel.filteredItems.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Main Visual Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, PrimaryPurpleLight, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = PrimaryPurple)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            GradientBrush.horizontalGradient(
                                colors = listOf(PrimaryPurple, PrimaryPurpleLight)
                            )
                        )
                        .padding(16.dp)
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
                                    text = "विशेष ऑफर",
                                    color = StudioWhite,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "२४/७ डिजिटल स्टुडिओ",
                                color = ElectricYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "तुमच्या जाहिरातीला 'Ktimes Media' ची जोड! जबरदस्त ऑडिओ आणि व्हिडिओ जाहिराती बनवा.",
                            color = StudioWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 24.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ऑडिओ जाहिराती • टीव्हीसी • निवडणूक जिंन्गल्स",
                                color = ElectricYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Button(
                                onClick = {
                                    viewModel.createLeadFromBanner("२४/७ डिजिटल स्टुडिओ विशेष ऑफर")
                                    IntentUtils.openWhatsAppDirectMessage(
                                        context,
                                        "नमस्कार Ktimes Media, मी ॲपवरून संपर्क साधत आहे. मला आपल्या सेवांबद्दल माहिती हवी आहे."
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                                shape = RoundedCornerShape(20.dp),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_whatsapp),
                                    contentDescription = "WhatsApp",
                                    tint = StudioWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("ऑर्डर करा", color = StudioWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Category Horizontal Scroll (Four Circular Categories)
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(CrimsonRed)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "मुख्य वर्गवारी (Categories)",
                        color = StudioWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CircularCategoryItem(
                        title = "ऑडिओ जाहिराती",
                        icon = Icons.Default.Headphones,
                        onClick = { viewModel.navigateToCategoryInPortfolio("ऑडिओ जाहिराती", "AUDIO") }
                    )

                    CircularCategoryItem(
                        title = "व्हिडिओ जाहिराती",
                        icon = Icons.Default.Videocam,
                        onClick = { viewModel.navigateToCategoryInPortfolio("व्हिडिओ जाहिराती", "VIDEO") }
                    )

                    CircularCategoryItem(
                        title = "ग्राफिक्स डिझाइन",
                        icon = Icons.Default.Brush,
                        onClick = { viewModel.navigateToCategoryInPortfolio("ग्राफिक्स डिझाइन", "GRAPHIC") }
                    )

                    CircularCategoryItem(
                        title = "निवडणूक स्पेशल",
                        icon = Icons.Default.HowToVote,
                        onClick = { viewModel.navigateToCategoryInPortfolio("निवडणूक स्पेशल", "AUDIO") }
                    )
                }
            }
        }

        // Featured Hot Spots Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CrimsonRed)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "हॉट स्पॉट्स / टॉप नमुने",
                        color = StudioWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "सर्व पहा →",
                    color = PrimaryPurple,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { viewModel.onTabSelected("portfolio") }
                )
            }
        }

        // Featured Media Items List
        val featuredList = items.filter { it.isFeatured }
        items(if (featuredList.isNotEmpty()) featuredList else items, key = { it.id }) { item ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                PortfolioItemCard(
                    item = item,
                    onItemClick = onItemClick,
                    onOrderClick = { orderItem -> viewModel.createLeadFromItem(orderItem) }
                )
            }
        }
    }
}

@Composable
fun CircularCategoryItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(68.dp)
                .clip(CircleShape)
                .background(PrimaryPurple)
                .border(2.dp, ElectricYellow, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = ElectricYellow,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            color = CharcoalBlack,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PortfolioScreenContent(
    viewModel: MediaViewModel,
    items: List<MediaItem>,
    selectedCategory: String,
    selectedType: String,
    searchQuery: String,
    onItemClick: (MediaItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Search Input Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            placeholder = {
                Text("ऑडिओ, व्हिडिओ, जाहिरात किंवा निवडणूक स्पेशल शोधा...", color = TextMuted, fontSize = 12.sp)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = PrimaryPurple)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
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
                .padding(horizontal = 16.dp, vertical = 6.dp)
        )

        // Media Format Filter Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardBackgroundLight)
                .border(1.dp, CardBorderLight, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val mediaTypes = listOf("ALL" to "सर्व", "AUDIO" to "ऑडिओ", "VIDEO" to "व्हिडिओ", "GRAPHIC" to "ग्राफिक्स")
            mediaTypes.forEach { (typeCode, typeLabel) ->
                val isSelected = selectedType == typeCode
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .then(
                            if (isSelected) Modifier.background(PrimaryPurple)
                            else Modifier.background(Color.Transparent)
                        )
                        .clickable { viewModel.onMediaTypeSelected(typeCode) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = typeLabel,
                        color = if (isSelected) ElectricYellow else CharcoalBlack,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(DefaultData.categories) { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.onCategorySelected(category) },
                    label = {
                        Text(
                            text = category,
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
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        // Media Portfolio List
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Empty",
                        tint = PrimaryPurple,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "काहीही नमुने सापडले नाहीत",
                        color = CharcoalBlack,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(items, key = { it.id }) { item ->
                    PortfolioItemCard(
                        item = item,
                        onItemClick = onItemClick,
                        onOrderClick = { orderItem -> viewModel.createLeadFromItem(orderItem) }
                    )
                }
            }
        }
    }
}

@Composable
fun RatesScreenContent(
    onSelectPackage: (String) -> Unit
) {
    val packages = listOf(
        Triple("ऑडिओ जाहिरात (Audio Ads)", "₹१,९९९ पासून", listOf("३० सेकंद हाय-क्वालिटी व्हॉईसओव्हर", "पार्श्वसंगीत आणि साऊंड इफेक्ट्स", "एफएम रेडिओ व लाउडस्पीकर रेडी", "२४ ते ४८ तासांत डिलिव्हरी")),
        Triple("व्हिडिओ जाहिरात (Video Ads / TVC)", "₹४,९९९ पासून", listOf("4K अल्ट्रा HD व्हिडिओ प्रोडक्शन", "३D मोशन ग्राफिक्स व लोगो अॅनिमेशन", "स्टुडिओ निवेदन आणि टायपोग्राफी", "टीव्ही व सोशल मीडिया फॉर्मॅट")),
        Triple("निवडणूक प्रचार स्पेशल (Election Package)", "₹२,४९९ पासून", listOf("उमेदवाराच्या नावासह धडाकेबाज गाणे", "रिक्षा व लाउडस्पीकरसाठी खणखणीत ऑडिओ", "डिजिटल प्रचार पोस्टर्स बंडल")),
        Triple("ग्राफिक्स व सोशल मीडिया (Graphics Bundle)", "₹९९९ पासून", listOf("३D धातू / गोल लोगो डिझाइन", "फ्लेक्स बॅनर्स व व्हेक्टर पोस्टर्स", "व्हॉट्सॲप व इन्स्टाग्राम पोस्ट्स"))
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(CrimsonRed)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "रेट कार्ड आणि स्टुडिओ पॅकेजेस",
                    color = StudioWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        items(packages) { (title, price, features) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, CardBorderLight, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CardBackgroundLight)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            color = CharcoalBlack,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricYellow)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = price,
                                color = PrimaryPurple,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    features.forEach { feat ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Feature",
                                tint = PrimaryPurple,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = feat, color = CharcoalBlack, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { onSelectPackage(title) },
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_whatsapp),
                            contentDescription = "WhatsApp",
                            tint = StudioWhite,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ऑर्डर करा (WhatsApp)", color = StudioWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun OrdersScreenContent(
    viewModel: MediaViewModel,
    onWhatsAppOrder: (name: String, phone: String, service: String, details: String) -> Unit
) {
    val context = LocalContext.current
    val liveOrders by viewModel.ordersList.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    var name by remember { mutableStateOf(currentUser?.displayName ?: "") }
    var phone by remember { mutableStateOf(currentUser?.phoneNumber ?: "") }
    var selectedService by remember { mutableStateOf("ऑडिओ जाहिरात (Audio Ad)") }
    var details by remember { mutableStateOf("") }
    var orderTab by remember { mutableStateOf("NEW") } // "NEW", "MY_ORDERS"

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(CrimsonRed)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "ऑर्डर व थेट संपर्क (Orders & CRM)",
                        color = StudioWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(SurfaceVariantLight)
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (orderTab == "NEW") PrimaryPurple else Color.Transparent)
                            .clickable { orderTab = "NEW" }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "नवीन ऑर्डर",
                            color = if (orderTab == "NEW") StudioWhite else CharcoalBlack,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(if (orderTab == "MY_ORDERS") PrimaryPurple else Color.Transparent)
                            .clickable { orderTab = "MY_ORDERS" }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "माझ्या ऑर्डर्स (${liveOrders.size})",
                            color = if (orderTab == "MY_ORDERS") StudioWhite else CharcoalBlack,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (orderTab == "NEW") {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, CardBorderLight, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = CardBackgroundLight)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "तुमची ऑर्डर माहिती भरा:",
                                color = CharcoalBlack,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "🔥 Live Firestore Sync",
                                color = Color(0xFF2E7D32),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("तुमचे नाव (Full Name)", color = TextMuted) },
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

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("मोबाईल नंबर (Mobile Number)", color = TextMuted) },
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

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = selectedService,
                            onValueChange = { selectedService = it },
                            label = { Text("सेवेचा प्रकार (Service Type)", color = TextMuted) },
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

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = details,
                            onValueChange = { details = it },
                            label = { Text("संदेश / जाहिरातीची माहिती (Details / Script Notes)", color = TextMuted) },
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

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (name.isBlank() || phone.isBlank()) {
                                    Toast.makeText(context, "कृपया नाव आणि मोबाईल नंबर भरा", Toast.LENGTH_SHORT).show()
                                } else {
                                    onWhatsAppOrder(name, phone, selectedService, details)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_whatsapp),
                                contentDescription = "WhatsApp",
                                tint = StudioWhite,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ऑर्डर नोंदवा व WhatsApp सुरू करा", color = StudioWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { IntentUtils.makePhoneCall(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhoneInTalk,
                                contentDescription = "Call",
                                tint = ElectricYellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("थेट कॉल करा (+91 9422337471)", color = StudioWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // "MY_ORDERS" Tab - Live from Firestore
            if (liveOrders.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CardBackgroundLight),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ListAlt,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("अद्याप कोणतीही ऑर्डर नाही.", color = CharcoalBlack, fontWeight = FontWeight.Bold)
                            Text("नवीन जाहिरात ऑर्डर नोंदवण्यासाठी 'नवीन ऑर्डर' वर क्लिक करा.", color = TextMuted, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                items(liveOrders) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, CardBorderLight, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = CardBackgroundLight)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
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
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = order.status,
                                            color = if (order.status == "New Lead" || order.status == "Quotation Sent") PrimaryPurple else StudioWhite,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = order.details.ifBlank { order.serviceType },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = CharcoalBlack
                                    )
                                    Text(
                                        text = "सेवा: ${order.serviceType} • ID: ${order.id}",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }

                                Text(
                                    text = order.budget,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp,
                                    color = CrimsonRed
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Progress Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("प्रगती (Progress)", fontSize = 11.sp, color = TextMuted)
                                Text("${order.progress}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
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

                            Button(
                                onClick = {
                                    val updateMsg = "नमस्कार Ktimes Media, माझ्या ऑर्डर ID: ${order.id} (${order.serviceType}) बद्दल अपडेट हवी आहे."
                                    IntentUtils.openWhatsAppDirectMessage(context, updateMsg)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurpleLight),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().height(38.dp)
                            ) {
                                Text("WhatsApp वर अपडेट तपासा", color = StudioWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}


