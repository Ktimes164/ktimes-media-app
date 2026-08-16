package com.example.data.models

data class Testimonial(
    val id: String,
    val clientName: String,
    val businessName: String,
    val businessType: String,
    val rating: Int = 5,
    val feedback: String,
    val serviceUsed: String,
    val cityOrRegion: String,
    val avatarInitials: String,
    val verifiedMember: Boolean = true,
    val dateText: String = "ऑगस्ट २०२६"
)
