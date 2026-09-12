package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistrationDao {
    @Query("SELECT * FROM registrations ORDER BY registrationTimestamp DESC")
    fun getAllRegistrations(): Flow<List<RegistrationEntity>>

    @Query("SELECT * FROM registrations WHERE id = :id LIMIT 1")
    fun getRegistrationById(id: Long): Flow<RegistrationEntity?>

    @Query("SELECT * FROM registrations WHERE bibNumber = :bibNumber LIMIT 1")
    fun getRegistrationByBib(bibNumber: String): Flow<RegistrationEntity?>

    @Query("SELECT * FROM registrations ORDER BY id DESC LIMIT 1")
    fun getLatestRegistration(): Flow<RegistrationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegistration(registration: RegistrationEntity): Long

    @Update
    suspend fun updateRegistration(registration: RegistrationEntity)

    @Query("UPDATE registrations SET isCheckedIn = :checkedIn WHERE id = :id")
    suspend fun updateCheckInStatus(id: Long, checkedIn: Boolean)

    @Query("DELETE FROM registrations WHERE id = :id")
    suspend fun deleteRegistration(id: Long)

    @Query("SELECT COUNT(*) FROM registrations")
    suspend fun getRegistrationCount(): Int
}
