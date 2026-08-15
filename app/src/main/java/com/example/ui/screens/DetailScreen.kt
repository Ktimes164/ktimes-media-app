package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MediaItem
import com.example.ui.components.AudioPlayerComponent
import com.example.ui.components.GraphicViewerComponent
import com.example.ui.components.VideoPlayerComponent
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
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.VibrantOrange
import com.example.ui.theme.WhatsAppGreen
import com.example.utils.IntentUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    item: MediaItem,
    viewModel: com.example.ui.MediaViewModel? = null,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CrimsonRed)
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = item.sampleId,
                            color = StudioWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
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
                    IconButton(onClick = { IntentUtils.shareItem(context, item) }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = ElectricYellow
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryPurple)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardBackgroundLight)
                    .border(1.dp, CardBorderLight)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel?.createLeadFromItem(item)
                            IntentUtils.openWhatsAppForOrder(context, item)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = VibrantOrange),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_whatsapp),
                            contentDescription = "WhatsApp Order",
                            tint = StudioWhite,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Order via WhatsApp", color = StudioWhite, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            viewModel?.createLeadFromItem(item, customNotes = "थेट कॉल चौकशी")
                            IntentUtils.makePhoneCall(context)
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple,
                            contentColor = StudioWhite
                        ),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Call",
                            tint = ElectricYellow,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call Studio", color = StudioWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = AppBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Category & Sample ID Pill Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PrimaryPurple)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = item.category,
                        color = ElectricYellow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceVariantLight)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = "Sample ID",
                            tint = PrimaryPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sample ID: ${item.sampleId}",
                            color = CharcoalBlack,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = item.title,
                color = CharcoalBlack,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // In-App Player / Viewer Section
            when (item.mediaType.uppercase()) {
                "AUDIO" -> {
                    AudioPlayerComponent(
                        audioUrl = item.mediaUrl,
                        title = item.title,
                        sampleId = item.sampleId
                    )
                }
                "VIDEO" -> {
                    VideoPlayerComponent(
                        videoUrl = item.mediaUrl,
                        thumbnailUrl = item.thumbnailUrl,
                        sampleId = item.sampleId
                    )
                }
                else -> {
                    GraphicViewerComponent(
                        imageUrl = item.thumbnailUrl.ifEmpty { item.mediaUrl },
                        title = item.title,
                        sampleId = item.sampleId
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Price & Service Details Card
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PriceCheck,
                                contentDescription = "Price",
                                tint = PrimaryPurple,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Estimated Rate",
                                color = TextMuted,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Text(
                            text = item.priceOrEstimate,
                            color = PrimaryPurple,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricYellow)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (item.tags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Tags & Features: ${item.tags}",
                            color = PrimaryPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Description & Details",
                        color = CharcoalBlack,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = item.description.ifEmpty { "High-quality professional production sample from Ktimes Media studio." },
                        color = TextMuted,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Studio Guarantee Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, PrimaryPurpleLight, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = PrimaryPurple)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Why Order from Ktimes Media?",
                        color = ElectricYellow,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    StudioGuaranteeItem("24 - 48 Hour Fast Turnaround Delivery")
                    StudioGuaranteeItem("Male & Female Professional Voiceover Artists")
                    StudioGuaranteeItem("Crystal Clear Broadcast Audio Mastering")
                    StudioGuaranteeItem("Full Commercial Broadcast & Social Rights")
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun StudioGuaranteeItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Check",
            tint = ElectricYellow,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = StudioWhite,
            fontSize = 13.sp
        )
    }
}

