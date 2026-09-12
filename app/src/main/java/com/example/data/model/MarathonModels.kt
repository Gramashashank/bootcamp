package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class MarathonCategory(
    val title: String,
    val distanceKm: Float,
    val distanceLabel: String,
    val startTime: String,
    val cutoffTime: String,
    val fee: String,
    val bibPrefix: String,
    val colorHex: Long,
    val description: String
) {
    FULL_MARATHON(
        title = "Full Marathon",
        distanceKm = 42.195f,
        distanceLabel = "42.195 KM",
        startTime = "05:00 AM",
        cutoffTime = "6h 00m",
        fee = "₹2,400",
        bibPrefix = "BLR-42",
        colorHex = 0xFFDC2626, // Crimson
        description = "AIMS Certified route through iconic Bengaluru landmarks: Cubbon Park, Vidhana Soudha, MG Road & Lalbagh."
    ),
    HALF_MARATHON(
        title = "Half Marathon",
        distanceKm = 21.0975f,
        distanceLabel = "21.1 KM",
        startTime = "05:45 AM",
        cutoffTime = "3h 30m",
        fee = "₹1,800",
        bibPrefix = "BLR-21",
        colorHex = 0xFF0284C7, // Athletic Cyan
        description = "Fast, scenic course through the historic heart of the Garden City with cheering zones and music."
    ),
    OPEN_10K(
        title = "Open 10K Run",
        distanceKm = 10.0f,
        distanceLabel = "10.0 KM",
        startTime = "06:30 AM",
        cutoffTime = "1h 45m",
        fee = "₹1,200",
        bibPrefix = "BLR-10",
        colorHex = 0xFFD97706, // Amber
        description = "The premier 10K road race through Bengaluru's lush green canopy and central business district."
    ),
    HOPE_RUN_5K(
        title = "Majestic 5K Run",
        distanceKm = 5.0f,
        distanceLabel = "5.0 KM",
        startTime = "07:15 AM",
        cutoffTime = "1h 00m",
        fee = "₹800",
        bibPrefix = "BLR-05",
        colorHex = 0xFF16A34A, // Green
        description = "Celebratory non-timed fitness & charity run for families, beginners, and corporate groups."
    );

    val badgeColor: Color get() = Color(colorHex)
}

data class CourseCheckpoint(
    val id: Int,
    val name: String,
    val landmark: String,
    val distanceKm: Float,
    val hasWater: Boolean = true,
    val hasMedical: Boolean = false,
    val hasEnergyGel: Boolean = false,
    val hasTimingMat: Boolean = true
)
