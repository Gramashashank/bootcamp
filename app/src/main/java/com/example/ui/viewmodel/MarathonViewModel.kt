package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.RegistrationEntity
import com.example.data.model.CourseCheckpoint
import com.example.data.model.MarathonCategory
import com.example.data.repository.MarathonRepository
import com.example.tracking.BengaluruCourseData
import com.example.tracking.CheckpointSplit
import com.example.tracking.LiveRunnerState
import com.example.tracking.TrackingStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class MarathonViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MarathonRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = MarathonRepository(database.registrationDao())

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    val registrations: StateFlow<List<RegistrationEntity>> = repository.allRegistrations
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val latestRegistration: StateFlow<RegistrationEntity?> = repository.latestRegistration
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Selected runner for Bib screen & detailed view
    private val _selectedRegistration = MutableStateFlow<RegistrationEntity?>(null)
    val selectedRegistration: StateFlow<RegistrationEntity?> = _selectedRegistration.asStateFlow()

    // Full-screen presentation mode for digital race bib scanning
    private val _isPresentationMode = MutableStateFlow(false)
    val isPresentationMode: StateFlow<Boolean> = _isPresentationMode.asStateFlow()

    // Registration Form State
    private val _selectedCategory = MutableStateFlow(MarathonCategory.HALF_MARATHON)
    val selectedCategory: StateFlow<MarathonCategory> = _selectedCategory.asStateFlow()

    val formName = MutableStateFlow("")
    val formEmail = MutableStateFlow("")
    val formPhone = MutableStateFlow("")
    val formAge = MutableStateFlow("28")
    val formGender = MutableStateFlow("Female")
    val formBloodGroup = MutableStateFlow("O+")
    val formEmergencyName = MutableStateFlow("")
    val formEmergencyPhone = MutableStateFlow("")
    val formTShirtSize = MutableStateFlow("M")
    val formWave = MutableStateFlow("Wave B (Sub 2:00)")
    val formMedical = MutableStateFlow("")

    private val _registrationSuccessEvent = MutableStateFlow<RegistrationEntity?>(null)
    val registrationSuccessEvent: StateFlow<RegistrationEntity?> = _registrationSuccessEvent.asStateFlow()

    // Real-Time Runner Tracking State
    private val _activeTrackingRunner = MutableStateFlow<LiveRunnerState?>(null)
    val activeTrackingRunner: StateFlow<LiveRunnerState?> = _activeTrackingRunner.asStateFlow()

    // Search query in tracker
    val trackerSearchQuery = MutableStateFlow("")

    // Tracking simulation job
    private var trackingJob: Job? = null
    private var simulationSpeedMultiplier = 20.0f // 20x speed for smooth realistic real-time preview

    init {
        // Initial dummy runner state while DB loads
        val initialCheckpoints = BengaluruCourseData.HALF_COURSE_CHECKPOINTS
        _activeTrackingRunner.value = LiveRunnerState(
            bibNumber = "BLR-21-3045",
            runnerName = "Ananya Sundaram",
            categoryName = "Half Marathon",
            totalDistanceKm = 21.0975f,
            currentDistanceKm = 8.42f,
            currentPaceMinSec = "5:18 /km",
            avgPaceMinSec = "5:15 /km",
            elapsedSeconds = 2650L,
            projectedFinishSeconds = 6650L,
            heartRateBpm = 158,
            cadenceSpm = 174,
            currentCheckpoint = initialCheckpoints[1],
            nextCheckpoint = initialCheckpoints[2],
            splits = listOf(
                CheckpointSplit("Kanteerava Stadium (Start)", 0.0f, "00:00", "0:00", System.currentTimeMillis() - 2650000),
                CheckpointSplit("Cubbon Park (5K)", 5.0f, "26:12", "5:14", System.currentTimeMillis() - 1080000)
            ),
            status = TrackingStatus.RUNNING,
            lastLocationName = "Cubbon Park Avenue near Museum"
        )

        startTrackingSimulation()
    }

    fun selectCategory(category: MarathonCategory) {
        _selectedCategory.value = category
        // Adjust default wave based on category
        _selectedCategory.value.let { cat ->
            when (cat) {
                MarathonCategory.FULL_MARATHON -> formWave.value = "Wave A (Sub 3:45)"
                MarathonCategory.HALF_MARATHON -> formWave.value = "Wave B (Sub 2:00)"
                MarathonCategory.OPEN_10K -> formWave.value = "Wave C (Sub 55 min)"
                MarathonCategory.HOPE_RUN_5K -> formWave.value = "Wave D (Open Fun)"
            }
        }
    }

    fun selectRegistrationForBib(reg: RegistrationEntity) {
        _selectedRegistration.value = reg
        // Also update tracking runner if clicked
        setupTrackingForRunner(reg)
    }

    fun togglePresentationMode() {
        _isPresentationMode.value = !_isPresentationMode.value
    }

    fun setPresentationMode(enabled: Boolean) {
        _isPresentationMode.value = enabled
    }

    fun toggleCheckIn(reg: RegistrationEntity) {
        viewModelScope.launch {
            repository.updateCheckInStatus(reg.id, !reg.isCheckedIn)
            if (_selectedRegistration.value?.id == reg.id) {
                _selectedRegistration.value = reg.copy(isCheckedIn = !reg.isCheckedIn)
            }
        }
    }

    fun registerRunner(onSuccess: (RegistrationEntity) -> Unit) {
        val name = formName.value.ifBlank { "Bangalore Runner" }
        val email = formEmail.value.ifBlank { "runner@bengaluru.in" }
        val phone = formPhone.value.ifBlank { "+91 98450 00000" }
        val age = formAge.value.toIntOrNull() ?: 28
        val emergencyName = formEmergencyName.value.ifBlank { "Emergency Contact" }
        val emergencyPhone = formEmergencyPhone.value.ifBlank { "+91 99000 00000" }

        viewModelScope.launch {
            val insertedId = repository.registerRunner(
                fullName = name,
                email = email,
                phone = phone,
                age = age,
                gender = formGender.value,
                bloodGroup = formBloodGroup.value,
                emergencyContactName = emergencyName,
                emergencyPhone = emergencyPhone,
                category = _selectedCategory.value,
                tShirtSize = formTShirtSize.value,
                wave = formWave.value,
                medicalNotes = formMedical.value
            )

            // Fetch newly registered entity
            repository.latestRegistration.collect { latest ->
                if (latest != null && latest.id == insertedId) {
                    _selectedRegistration.value = latest
                    _registrationSuccessEvent.value = latest
                    setupTrackingForRunner(latest)
                    onSuccess(latest)
                }
            }
        }
    }

    fun clearSuccessEvent() {
        _registrationSuccessEvent.value = null
    }

    fun setupTrackingForRunner(reg: RegistrationEntity) {
        val cat = try {
            MarathonCategory.valueOf(reg.category)
        } catch (e: Exception) {
            MarathonCategory.FULL_MARATHON
        }

        val checkpoints = BengaluruCourseData.getCheckpointsForDistance(cat.distanceKm)
        val initialDist = (cat.distanceKm * 0.25f).coerceAtMost(cat.distanceKm)

        _activeTrackingRunner.value = LiveRunnerState(
            bibNumber = reg.bibNumber,
            runnerName = reg.fullName,
            categoryName = cat.title,
            totalDistanceKm = cat.distanceKm,
            currentDistanceKm = initialDist,
            currentPaceMinSec = "5:12 /km",
            avgPaceMinSec = "5:10 /km",
            elapsedSeconds = (initialDist * 312).toLong(),
            projectedFinishSeconds = (cat.distanceKm * 312).toLong(),
            heartRateBpm = 162,
            cadenceSpm = 176,
            currentCheckpoint = checkpoints.firstOrNull { it.distanceKm <= initialDist } ?: checkpoints[0],
            nextCheckpoint = checkpoints.firstOrNull { it.distanceKm > initialDist },
            splits = listOf(
                CheckpointSplit(checkpoints[0].name, 0.0f, "00:00", "0:00", System.currentTimeMillis() - 3600000)
            ),
            status = TrackingStatus.RUNNING,
            lastLocationName = checkpoints.firstOrNull { it.distanceKm <= initialDist }?.name ?: "Course"
        )
    }

    fun setSimulationSpeed(multiplier: Float) {
        simulationSpeedMultiplier = multiplier
    }

    fun toggleTrackingRun() {
        val current = _activeTrackingRunner.value ?: return
        if (current.status == TrackingStatus.RUNNING) {
            _activeTrackingRunner.value = current.copy(status = TrackingStatus.PAUSED)
        } else {
            _activeTrackingRunner.value = current.copy(status = TrackingStatus.RUNNING)
        }
    }

    fun resetRunnerToStart() {
        val current = _activeTrackingRunner.value ?: return
        val checkpoints = BengaluruCourseData.getCheckpointsForDistance(current.totalDistanceKm)
        _activeTrackingRunner.value = current.copy(
            currentDistanceKm = 0.0f,
            elapsedSeconds = 0L,
            currentPaceMinSec = "0:00 /km",
            currentCheckpoint = checkpoints[0],
            nextCheckpoint = checkpoints.getOrNull(1),
            splits = emptyList(),
            status = TrackingStatus.NOT_STARTED,
            lastLocationName = "Start Line - Kanteerava Stadium"
        )
    }

    private fun startTrackingSimulation() {
        trackingJob?.cancel()
        trackingJob = viewModelScope.launch {
            while (true) {
                delay(1000L) // 1-second pulse
                val current = _activeTrackingRunner.value ?: continue
                if (current.status != TrackingStatus.RUNNING) continue

                val distanceDelta = (0.003f * simulationSpeedMultiplier)
                val newDist = (current.currentDistanceKm + distanceDelta).coerceAtMost(current.totalDistanceKm)
                val newElapsed = current.elapsedSeconds + (1 * simulationSpeedMultiplier.toInt()).coerceAtLeast(1)

                val checkpoints = BengaluruCourseData.getCheckpointsForDistance(current.totalDistanceKm)
                val currentCp = checkpoints.lastOrNull { it.distanceKm <= newDist } ?: checkpoints[0]
                val nextCp = checkpoints.firstOrNull { it.distanceKm > newDist }

                // Check if crossed checkpoint
                val newSplits = current.splits.toMutableList()
                for (cp in checkpoints) {
                    if (cp.distanceKm > 0f && newDist >= cp.distanceKm && newSplits.none { it.checkpointName == cp.name }) {
                        val splitSecs = (cp.distanceKm * 315).toLong()
                        val paceSecs = 310 + Random.nextInt(-15, 20)
                        newSplits.add(
                            CheckpointSplit(
                                checkpointName = cp.name,
                                distanceKm = cp.distanceKm,
                                splitTimeFormatted = formatTime(splitSecs),
                                paceFormatted = "${paceSecs / 60}:${String.format("%02d", paceSecs % 60)} /km",
                                passedTimestamp = System.currentTimeMillis()
                            )
                        )
                    }
                }

                val isFinished = newDist >= current.totalDistanceKm
                val paceSec = 312 + Random.nextInt(-10, 10)
                val paceStr = "${paceSec / 60}:${String.format("%02d", paceSec % 60)} /km"
                val heartRate = 152 + Random.nextInt(-4, 6)
                val cadence = 174 + Random.nextInt(-2, 3)

                _activeTrackingRunner.value = current.copy(
                    currentDistanceKm = newDist,
                    elapsedSeconds = newElapsed,
                    currentPaceMinSec = paceStr,
                    heartRateBpm = heartRate,
                    cadenceSpm = cadence,
                    currentCheckpoint = currentCp,
                    nextCheckpoint = nextCp,
                    splits = newSplits,
                    status = if (isFinished) TrackingStatus.FINISHED else TrackingStatus.RUNNING,
                    lastLocationName = currentCp.landmark
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        trackingJob?.cancel()
    }

    private fun formatTime(seconds: Long): String {
        val hrs = seconds / 3600
        val mins = (seconds % 3600) / 60
        val secs = seconds % 60
        return if (hrs > 0) {
            String.format("%d:%02d:%02d", hrs, mins, secs)
        } else {
            String.format("%02d:%02d", mins, secs)
        }
    }
}
