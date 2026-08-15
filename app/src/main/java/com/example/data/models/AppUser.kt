package com.example.data.models

enum class UserRole {
    CLIENT,
    ADMIN;

    companion object {
        fun fromString(value: String?): UserRole {
            return when (value?.uppercase()) {
                "ADMIN" -> ADMIN
                else -> CLIENT
            }
        }
    }
}

data class AppUser(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val role: UserRole = UserRole.CLIENT,
    val phoneNumber: String = "",
    val businessName: String = "Ganesh Jewellers",
    val category: String = "Jewellers",
    val location: String = "Satara",
    val rewardsPoints: Int = 450,
    val createdAt: Long = System.currentTimeMillis()
)
