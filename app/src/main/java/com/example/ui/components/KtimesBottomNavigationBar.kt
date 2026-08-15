package com.example.ui.components

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.ElectricYellow
import com.example.ui.theme.PrimaryPurple
import com.example.ui.theme.StudioWhite
import com.example.ui.theme.VibrantOrange
import com.example.ui.theme.WhatsAppGreen
import com.example.utils.IntentUtils

@Composable
fun KtimesBottomNavigationBar(
    currentTab: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(PrimaryPurple)
            .border(width = 1.dp, color = PrimaryPurple, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Home - 'होम'
            NavTabItem(
                label = "होम",
                icon = Icons.Default.Home,
                isSelected = currentTab == "home",
                onClick = { onTabSelected("home") }
            )

            // 2. Portfolio - 'पोर्टफोलिओ'
            NavTabItem(
                label = "पोर्टफोलिओ",
                icon = Icons.Default.Folder,
                isSelected = currentTab == "portfolio",
                onClick = { onTabSelected("portfolio") }
            )

            // 3. Center CTA - Ktimes Media Logo + WhatsApp Direct Connect
            Box(
                modifier = Modifier
                    .offset(y = (-12).dp)
                    .size(56.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(WhatsAppGreen)
                    .border(2.dp, ElectricYellow, CircleShape)
                    .clickable {
                        IntentUtils.openWhatsAppDirectMessage(context)
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(CrimsonRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "K",
                            color = StudioWhite,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_whatsapp),
                        contentDescription = "WhatsApp Direct Connect",
                        tint = StudioWhite,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // 4. Marketplace - 'मार्केटप्लेस'
            NavTabItem(
                label = "मार्केटप्लेस",
                icon = Icons.Default.Storefront,
                isSelected = currentTab == "marketplace" || currentTab == "rates",
                onClick = { onTabSelected("marketplace") }
            )

            // 5. Orders - 'ऑर्डर'
            NavTabItem(
                label = "ऑर्डर",
                icon = Icons.Default.Assignment,
                isSelected = currentTab == "orders",
                onClick = { onTabSelected("orders") }
            )
        }
    }
}

@Composable
private fun NavTabItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) ElectricYellow else StudioWhite.copy(alpha = 0.8f),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = if (isSelected) ElectricYellow else StudioWhite.copy(alpha = 0.8f),
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
