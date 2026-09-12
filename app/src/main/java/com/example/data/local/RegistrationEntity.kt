package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registrations")
data class RegistrationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bibNumber: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val age: Int,
    val gender: String,
    val bloodGroup: String,
    val emergencyContactName: String,
    val emergencyPhone: String,
    val category: String, // name of MarathonCategory enum
    val tShirtSize: String,
    val wave: String,
    val corralGate: String,
    val medicalNotes: String = "",
    val registrationTimestamp: Long = System.currentTimeMillis(),
    val qrVerificationCode: String,
    val isCheckedIn: Boolean = false,
    val baggageTag: String
)
