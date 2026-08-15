package com.example.data.firebase

import android.util.Log
import com.example.data.DefaultData
import com.example.data.MediaItem
import com.example.data.models.AdOrder
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreService {

    private val firestore: FirebaseFirestore by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.e("FirestoreService", "Firestore init error", e)
            throw e
        }
    }

    companion object {
        const val COLLECTION_ORDERS = "ad_orders"
        const val COLLECTION_PORTFOLIO = "portfolio_items"
        const val COLLECTION_USERS = "users"
    }

    // Default mock orders with rich Project Management, Draft, Revision, Approval & Delivery data
    val initialMockOrders = listOf(
        AdOrder(
            id = "KTM-ORD-101",
            clientUid = "client_1",
            clientName = "Ganesh Jewellers",
            clientPhone = "9422337471",
            businessName = "Ganesh Jewellers Satara",
            serviceType = "Video Ads",
            budget = "₹4,999",
            details = "गणेशोत्सव विशेष ज्वेलरी डिस्काउंट व्हिडिओ जाहिरात (4K Cinematic Video)",
            location = "Satara",
            status = "Production",
            progress = 65,
            deadline = "१६ ऑगस्ट",
            draftNotes = "व्हिडिओ एडिटिंग व व्हॉईस मिक्सिंग सुरू आहे. लवकरच ड्राफ्ट शेअर केला जाईल.",
            projectManager = "Ktimes Video Production Team"
        ),
        AdOrder(
            id = "KTM-ORD-102",
            clientUid = "client_1",
            clientName = "Ganesh Jewellers",
            clientPhone = "9422337471",
            businessName = "Ganesh Jewellers Satara",
            serviceType = "Audio Jingles",
            budget = "₹1,499",
            details = "रिक्षा प्रचार व सोशल मीडिया ऑडिओ जिगल (मराठी व्हॉईस & ढोल-ताशा बीट)",
            location = "Satara",
            status = "Approval",
            progress = 90,
            draftUrl = "https://raw.githubusercontent.com/google/google-api-javascript-client/master/samples/analytics/hello_analytics_api_v3.html",
            draftNotes = "व्हर्जन १.०: मराठी व्हॉईसओव्हर + बॅकग्राउंड संगीत पूर्ण झाले आहे. कृपया ऑडिओ प्रिव्ह्यू तपासून मंजुरी द्या.",
            draftUpdatedAt = System.currentTimeMillis() - 3600000L * 5,
            clientApprovalStatus = "PENDING",
            deadline = "आज सायंकाळी",
            projectManager = "Ktimes Audio Studio"
        ),
        AdOrder(
            id = "KTM-ORD-103",
            clientUid = "client_2",
            clientName = "Shah Brothers",
            clientPhone = "9822001122",
            businessName = "Shah Brothers Saree Depot",
            serviceType = "Graphics & Banners",
            budget = "₹2,500",
            details = "सण-उत्सव ५ सोशल मीडिया पोस्टर्स (पैठणी व सिल्क साड्या 3D डिझाइन)",
            location = "Phaltan",
            status = "Delivered",
            progress = 100,
            draftUrl = "https://drive.google.com",
            draftNotes = "पोस्टर्स ड्राफ्ट v1.2 मंजूर केले.",
            finalFileUrl = "https://drive.google.com/drive/folders/ktimes-media-final-deliverables",
            finalDeliveryNotes = "सर्व ५ पोस्टर्स हाय-रिझोल्यूशन (Print CMYK 300DPI + Instagram 1080x1350 + Story 1080x1920) गुगल ड्राईव्हवर उपलब्ध आहेत. धन्यवाद!",
            deliveredAt = System.currentTimeMillis() - 3600000L * 24,
            clientApprovalStatus = "APPROVED",
            clientApprovedAt = System.currentTimeMillis() - 3600000L * 28,
            deadline = "पूर्ण झाले",
            projectManager = "Ktimes Graphics Desk"
        ),
        AdOrder(
            id = "KTM-ORD-104",
            clientUid = "client_3",
            clientName = "Samarth Enterprise",
            clientPhone = "9850112233",
            businessName = "Samarth Enterprise Automobile",
            serviceType = "AI Video Ads",
            budget = "₹6,999",
            details = "ऑटोमोबाईल शोरूम नवीन फेस्टिव्ह लॉन्च कॅम्पेन (AI News Anchor + 3D Logo)",
            location = "Baramati",
            status = "Revision",
            progress = 80,
            draftUrl = "https://youtube.com",
            draftNotes = "ड्राफ्ट v1.0 AI अँकर व्हिडिओ तयार केला होता.",
            revisionNotes = "कृपया अँकरच्या मागे शोरूमचा खरा 4K बॅकग्राउंड व्हिडिओ टाका आणि शेवटी टेस्ट ड्राईव्ह बुकिंगचा QR कोड ॲड करा.",
            revisionCount = 1,
            clientApprovalStatus = "REVISION_REQUESTED",
            deadline = "२० ऑगस्ट",
            projectManager = "Ktimes AI Studio"
        )
    )

    fun observeOrders(): Flow<List<AdOrder>> = callbackFlow {
        try {
            val listener = firestore.collection(COLLECTION_ORDERS)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("FirestoreService", "Listen failed on orders, using fallback: ${error.message}")
                        trySend(initialMockOrders)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val orders = snapshot.documents.mapNotNull { doc ->
                            try {
                                val data = doc.data ?: return@mapNotNull null
                                AdOrder.fromMap(data)
                            } catch (e: Exception) {
                                null
                            }
                        }
                        trySend(orders)
                    } else {
                        trySend(initialMockOrders)
                    }
                }

            awaitClose { listener.remove() }
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error establishing orders listener: ${e.message}")
            trySend(initialMockOrders)
            awaitClose { }
        }
    }

    suspend fun createOrder(order: AdOrder): Result<Boolean> {
        return try {
            firestore.collection(COLLECTION_ORDERS)
                .document(order.id)
                .set(order.toMap())
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Failed to create order: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun dispatchDraft(
        orderId: String,
        draftUrl: String,
        draftNotes: String
    ): Result<Boolean> {
        return try {
            val updates = mapOf(
                "draftUrl" to draftUrl,
                "draftNotes" to draftNotes,
                "draftUpdatedAt" to System.currentTimeMillis(),
                "status" to "Approval",
                "progress" to 90,
                "clientApprovalStatus" to "PENDING"
            )
            firestore.collection(COLLECTION_ORDERS)
                .document(orderId)
                .update(updates)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Failed to dispatch draft: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun dispatchFinalDelivery(
        orderId: String,
        finalFileUrl: String,
        finalDeliveryNotes: String
    ): Result<Boolean> {
        return try {
            val updates = mapOf(
                "finalFileUrl" to finalFileUrl,
                "finalDeliveryNotes" to finalDeliveryNotes,
                "deliveredAt" to System.currentTimeMillis(),
                "status" to "Delivered",
                "progress" to 100,
                "clientApprovalStatus" to "APPROVED"
            )
            firestore.collection(COLLECTION_ORDERS)
                .document(orderId)
                .update(updates)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Failed to dispatch final delivery: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun submitClientApproval(orderId: String): Result<Boolean> {
        return try {
            val updates = mapOf(
                "clientApprovalStatus" to "APPROVED",
                "clientApprovedAt" to System.currentTimeMillis(),
                "status" to "Approval",
                "progress" to 95
            )
            firestore.collection(COLLECTION_ORDERS)
                .document(orderId)
                .update(updates)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Failed to submit client approval: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun submitClientRevision(
        orderId: String,
        currentRevisionCount: Int,
        revisionNotes: String
    ): Result<Boolean> {
        return try {
            val updates = mapOf(
                "revisionNotes" to revisionNotes,
                "revisionCount" to (currentRevisionCount + 1),
                "clientApprovalStatus" to "REVISION_REQUESTED",
                "status" to "Revision",
                "progress" to 80
            )
            firestore.collection(COLLECTION_ORDERS)
                .document(orderId)
                .update(updates)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Failed to submit client revision: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateOrderStatus(
        orderId: String,
        newStatus: String,
        progress: Int? = null,
        revisionNotes: String? = null
    ): Result<Boolean> {
        return try {
            val updates = mutableMapOf<String, Any>("status" to newStatus)
            progress?.let { updates["progress"] = it }
            revisionNotes?.let { updates["revisionNotes"] = it }

            firestore.collection(COLLECTION_ORDERS)
                .document(orderId)
                .update(updates)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Failed to update order status: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateFullOrder(order: AdOrder): Result<Boolean> {
        return try {
            firestore.collection(COLLECTION_ORDERS)
                .document(order.id)
                .set(order.toMap())
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Failed to update full order: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun deleteOrder(orderId: String): Result<Boolean> {
        return try {
            firestore.collection(COLLECTION_ORDERS)
                .document(orderId)
                .delete()
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Failed to delete order: ${e.message}")
            Result.failure(e)
        }
    }

    fun observePortfolioItems(): Flow<List<MediaItem>> = callbackFlow {
        try {
            val listener = firestore.collection(COLLECTION_PORTFOLIO)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.w("FirestoreService", "Listen failed on portfolio: ${error.message}")
                        trySend(DefaultData.sampleItems)
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val items = snapshot.documents.mapNotNull { doc ->
                            try {
                                val data = doc.data ?: return@mapNotNull null
                                MediaItem(
                                    id = (data["id"] as? Number)?.toInt() ?: 0,
                                    sampleId = data["sampleId"] as? String ?: doc.id,
                                    title = data["title"] as? String ?: "Sample",
                                    category = data["category"] as? String ?: "General",
                                    mediaType = (data["mediaType"] as? String ?: "AUDIO").uppercase(),
                                    mediaUrl = data["mediaUrl"] as? String ?: "",
                                    thumbnailUrl = data["thumbnailUrl"] as? String ?: "",
                                    description = data["description"] as? String ?: "",
                                    priceOrEstimate = data["priceOrEstimate"] as? String ?: "Quote on Request",
                                    tags = data["tags"] as? String ?: "",
                                    isFeatured = data["isFeatured"] as? Boolean ?: false,
                                    whatsappMsg = data["whatsappMsg"] as? String ?: "",
                                    timestamp = (data["timestamp"] as? Number)?.toLong() ?: System.currentTimeMillis()
                                )
                            } catch (e: Exception) {
                                null
                            }
                        }
                        trySend(items)
                    } else {
                        trySend(DefaultData.sampleItems)
                    }
                }

            awaitClose { listener.remove() }
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error setting up portfolio listener", e)
            trySend(DefaultData.sampleItems)
            awaitClose { }
        }
    }

    suspend fun savePortfolioItem(item: MediaItem): Result<Boolean> {
        return try {
            val map = mapOf(
                "sampleId" to item.sampleId,
                "title" to item.title,
                "category" to item.category,
                "mediaType" to item.mediaType,
                "mediaUrl" to item.mediaUrl,
                "thumbnailUrl" to item.thumbnailUrl,
                "description" to item.description,
                "priceOrEstimate" to item.priceOrEstimate,
                "tags" to item.tags,
                "isFeatured" to item.isFeatured,
                "whatsappMsg" to item.whatsappMsg,
                "timestamp" to item.timestamp
            )

            firestore.collection(COLLECTION_PORTFOLIO)
                .document(item.sampleId)
                .set(map)
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error saving portfolio item: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun deletePortfolioItem(sampleId: String): Result<Boolean> {
        return try {
            firestore.collection(COLLECTION_PORTFOLIO)
                .document(sampleId)
                .delete()
                .await()
            Result.success(true)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error deleting portfolio item: ${e.message}")
            Result.failure(e)
        }
    }
}
