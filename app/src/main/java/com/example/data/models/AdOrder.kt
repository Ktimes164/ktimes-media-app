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
    val status: String = "New Lead", // "New Lead", "In Review", "Confirmed", "Production", "Approval", "Revision", "Delivered"
    val progress: Int = 15,
    val draftUrl: String = "",
    val draftNotes: String = "",
    val draftUpdatedAt: Long = 0L,
    val finalFileUrl: String = "",
    val finalDeliveryNotes: String = "",
    val deliveredAt: Long = 0L,
    val revisionNotes: String = "",
    val revisionCount: Int = 0,
    val clientApprovalStatus: String = "PENDING", // "PENDING", "APPROVED", "REVISION_REQUESTED"
    val clientApprovedAt: Long = 0L,
    val timestamp: Long = System.currentTimeMillis(),
    val deadline: String = "३ दिवस",
    val projectManager: String = "Ktimes Creative Studio"
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
            "draftNotes" to draftNotes,
            "draftUpdatedAt" to draftUpdatedAt,
            "finalFileUrl" to finalFileUrl,
            "finalDeliveryNotes" to finalDeliveryNotes,
            "deliveredAt" to deliveredAt,
            "revisionNotes" to revisionNotes,
            "revisionCount" to revisionCount,
            "clientApprovalStatus" to clientApprovalStatus,
            "clientApprovedAt" to clientApprovedAt,
            "timestamp" to timestamp,
            "deadline" to deadline,
            "projectManager" to projectManager
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
                draftNotes = map["draftNotes"] as? String ?: "",
                draftUpdatedAt = (map["draftUpdatedAt"] as? Number)?.toLong() ?: 0L,
                finalFileUrl = map["finalFileUrl"] as? String ?: "",
                finalDeliveryNotes = map["finalDeliveryNotes"] as? String ?: "",
                deliveredAt = (map["deliveredAt"] as? Number)?.toLong() ?: 0L,
                revisionNotes = map["revisionNotes"] as? String ?: "",
                revisionCount = (map["revisionCount"] as? Number)?.toInt() ?: 0,
                clientApprovalStatus = map["clientApprovalStatus"] as? String ?: "PENDING",
                clientApprovedAt = (map["clientApprovedAt"] as? Number)?.toLong() ?: 0L,
                timestamp = (map["timestamp"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                deadline = map["deadline"] as? String ?: "३ दिवस",
                projectManager = map["projectManager"] as? String ?: "Ktimes Creative Studio"
            )
        }
    }
}
