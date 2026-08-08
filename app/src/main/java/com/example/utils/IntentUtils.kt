package com.example.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.data.MediaItem

object IntentUtils {

    const val STUDIO_WHATSAPP_NUMBER = "919422337471" // Ktimes Media Hotline
    const val STUDIO_PHONE_NUMBER = "+919422337471"

    fun openWhatsAppForOrder(context: Context, item: MediaItem) {
        val message = if (item.whatsappMsg.isNotBlank()) {
            item.whatsappMsg
        } else {
            """
                Hello Ktimes Media! 👋
                I would like to order / inquire about:
                📌 *Title*: ${item.title}
                🆔 *Sample ID*: ${item.sampleId}
                📁 *Category*: ${item.category}
                💰 *Estimated Rate*: ${item.priceOrEstimate}
                
                Please share script guidelines, turnaround time, and voiceover options.
            """.trimIndent()
        }

        try {
            val encodedMsg = Uri.encode(message)
            val whatsappUri = Uri.parse("https://api.whatsapp.com/send?phone=$STUDIO_WHATSAPP_NUMBER&text=$encodedMsg")
            val intent = Intent(Intent.ACTION_VIEW, whatsappUri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not launch WhatsApp. Opening web link...", Toast.LENGTH_SHORT).show()
            val webUri = Uri.parse("https://wa.me/$STUDIO_WHATSAPP_NUMBER?text=${Uri.encode(message)}")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            context.startActivity(webIntent)
        }
    }

    fun openWhatsAppDirectMessage(
        context: Context,
        message: String = "नमस्कार Ktimes Media, मी ॲपवरून संपर्क साधत आहे. मला आपल्या सेवांबद्दल माहिती हवी आहे."
    ) {
        try {
            val encodedMsg = Uri.encode(message)
            val whatsappUri = Uri.parse("https://api.whatsapp.com/send?phone=$STUDIO_WHATSAPP_NUMBER&text=$encodedMsg")
            val intent = Intent(Intent.ACTION_VIEW, whatsappUri)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp उघडत आहे...", Toast.LENGTH_SHORT).show()
            val webUri = Uri.parse("https://wa.me/$STUDIO_WHATSAPP_NUMBER?text=${Uri.encode(message)}")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            context.startActivity(webIntent)
        }
    }
    fun makePhoneCall(context: Context, phoneNumber: String = STUDIO_PHONE_NUMBER) {
        try {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Unable to initiate call", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareItem(context: Context, item: MediaItem) {
        val shareText = """
            Check out this portfolio sample from Ktimes Media:
            ${item.title} (Sample ID: ${item.sampleId})
            Category: ${item.category}
            Sample Link: ${item.mediaUrl}
            
            Contact Ktimes Media for custom voiceovers, ads & video production!
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, item.title)
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Share via"))
    }
}
