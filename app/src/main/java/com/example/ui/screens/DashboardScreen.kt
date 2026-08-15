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
import androidx.compose.material.icons.filled.AutoAwesome
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
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
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
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.data.models.MarketplaceCategory
import com.example.data.models.MarketplaceService
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
                "marketplace", "rates" -> MarketplaceScreen(
                    viewModel = viewModel
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

        // Advertising Marketplace 5-Pillar Category Strip
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CrimsonRed)
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "जाहिरात मार्केटप्लेस (Marketplace)",
                            color = StudioWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "सर्व सेवा →",
                        color = PrimaryPurple,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { viewModel.onTabSelected("marketplace") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        CircularCategoryItem(
                            title = "व्हिडिओ जाहिराती",
                            icon = Icons.Default.Videocam,
                            badge = "4K / TVC",
                            onClick = { viewModel.navigateToMarketplace(MarketplaceCategory.VIDEO) }
                        )
                    }
                    item {
                        CircularCategoryItem(
                            title = "ऑडिओ व जिंन्गल्स",
                            icon = Icons.Default.Headphones,
                            badge = "FM / ऑटो",
                            onClick = { viewModel.navigateToMarketplace(MarketplaceCategory.AUDIO) }
                        )
                    }
                    item {
                        CircularCategoryItem(
                            title = "बॅनर्स व फ्लेक्स",
                            icon = Icons.Default.Brush,
                            badge = "3D / HD",
                            onClick = { viewModel.navigateToMarketplace(MarketplaceCategory.BANNER) }
                        )
                    }
                    item {
                        CircularCategoryItem(
                            title = "AI व्हिडिओ",
                            icon = Icons.Default.AutoAwesome,
                            badge = "न्यूज अँकर",
                            onClick = { viewModel.navigateToMarketplace(MarketplaceCategory.AI_VIDEO) }
                        )
                    }
                    item {
                        CircularCategoryItem(
                            title = "सोशल मीडिया",
                            icon = Icons.Default.Public,
                            badge = "३० दिवस",
                            onClick = { viewModel.navigateToMarketplace(MarketplaceCategory.SOCIAL_MEDIA) }
                        )
                    }
                }
            }
        }

        // Marketplace Promo Spotlight Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, CrimsonRed.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .clickable { viewModel.onTabSelected("marketplace") },
                colors = CardDefaults.cardColors(containerColor = PrimaryPurpleLight.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Storefront,
                                contentDescription = null,
                                tint = PrimaryPurple,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Ktimes Direct Marketplace",
                                color = PrimaryPurple,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "व्हिडिओ, ऑडिओ, ३D बॅनर्स, AI व्हॉइस व सोशल मीडिया बंडल एका क्लिकवर ऑर्डर करा.",
                            color = CharcoalBlack,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = { viewModel.onTabSelected("marketplace") },
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("ऑर्डर करा", color = StudioWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
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
    badge: String = "",
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(2.dp)
            .width(76.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(PrimaryPurple)
                .border(2.dp, ElectricYellow, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = ElectricYellow,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = CharcoalBlack,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (badge.isNotBlank()) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(CrimsonRed)
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = badge,
                    color = StudioWhite,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
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
    var businessName by remember { mutableStateOf(currentUser?.businessName ?: "") }
    var phone by remember { mutableStateOf(currentUser?.phoneNumber ?: "") }
    var selectedService by remember { mutableStateOf("ऑडिओ जाहिरात (Audio Ad)") }
    var selectedBudget by remember { mutableStateOf("₹१,९९९") }
    var deadline by remember { mutableStateOf("३ दिवस") }
    var details by remember { mutableStateOf("") }
    var orderTab by remember { mutableStateOf("MY_ORDERS") } // "NEW", "MY_ORDERS"

    var orderForRevision by remember { mutableStateOf<AdOrder?>(null) }
    var orderForApproval by remember { mutableStateOf<AdOrder?>(null) }

    val serviceOptions = listOf(
        "ऑडिओ जाहिरात (Audio Ad)",
        "व्हिडिओ जाहिरात (Video Commercial)",
        "सोशल मीडिया पोस्टर्स (Social Graphics)",
        "AI व्हॉइस व स्क्रिप्ट (AI Voice & Script)",
        "३६०° डिजिटल कॅम्पेन (360 Campaign)"
    )

    val budgetOptions = listOf("₹१,४९९", "₹१,९९९", "₹२,९९९", "₹४,९९९", "₹९,९९९+")

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
                        text = "जाहिरात ऑर्डर व CRM Pipeline",
                        color = StudioWhite,
                        fontSize = 14.sp,
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
                                text = "जाहिरात ऑर्डर फॉर्म भरा:",
                                color = CharcoalBlack,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "🔥 Live CRM Sync",
                                color = Color(0xFF2E7D32),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
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

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("मोबाईल नंबर (WhatsApp Number)*", color = TextMuted) },
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

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            label = { Text("फर्म / दुकानाचे नाव (Business / Firm Name)", color = TextMuted) },
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

                        Spacer(modifier = Modifier.height(10.dp))

                        // Service Type Selector Chips
                        Text(
                            text = "सेवेचा प्रकार निवडा (Service Type):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalBlack
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            serviceOptions.chunked(2).forEach { rowServices ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    rowServices.forEach { service ->
                                        val isSelected = selectedService == service
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isSelected) PrimaryPurple else SurfaceVariantLight)
                                                .border(1.dp, if (isSelected) PrimaryPurple else CardBorderLight, RoundedCornerShape(8.dp))
                                                .clickable { selectedService = service }
                                                .padding(vertical = 6.dp, horizontal = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = service,
                                                color = if (isSelected) StudioWhite else CharcoalBlack,
                                                fontSize = 10.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Budget Selector Chips
                        Text(
                            text = "अंदाजे बजेट (Estimated Budget):",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalBlack
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            budgetOptions.forEach { budget ->
                                val isSelected = selectedBudget == budget
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) CrimsonRed else SurfaceVariantLight)
                                        .border(1.dp, if (isSelected) CrimsonRed else CardBorderLight, RoundedCornerShape(8.dp))
                                    .clickable { selectedBudget = budget }
                                    .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = budget,
                                        color = if (isSelected) StudioWhite else CharcoalBlack,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = deadline,
                            onValueChange = { deadline = it },
                            label = { Text("अपेक्षित मुदत (Target Delivery, e.g. २४ तास, ३ दिवस)", color = TextMuted) },
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

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = details,
                            onValueChange = { details = it },
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

                        Spacer(modifier = Modifier.height(16.dp))

                        // Submit Button - creates CRM Lead & opens WhatsApp
                        Button(
                            onClick = {
                                if (name.isBlank() || phone.isBlank()) {
                                    Toast.makeText(context, "कृपया नाव आणि मोबाईल नंबर भरा", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.submitAdOrder(
                                        clientName = name,
                                        clientPhone = phone,
                                        businessName = businessName,
                                        serviceType = selectedService,
                                        budget = selectedBudget,
                                        deadline = deadline,
                                        details = details,
                                        onOrderCreated = { generatedOrderId ->
                                            val whatsappMsg = """
                                                नमस्कार Ktimes Media, मी ॲपवरून नवीन जाहिरात ऑर्डर नोंदवली आहे:
                                                🆔 *लीड / ऑर्डर ID*: $generatedOrderId
                                                👤 *नाव*: $name
                                                🏢 *व्यवसाय / फर्म*: ${businessName.ifBlank { "N/A" }}
                                                📱 *मोबाईल*: $phone
                                                🎯 *सेवेचा प्रकार*: $selectedService
                                                💰 *अंदाजे बजेट*: $selectedBudget
                                                ⏳ *मुदत (Deadline)*: $deadline
                                                📝 *माहिती / स्क्रिप्ट*: ${details.ifBlank { "N/A" }}

                                                कृपया ऑर्डर तपासून CRM मध्ये पुढील प्रक्रिया सुरू करावी. धन्यवाद!
                                            """.trimIndent()

                                            IntentUtils.openWhatsAppDirectMessage(context, whatsappMsg)
                                            Toast.makeText(context, "ऑर्डर नोंदवली! CRM लीड तयार झाली ($generatedOrderId)", Toast.LENGTH_LONG).show()
                                            orderTab = "MY_ORDERS"
                                        }
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_whatsapp),
                                contentDescription = "WhatsApp",
                                tint = StudioWhite,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ऑर्डर नोंदवा व थेट WhatsApp सुरू करा", color = StudioWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { IntentUtils.makePhoneCall(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhoneInTalk,
                                contentDescription = "Call",
                                tint = ElectricYellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("थेट कॉल करा (+91 9422337471)", color = StudioWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
                items(liveOrders, key = { it.id }) { order ->
                    ClientProjectCard(
                        order = order,
                        onApproveDraft = { orderForApproval = order },
                        onRequestRevision = { orderForRevision = order }
                    )
                }
            }
        }
    }

    // Client Revision Request Dialog
    orderForRevision?.let { order ->
        var revisionNotesText by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { orderForRevision = null },
            title = {
                Text(
                    text = "बदल / सुधारणा सुचवा (Request Revision)",
                    color = VibrantOrange,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "ऑर्डर ID: ${order.id} (${order.serviceType})\nचालू आवृत्ती: v${order.revisionCount + 1}",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                    Text(
                        text = "व्हॉईस, संगीत, उच्चार किंवा व्हिज्युअल्समध्ये आपल्याला जे बदल हवे आहेत ते खाली लिहा:",
                        fontSize = 12.sp,
                        color = CharcoalBlack
                    )
                    OutlinedTextField(
                        value = revisionNotesText,
                        onValueChange = { revisionNotesText = it },
                        label = { Text("उदा. व्हॉईसमध्ये नाव अधिक स्पष्ट करावे, फोन नंबर हळू बोला...", color = TextMuted) },
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
                        if (revisionNotesText.isNotBlank()) {
                            viewModel.submitClientRevision(order, revisionNotesText) {
                                val msg = """
                                    नमस्कार Ktimes Media,
                                    मी ऑर्डर ID: ${order.id} (${order.serviceType}) च्या ड्राफ्टमध्ये खालील सुधारणा सुचवल्या आहेत:
                                    
                                    📝 सुधारणा:
                                    $revisionNotesText
                                    
                                    कृपया दुरुस्ती करून नवीन ड्राफ्ट पाठवावा.
                                """.trimIndent()
                                IntentUtils.openWhatsAppDirectMessage(context, msg)
                            }
                            orderForRevision = null
                        } else {
                            Toast.makeText(context, "कृपया सुधारणा तपशील लिहा", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange)
                ) {
                    Text("सुधारणा सबमिट करा", color = StudioWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { orderForRevision = null }) {
                    Text("रद्द करा", color = TextMuted)
                }
            },
            containerColor = CardBackgroundLight
        )
    }

    // Client Approval Confirmation Dialog
    orderForApproval?.let { order ->
        AlertDialog(
            onDismissRequest = { orderForApproval = null },
            title = {
                Text(
                    text = "कामास अंतिम मंजुरी द्यायची आहे का?",
                    color = Color(0xFF2E7D32),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "ऑर्डर ID: ${order.id} (${order.serviceType})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = CharcoalBlack
                    )
                    Text(
                        text = "तुम्ही हा ड्राफ्ट मंजूर केल्यानंतर Ktimes Media ची प्रॉडक्शन टीम अंतिम 4K Ultra HD / 320kbps WAV मास्टर फाईल्स तयार करून तुम्हाला डाउनलोडसाठी उपलब्ध करेल.",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitClientApproval(order) {
                            val msg = """
                                नमस्कार Ktimes Media,
                                मी ऑर्डर ID: ${order.id} (${order.serviceType}) चा ड्राफ्ट तपासून मंजूर (Approved) केला आहे! ✅
                                
                                कृपया अंतिम मास्टर पॅकेज (4K / WAV / Print File) तयार करून पाठवावे.
                            """.trimIndent()
                            IntentUtils.openWhatsAppDirectMessage(context, msg)
                        }
                        orderForApproval = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("होय, ड्राफ्ट मंजूर करा (Approve)", color = StudioWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { orderForApproval = null }) {
                    Text("रद्द करा", color = TextMuted)
                }
            },
            containerColor = CardBackgroundLight
        )
    }
}

@Composable
fun ClientProjectCard(
    order: AdOrder,
    onApproveDraft: () -> Unit,
    onRequestRevision: () -> Unit
) {
    val context = LocalContext.current

    val statusColor = when (order.status) {
        "Delivered" -> Color(0xFF2E7D32)
        "Approval" -> Color(0xFFF57F17)
        "Production" -> PrimaryPurple
        "Confirmed", "Quotation Sent" -> Color(0xFF0288D1)
        "In Review", "Requirement Received" -> VibrantOrange
        else -> CrimsonRed
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, CardBorderLight, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundLight)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(statusColor)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = order.status,
                                color = StudioWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceVariantLight)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PM: ${order.projectManager.ifBlank { "Ktimes Studio" }}",
                                fontSize = 10.sp,
                                color = PrimaryPurple,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = order.details.ifBlank { order.serviceType },
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = CharcoalBlack
                    )
                    Text(
                        text = "सेवा: ${order.serviceType} • ID: ${order.id}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                    if (order.deadline.isNotBlank()) {
                        Text(
                            text = "अपेक्षित मुदत: ${order.deadline}",
                            fontSize = 11.sp,
                            color = CharcoalBlack,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Text(
                    text = order.budget,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = CrimsonRed
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Project Milestone Stepper
            ProjectMilestoneStepper(currentStatus = order.status, progress = order.progress)

            // 1. DRAFT REVIEW & CLIENT APPROVAL / REVISION BOX
            if (order.draftUrl.isNotBlank() || order.status in listOf("Approval", "Revision")) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, PrimaryPurple.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F3FC))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = null,
                                    tint = PrimaryPurple,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "🎬 ड्राफ्ट आवृत्ती v${order.revisionCount + 1} (Draft Review)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryPurple
                                )
                            }

                            if (order.draftUrl.isNotBlank()) {
                                Button(
                                    onClick = { IntentUtils.openUrl(context, order.draftUrl) },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                                    contentPadding = PaddingValues(horizontal = 8.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("▶️ प्रिव्ह्यू उघडा", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                                }
                            }
                        }

                        if (order.draftNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "स्टुडिओ टीप: \"${order.draftNotes}\"",
                                fontSize = 11.sp,
                                color = CharcoalBlack,
                                lineHeight = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Client Approval Status and Buttons
                        when (order.clientApprovalStatus) {
                            "APPROVED" -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFE8F5E9))
                                        .padding(8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "✅ तुम्ही हा ड्राफ्ट मंजूर केला आहे! अंतिम फाईल्स तयार होत आहेत.",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2E7D32)
                                        )
                                    }
                                }
                            }
                            "REVISION_REQUESTED" -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFFF3E0))
                                        .padding(8.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = "🔄 दुरुस्ती विनंती नोंदवली गेली आहे (Revision Underway)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFE65100)
                                        )
                                        if (order.revisionNotes.isNotBlank()) {
                                            Text(
                                                text = "सुधारणा टीप: \"${order.revisionNotes}\"",
                                                fontSize = 11.sp,
                                                color = CharcoalBlack
                                            )
                                        }
                                    }
                                }
                            }
                            else -> {
                                // Action Buttons: Approve or Revision
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = onApproveDraft,
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 6.dp),
                                        modifier = Modifier
                                            .weight(1.2f)
                                            .height(36.dp)
                                    ) {
                                        Text("✅ काम मंजूर करा", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                                    }

                                    Button(
                                        onClick = onRequestRevision,
                                        colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 6.dp),
                                        modifier = Modifier
                                            .weight(1.1f)
                                            .height(36.dp)
                                    ) {
                                        Text("✏️ बदल सुचवा", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioWhite)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. FINAL DELIVERABLES & DOWNLOAD CARD
            if (order.finalFileUrl.isNotBlank() || order.status == "Delivered") {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.5.dp, Color(0xFF2E7D32), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎉 अंतिम मास्टर फाईल्स तयार आहेत!",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF1B5E20)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF2E7D32))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("100% Complete", color = StudioWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "🎬 4K UHD Master • 🔊 320kbps WAV • 📐 CMYK Print 300DPI • 📱 Vertical Reels",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryPurple
                        )

                        if (order.finalDeliveryNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = order.finalDeliveryNotes,
                                fontSize = 11.sp,
                                color = CharcoalBlack
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (order.finalFileUrl.isNotBlank()) {
                                    IntentUtils.openUrl(context, order.finalFileUrl)
                                } else {
                                    Toast.makeText(context, "फाईल लिंक लवकरच अपडेट होईल", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            Text("📥 अंतिम मास्टर फाईल्स डाउनलोड करा", color = StudioWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Direct WhatsApp Support & Enquiry Button
            Button(
                onClick = {
                    val updateMsg = "नमस्कार Ktimes Media, माझ्या प्रोजेक्ट ID: ${order.id} (${order.serviceType}) बद्दल माहिती हवी आहे."
                    IntentUtils.openWhatsAppDirectMessage(context, updateMsg)
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurpleLight),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
            ) {
                Text("WhatsApp वर स्टुडिओशी संपर्क", color = StudioWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ProjectMilestoneStepper(
    currentStatus: String,
    progress: Int
) {
    val steps = listOf(
        "नोंदणी" to 15,
        "प्रॉडक्शन" to 60,
        "ड्राफ्ट रिव्ह्यू" to 85,
        "अंतिम डिलिव्हरी" to 100
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("प्रकल्प प्रगती (Project Pipeline)", fontSize = 11.sp, color = TextMuted)
            Text("$progress%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
        }

        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = if (progress >= 100) Color(0xFF2E7D32) else PrimaryPurple,
            trackColor = SurfaceVariantLight
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEach { (label, minProg) ->
                val isCompleted = progress >= minProg
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) Color(0xFF2E7D32) else TextMuted.copy(alpha = 0.4f))
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = label,
                        fontSize = 9.sp,
                        fontWeight = if (isCompleted) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCompleted) CharcoalBlack else TextMuted
                    )
                }
            }
        }
    }
}


