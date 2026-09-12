package com.example.tracking

import androidx.compose.ui.graphics.Color
import com.example.data.model.CourseCheckpoint

data class CheckpointSplit(
    val checkpointName: String,
    val distanceKm: Float,
    val splitTimeFormatted: String,
    val paceFormatted: String,
    val passedTimestamp: Long
)

enum class TrackingStatus {
    NOT_STARTED,
    RUNNING,
    PAUSED,
    FINISHED
}

data class LiveRunnerState(
    val bibNumber: String,
    val runnerName: String,
    val categoryName: String,
    val totalDistanceKm: Float,
    val currentDistanceKm: Float,
    val currentPaceMinSec: String, // e.g. "5:12 /km"
    val avgPaceMinSec: String,
    val elapsedSeconds: Long,
    val projectedFinishSeconds: Long,
    val heartRateBpm: Int,
    val cadenceSpm: Int,
    val currentCheckpoint: CourseCheckpoint,
    val nextCheckpoint: CourseCheckpoint?,
    val splits: List<CheckpointSplit>,
    val status: TrackingStatus,
    val lastLocationName: String,
    val batteryPct: Int = 89
)

object BengaluruCourseData {
    val FULL_COURSE_CHECKPOINTS = listOf(
        CourseCheckpoint(0, "Kanteerava Stadium", "Start Line & Timing Mat", 0.0f, hasWater = true, hasMedical = true, hasTimingMat = true),
        CourseCheckpoint(1, "Cubbon Park Avenue", "Kingfisher Pavilion (5K)", 5.0f, hasWater = true, hasEnergyGel = false, hasTimingMat = true),
        CourseCheckpoint(2, "Vidhana Soudha", "Legislative Assembly Facade (10K)", 10.0f, hasWater = true, hasEnergyGel = true, hasTimingMat = true),
        CourseCheckpoint(3, "MG Road Boulevard", "Brigade Rd Junction (15K)", 15.0f, hasWater = true, hasMedical = true, hasTimingMat = true),
        CourseCheckpoint(4, "Ulsoor Lake", "Halfway Split & Timing Mat (21.1K)", 21.0975f, hasWater = true, hasEnergyGel = true, hasTimingMat = true),
        CourseCheckpoint(5, "Domlur Flyover", "Old Airport Rd Junction (27K)", 27.0f, hasWater = true, hasEnergyGel = false, hasTimingMat = true),
        CourseCheckpoint(6, "Lalbagh Botanical Garden", "Glass House West Gate (32K)", 32.0f, hasWater = true, hasMedical = true, hasEnergyGel = true, hasTimingMat = true),
        CourseCheckpoint(7, "Richmond Circle", "Residency Rd Overpass (37K)", 37.0f, hasWater = true, hasEnergyGel = false, hasTimingMat = true),
        CourseCheckpoint(8, "Hudson Circle", "City Corporation Square (40K)", 40.0f, hasWater = true, hasMedical = false, hasTimingMat = true),
        CourseCheckpoint(9, "Kanteerava Stadium Track", "Grand Finish Line & Podium", 42.195f, hasWater = true, hasMedical = true, hasTimingMat = true)
    )

    val HALF_COURSE_CHECKPOINTS = listOf(
        CourseCheckpoint(0, "Kanteerava Stadium", "Start Line", 0.0f, hasWater = true, hasMedical = true),
        CourseCheckpoint(1, "Cubbon Park", "Lush Canopies (5K)", 5.0f, hasWater = true),
        CourseCheckpoint(2, "Vidhana Soudha", "Historic Gate (10K)", 10.0f, hasWater = true, hasEnergyGel = true),
        CourseCheckpoint(3, "MG Road", "Trinity Circle (15K)", 15.0f, hasWater = true, hasMedical = true),
        CourseCheckpoint(4, "Cubbon Road Turnaround", "Near Bowring Club (18K)", 18.0f, hasWater = true),
        CourseCheckpoint(5, "Kanteerava Stadium Track", "Grand Finish Line (21.1K)", 21.0975f, hasWater = true, hasMedical = true)
    )

    val TEN_K_CHECKPOINTS = listOf(
        CourseCheckpoint(0, "Kanteerava Stadium", "Start Line", 0.0f, hasWater = true),
        CourseCheckpoint(1, "Cubbon Park Loop", "Queen's Park Gate (3K)", 3.0f, hasWater = true),
        CourseCheckpoint(2, "High Court / Vidhana", "Grand Plaza (6K)", 6.0f, hasWater = true, hasEnergyGel = true),
        CourseCheckpoint(3, "Kasturba Road", "Museum Junction (8K)", 8.0f, hasWater = true),
        CourseCheckpoint(4, "Kanteerava Stadium Track", "Grand Finish (10K)", 10.0f, hasWater = true, hasMedical = true)
    )

    val FIVE_K_CHECKPOINTS = listOf(
        CourseCheckpoint(0, "Kanteerava Stadium", "Start Line", 0.0f, hasWater = true),
        CourseCheckpoint(1, "Cubbon Park Century Trees", "Children's Park (2.5K)", 2.5f, hasWater = true),
        CourseCheckpoint(2, "Kanteerava Stadium Track", "Grand Finish (5K)", 5.0f, hasWater = true, hasMedical = true)
    )

    fun getCheckpointsForDistance(distanceKm: Float): List<CourseCheckpoint> {
        return when {
            distanceKm > 30f -> FULL_COURSE_CHECKPOINTS
            distanceKm > 15f -> HALF_COURSE_CHECKPOINTS
            distanceKm > 7f -> TEN_K_CHECKPOINTS
            else -> FIVE_K_CHECKPOINTS
        }
    }
}
