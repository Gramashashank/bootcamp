package com.example.data.repository

import com.example.data.local.RegistrationDao
import com.example.data.local.RegistrationEntity
import com.example.data.model.MarathonCategory
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import kotlin.random.Random

class MarathonRepository(private val registrationDao: RegistrationDao) {

    val allRegistrations: Flow<List<RegistrationEntity>> = registrationDao.getAllRegistrations()
    val latestRegistration: Flow<RegistrationEntity?> = registrationDao.getLatestRegistration()

    fun getRegistrationById(id: Long): Flow<RegistrationEntity?> =
        registrationDao.getRegistrationById(id)

    fun getRegistrationByBib(bib: String): Flow<RegistrationEntity?> =
        registrationDao.getRegistrationByBib(bib)

    suspend fun seedInitialDataIfEmpty() {
        if (registrationDao.getRegistrationCount() == 0) {
            val samples = listOf(
                RegistrationEntity(
                    bibNumber = "BLR-42-1082",
                    fullName = "Arjun K. Rao",
                    email = "arjun.rao@bengaluru.in",
                    phone = "+91 98450 11223",
                    age = 32,
                    gender = "Male",
                    bloodGroup = "O+",
                    emergencyContactName = "Kavita Rao (Spouse)",
                    emergencyPhone = "+91 98450 99887",
                    category = MarathonCategory.FULL_MARATHON.name,
                    tShirtSize = "M",
                    wave = "Wave A (Sub 3:45)",
                    corralGate = "Gate 1 - North Stand, Kanteerava",
                    medicalNotes = "Asthma inhaler in pocket",
                    qrVerificationCode = "BCM26-42-88F9A1",
                    isCheckedIn = true,
                    baggageTag = "BAG-1082"
                ),
                RegistrationEntity(
                    bibNumber = "BLR-21-3045",
                    fullName = "Ananya Sundaram",
                    email = "ananya.s@outlook.com",
                    phone = "+91 99001 44556",
                    age = 28,
                    gender = "Female",
                    bloodGroup = "B+",
                    emergencyContactName = "Dr. Sundaram (Father)",
                    emergencyPhone = "+91 99001 22334",
                    category = MarathonCategory.HALF_MARATHON.name,
                    tShirtSize = "S",
                    wave = "Wave B (Sub 2:00 Pacer)",
                    corralGate = "Gate 3 - West Track, Kanteerava",
                    medicalNotes = "None",
                    qrVerificationCode = "BCM26-21-44B27C",
                    isCheckedIn = true,
                    baggageTag = "BAG-3045"
                ),
                RegistrationEntity(
                    bibNumber = "BLR-10-7210",
                    fullName = "Vikramaditya Hegde",
                    email = "vikram.hegde@techblr.com",
                    phone = "+91 94481 77665",
                    age = 41,
                    gender = "Male",
                    bloodGroup = "A+",
                    emergencyContactName = "Pooja Hegde (Sister)",
                    emergencyPhone = "+91 94481 11223",
                    category = MarathonCategory.OPEN_10K.name,
                    tShirtSize = "L",
                    wave = "Wave C (Open Fitness)",
                    corralGate = "Gate 4 - South Plaza, Kanteerava",
                    medicalNotes = "Mild hypertension (medicated)",
                    qrVerificationCode = "BCM26-10-C317E9",
                    isCheckedIn = false,
                    baggageTag = "BAG-7210"
                )
            )

            for (sample in samples) {
                registrationDao.insertRegistration(sample)
            }
        }
    }

    suspend fun registerRunner(
        fullName: String,
        email: String,
        phone: String,
        age: Int,
        gender: String,
        bloodGroup: String,
        emergencyContactName: String,
        emergencyPhone: String,
        category: MarathonCategory,
        tShirtSize: String,
        wave: String,
        medicalNotes: String
    ): Long {
        val randomNum = Random.nextInt(1000, 9999)
        val bibNumber = "${category.bibPrefix}-$randomNum"
        val qrHash = "BCM26-${category.bibPrefix.takeLast(2)}-" + UUID.randomUUID().toString().take(6).uppercase()
        val baggageTag = "BAG-$randomNum"
        val corralGate = when (category) {
            MarathonCategory.FULL_MARATHON -> "Gate 1 - North Stand, Kanteerava"
            MarathonCategory.HALF_MARATHON -> "Gate 2 - East Stand, Kanteerava"
            MarathonCategory.OPEN_10K -> "Gate 3 - West Concourse, Kanteerava"
            MarathonCategory.HOPE_RUN_5K -> "Gate 4 - South Plaza, Kanteerava"
        }

        val entity = RegistrationEntity(
            bibNumber = bibNumber,
            fullName = fullName.trim(),
            email = email.trim(),
            phone = phone.trim(),
            age = age,
            gender = gender,
            bloodGroup = bloodGroup,
            emergencyContactName = emergencyContactName.trim(),
            emergencyPhone = emergencyPhone.trim(),
            category = category.name,
            tShirtSize = tShirtSize,
            wave = wave,
            corralGate = corralGate,
            medicalNotes = medicalNotes.trim(),
            qrVerificationCode = qrHash,
            isCheckedIn = false,
            baggageTag = baggageTag
        )

        return registrationDao.insertRegistration(entity)
    }

    suspend fun updateCheckInStatus(id: Long, checkedIn: Boolean) {
        registrationDao.updateCheckInStatus(id, checkedIn)
    }

    suspend fun deleteRegistration(id: Long) {
        registrationDao.deleteRegistration(id)
    }
}
