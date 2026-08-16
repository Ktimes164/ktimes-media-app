package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.Testimonial
import com.example.ui.theme.CardBackgroundLight
import com.example.ui.theme.CardBorderLight
import com.example.ui.theme.CharcoalBlack
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.PrimaryPurpleLight
import com.example.ui.theme.StudioWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VibrantOrange
import com.example.utils.IntentUtils
import kotlinx.coroutines.delay

@Composable
fun ClientTestimonialsSection(
    testimonials: List<Testimonial>,
    modifier: Modifier = Modifier,
    autoCycleIntervalMs: Long = 4500L
) {
    if (testimonials.isEmpty()) return

    val context = LocalContext.current
    var currentIndex by remember { mutableIntStateOf(0) }
    var isAutoPlaying by remember { mutableStateOf(true) }

    // Automatic cycling effect
    LaunchedEffect(isAutoPlaying, currentIndex, testimonials.size) {
        if (isAutoPlaying && testimonials.size > 1) {
            delay(autoCycleIntervalMs)
            currentIndex = (currentIndex + 1) % testimonials.size
        }
    }

    val currentTestimonial = testimonials[currentIndex.coerceIn(0, testimonials.size - 1)]

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("client_testimonials_section")
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CrimsonRed)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ग्राहकांचे अभिप्राय (Client Testimonials)",
                        color = StudioWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Auto-cycle indicator badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryPurple.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(if (isAutoPlaying) Color(0xFF2E7D32) else TextMuted)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${currentIndex + 1} / ${testimonials.size} क्लब सदस्य",
                        color = PrimaryPurple,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Testimonial Card with Animated Content Transition
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.5.dp, PrimaryPurple.copy(alpha = 0.35f), RoundedCornerShape(18.dp)),
            colors = CardDefaults.cardColors(containerColor = CardBackgroundLight),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                StudioWhite,
                                PrimaryPurpleLight.copy(alpha = 0.08f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                AnimatedContent(
                    targetState = currentTestimonial,
                    transitionSpec = {
                        slideInHorizontally { width -> width / 3 } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width / 3 } + fadeOut()
                    },
                    label = "TestimonialTransition"
                ) { item ->
                    Column {
                        // Top Meta Row: Business Club Tag & 5-Star Rating
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Verified Business Badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color(0xFFE8F5E9))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified Member",
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Verified Business Member",
                                    color = Color(0xFF1B5E20),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Star Rating Bar
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(item.rating) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Star",
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quote Text & Decorative Icon
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = "Quote",
                                tint = PrimaryPurple.copy(alpha = 0.5f),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "“${item.feedback}”",
                                color = CharcoalBlack,
                                fontSize = 13.5.sp,
                                lineHeight = 20.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Client Profile Box
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(StudioWhite)
                                .border(1.dp, CardBorderLight, RoundedCornerShape(12.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar with Marathi initial
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(PrimaryPurple, CrimsonRed)
                                        )
                                    )
                                    .border(1.5.dp, ElectricYellow, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.avatarInitials,
                                    color = StudioWhite,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.clientName,
                                    color = CharcoalBlack,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = item.businessName,
                                    color = PrimaryPurple,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = item.businessType,
                                    color = TextMuted,
                                    fontSize = 10.5.sp
                                )
                            }

                            // Service tag badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(PrimaryPurple.copy(alpha = 0.1f))
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = item.serviceUsed,
                                    color = PrimaryPurple,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Navigation Controls & Dot Indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Button
                    IconButton(
                        onClick = {
                            currentIndex = if (currentIndex - 1 < 0) testimonials.size - 1 else currentIndex - 1
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Previous Testimonial",
                            tint = PrimaryPurple,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Cycle Dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        testimonials.indices.forEach { index ->
                            val isSelected = index == currentIndex
                            Box(
                                modifier = Modifier
                                    .height(6.dp)
                                    .width(if (isSelected) 18.dp else 6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(if (isSelected) PrimaryPurple else TextMuted.copy(alpha = 0.35f))
                                    .clickable {
                                        currentIndex = index
                                    }
                            )
                        }
                    }

                    // Play/Pause Auto Cycle & Next Button
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { isAutoPlaying = !isAutoPlaying },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (isAutoPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isAutoPlaying) "Pause auto-cycle" else "Start auto-cycle",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                currentIndex = (currentIndex + 1) % testimonials.size
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForwardIos,
                                contentDescription = "Next Testimonial",
                                tint = PrimaryPurple,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Share Review CTA
                OutlinedButton(
                    onClick = {
                        val reviewMessage = """
                            नमस्कार Ktimes Media, मला आमच्या व्यवसायाचा अभिप्राय (Review / Feedback) शेअर करायचा आहे:
                            🏢 व्यवसाय नाव: 
                            ⭐ रेटिंग: ⭐⭐⭐⭐⭐
                            📝 अभिप्राय: 
                        """.trimIndent()
                        IntentUtils.openWhatsAppDirectMessage(context, reviewMessage)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryPurple),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_whatsapp),
                        contentDescription = "WhatsApp",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "आपला व्यावसायिक अनुभव / रिव्ह्यू शेअर करा",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryPurple
                    )
                }
            }
        }
    }
}
