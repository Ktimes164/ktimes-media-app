package com.example.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow

class MediaRepository(private val dao: MediaItemDao) {

    val allItems: Flow<List<MediaItem>> = dao.getAllItems()

    suspend fun checkAndPrepopulate() {
        if (dao.getItemCount() == 0) {
            dao.insertAll(DefaultData.sampleItems)
        }
    }

    suspend fun insertItem(item: MediaItem) = dao.insertItem(item)

    suspend fun updateItem(item: MediaItem) = dao.updateItem(item)

    suspend fun deleteItem(item: MediaItem) = dao.deleteItem(item)

    suspend fun deleteItemById(id: Int) = dao.deleteItemById(id)

    suspend fun resetToDefault() {
        dao.deleteAll()
        dao.insertAll(DefaultData.sampleItems)
    }

    // JSON Export
    suspend fun exportToJson(items: List<MediaItem>): String {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, MediaItem::class.java)
        val adapter = moshi.adapter<List<MediaItem>>(type)
        return adapter.toJson(items)
    }

    // JSON Import
    suspend fun importFromJson(jsonString: String, replaceAll: Boolean = true): Boolean {
        return try {
            var itemsList: List<MediaItem>? = null
            try {
                val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                val type = Types.newParameterizedType(List::class.java, MediaItem::class.java)
                val adapter = moshi.adapter<List<MediaItem>>(type)
                itemsList = adapter.fromJson(jsonString)
            } catch (e: Exception) {
                // Ignore and try fallback manual JSON parser
            }

            if (itemsList.isNullOrEmpty()) {
                val parsed = mutableListOf<MediaItem>()
                val jsonArray = org.json.JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val rawId = obj.optString("id", "${i + 1}")
                    val sampleId = obj.optString("sample_id", obj.optString("sampleId", "KTM-SMP-$rawId"))
                    val title = obj.optString("title", "Untitled Sample")
                    val category = obj.optString("category", "General")
                    val mediaType = obj.optString("media_type", obj.optString("mediaType", "AUDIO")).uppercase()
                    val mediaUrl = obj.optString("url", obj.optString("mediaUrl", ""))
                    val whatsappMsg = obj.optString("whatsapp_msg", obj.optString("whatsappMsg", ""))
                    val description = obj.optString("description", "")
                    val priceOrEstimate = obj.optString("priceOrEstimate", obj.optString("price", "Quote on Request"))

                    parsed.add(
                        MediaItem(
                            id = 0,
                            sampleId = sampleId,
                            title = title,
                            category = category,
                            mediaType = mediaType,
                            mediaUrl = mediaUrl,
                            thumbnailUrl = obj.optString("thumbnailUrl", ""),
                            description = description,
                            priceOrEstimate = priceOrEstimate,
                            tags = obj.optString("tags", ""),
                            isFeatured = obj.optBoolean("isFeatured", true),
                            whatsappMsg = whatsappMsg
                        )
                    )
                }
                itemsList = parsed
            }

            if (!itemsList.isNullOrEmpty()) {
                if (replaceAll) {
                    dao.deleteAll()
                }
                val cleanItems = itemsList.map { it.copy(id = 0) }
                dao.insertAll(cleanItems)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
