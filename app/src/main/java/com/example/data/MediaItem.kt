package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_portfolio_items")
data class MediaItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sampleId: String,
    val title: String,
    val category: String, // "Audio Ads", "Video Ads", "Graphics & Design", "Election Special", "Album Songs", "Documentaries", "Promotion Shows & Events"
    val mediaType: String, // "AUDIO", "VIDEO", "GRAPHIC"
    val mediaUrl: String,
    val thumbnailUrl: String = "",
    val description: String = "",
    val priceOrEstimate: String = "Quote on Request",
    val tags: String = "",
    val isFeatured: Boolean = false,
    val whatsappMsg: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
