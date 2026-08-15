package com.example.data.models

data class AdOrder(
    val id: String = "",
    val clientUid: String = "",
    val clientName: String = "",
    val clientPhone: String = "",
    val businessName: String = "",
    val serviceType: String = "", // "Video Ads", "Audio Jingles", "Graphics & Banners", "AI Ads", "360 Campaign"
    val budget: String = "",
    val details: String = "",
    val location: String = "Satara",
    val status: String = "New Lead", // "New Lead", "Requirement Received", "Quotation Sent", "Production", "Approval", "Delivered"
    val progress: Int = 15,
    val draftUrl: String = "",
    val finalFileUrl: String = "",
    val revisionNotes: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val deadline: String = "३ दिवस"
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "clientUid" to clientUid,
            "clientName" to clientName,
            "clientPhone" to clientPhone,
            "businessName" to businessName,
            "serviceType" to serviceType,
            "budget" to budget,
            "details" to details,
            "location" to location,
            "status" to status,
            "progress" to progress,
            "draftUrl" to draftUrl,
            "finalFileUrl" to finalFileUrl,
            "revisionNotes" to revisionNotes,
            "timestamp" to timestamp,
            "deadline" to deadline
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): AdOrder {
            return AdOrder(
                id = map["id"] as? String ?: "",
                clientUid = map["clientUid"] as? String ?: "",
                clientName = map["clientName"] as? String ?: "",
                clientPhone = map["clientPhone"] as? String ?: "",
                businessName = map["businessName"] as? String ?: "",
                serviceType = map["serviceType"] as? String ?: "",
                budget = map["budget"] as? String ?: "",
                details = map["details"] as? String ?: "",
                location = map["location"] as? String ?: "Satara",
                status = map["status"] as? String ?: "New Lead",
                progress = (map["progress"] as? Number)?.toInt() ?: 15,
                draftUrl = map["draftUrl"] as? String ?: "",
                finalFileUrl = map["finalFileUrl"] as? String ?: "",
                revisionNotes = map["revisionNotes"] as? String ?: "",
                timestamp = (map["timestamp"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                deadline = map["deadline"] as? String ?: "३ दिवस"
            )
        }
    }
}
