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

    // Default mock orders for instant display & fallback
    val initialMockOrders = listOf(
        AdOrder(
            id = "KTM-ORD-101",
            clientUid = "client_1",
            clientName = "Ganesh Jewellers",
            clientPhone = "9422337471",
            businessName = "Ganesh Jewellers",
            serviceType = "Video Ads",
            budget = "₹4,999",
            details = "गणेशोत्सव विशेष ज्वेलरी डिस्काउंट व्हिडिओ जाहिरात (4K Cinematic)",
            location = "Satara",
            status = "Production",
            progress = 65,
            deadline = "१६ ऑगस्ट"
        ),
        AdOrder(
            id = "KTM-ORD-102",
            clientUid = "client_1",
            clientName = "Ganesh Jewellers",
            clientPhone = "9422337471",
            businessName = "Ganesh Jewellers",
            serviceType = "Audio Jingles",
            budget = "₹1,499",
            details = "रिक्षा प्रचार व सोशल मीडिया ऑडिओ जिगल (मराठी व्हॉईस)",
            location = "Satara",
            status = "Approval",
            progress = 90,
            deadline = "आज सायंकाळी"
        ),
        AdOrder(
            id = "KTM-ORD-103",
            clientUid = "client_2",
            clientName = "Shah Brothers",
            clientPhone = "9822001122",
            businessName = "Shah Brothers Saree Depot",
            serviceType = "Graphics & Banners",
            budget = "₹2,500",
            details = "सण-उत्सव ५ सोशल मीडिया पोस्टर्स (पैठणी व सिल्क साड्या)",
            location = "Phaltan",
            status = "Delivered",
            progress = 100,
            deadline = "पूर्ण झाले"
        ),
        AdOrder(
            id = "KTM-ORD-104",
            clientUid = "client_3",
            clientName = "Samarth Enterprise",
            clientPhone = "9850112233",
            businessName = "Samarth Enterprise Automobile",
            serviceType = "360 Campaign",
            budget = "₹11,999",
            details = "ऑटोमोबाईल शोरूम नवीन फेस्टिव्ह लॉन्च कॅम्पेन",
            location = "Baramati",
            status = "Quotation Sent",
            progress = 40,
            deadline = "२० ऑगस्ट"
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
