package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.RegistrationEntity
import com.example.data.model.MarathonCategory
import com.example.ui.components.DigitalBibCard
import com.example.ui.viewmodel.MarathonViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegistrationScreen(
    viewModel: MarathonViewModel,
    onNavigateToBib: () -> Unit,
    onNavigateToTracking: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val name by viewModel.formName.collectAsState()
    val email by viewModel.formEmail.collectAsState()
    val phone by viewModel.formPhone.collectAsState()
    val age by viewModel.formAge.collectAsState()
    val gender by viewModel.formGender.collectAsState()
    val bloodGroup by viewModel.formBloodGroup.collectAsState()
    val emergencyName by viewModel.formEmergencyName.collectAsState()
    val emergencyPhone by viewModel.formEmergencyPhone.collectAsState()
    val tShirtSize by viewModel.formTShirtSize.collectAsState()
    val wave by viewModel.formWave.collectAsState()
    val medicalNotes by viewModel.formMedical.collectAsState()
    val successEvent by viewModel.registrationSuccessEvent.collectAsState()

    val bloodGroups = listOf("O+", "A+", "B+", "AB+", "O-", "A-", "B-", "AB-")
    val tShirtSizes = listOf("XS", "S", "M", "L", "XL", "XXL")
    val genders = listOf("Female", "Male", "Non-binary")

    var showSuccessDialog by remember { mutableStateOf(false) }
    var newlyCreatedBib by remember { mutableStateOf<RegistrationEntity?>(null) }

    val scrollState = rememberScrollState()

    // Temporary preview entity for live bib preview as user inputs details
    val previewEntity = remember(name, selectedCategory, bloodGroup, wave, emergencyPhone) {
        RegistrationEntity(
            bibNumber = "${selectedCategory.bibPrefix}-7721",
            fullName = name.ifBlank { "YOUR NAME HERE" },
            email = email,
            phone = phone,
            age = age.toIntOrNull() ?: 28,
            gender = gender,
            bloodGroup = bloodGroup,
            emergencyContactName = emergencyName,
            emergencyPhone = emergencyPhone.ifBlank { "+91 98450 XXXXX" },
            category = selectedCategory.name,
            tShirtSize = tShirtSize,
            wave = wave,
            corralGate = when (selectedCategory) {
                MarathonCategory.FULL_MARATHON -> "Gate 1 - North"
                MarathonCategory.HALF_MARATHON -> "Gate 2 - East"
                MarathonCategory.OPEN_10K -> "Gate 3 - West"
                MarathonCategory.HOPE_RUN_5K -> "Gate 4 - South"
            },
            medicalNotes = medicalNotes,
            qrVerificationCode = "BCM26-${selectedCategory.bibPrefix.takeLast(2)}-PREVIEW",
            isCheckedIn = false,
            baggageTag = "BAG-7721"
        )
    }

    Box(modifier = modifier.fillMaxSize().background(Color(0xFFF8FAFC))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 90.dp)
        ) {
            // Hero Banner Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_marathon_hero),
                    contentDescription = "Bengaluru City Marathon Runners",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient Scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x220F172A),
                                    Color(0xE60F172A)
                                )
                            )
                        )
                )
                // Overlay text
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFE11D48)
                    ) {
                        Text(
                            text = "OFFICIAL REGISTRATION OPEN",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Bengaluru City Marathon 2026",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Sunday, Oct 18, 2026",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2E8F0)
                        )
                        Text(
                            text = "•",
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "Sri Kanteerava Stadium",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE2E8F0)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Category Selection Section
                Text(
                    text = "SELECT RACE CATEGORY",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = Color(0xFF334155)
                )
                Spacer(modifier = Modifier.height(10.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    MarathonCategory.values().forEach { cat ->
                        val isSelected = selectedCategory == cat
                        val catColor = cat.badgeColor

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("category_card_${cat.name}")
                                .clickable { viewModel.selectCategory(cat) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color.White else Color(0xFFF1F5F9)
                            ),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, catColor) else null,
                            elevation = CardDefaults.cardElevation(if (isSelected) 6.dp else 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(catColor.copy(alpha = if (isSelected) 1f else 0.15f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DirectionsRun,
                                            contentDescription = null,
                                            tint = if (isSelected) Color.White else catColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Column {
                                        Text(
                                            text = cat.title,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A)
                                        )
                                        Text(
                                            text = "${cat.distanceLabel} • Start ${cat.startTime} • Cutoff ${cat.cutoffTime}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = cat.fee,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSelected) catColor else Color(0xFF0F172A)
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = catColor,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Runner Details Form
                Text(
                    text = "RUNNER INFORMATION",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    color = Color(0xFF334155)
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { viewModel.formName.value = it },
                            label = { Text("Full Name (as on Digital Bib)") },
                            placeholder = { Text("e.g. Rahul Dravid") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("input_full_name"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF0F172A),
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { viewModel.formEmail.value = it },
                                label = { Text("Email Address") },
                                placeholder = { Text("runner@email.com") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.weight(1f).testTag("input_email")
                            )

                            OutlinedTextField(
                                value = phone,
                                onValueChange = { viewModel.formPhone.value = it },
                                label = { Text("Mobile Phone") },
                                placeholder = { Text("+91 98450...") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.weight(1f).testTag("input_phone")
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = age,
                                onValueChange = { viewModel.formAge.value = it },
                                label = { Text("Age") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.width(90.dp).testTag("input_age")
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Gender",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    genders.forEach { g ->
                                        val isSel = gender == g
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSel) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                                            modifier = Modifier
                                                .clickable { viewModel.formGender.value = g }
                                        ) {
                                            Text(
                                                text = g,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSel) Color.White else Color(0xFF475569),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Blood Group Selector
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Emergency,
                                    contentDescription = null,
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Blood Group (Printed on Digital Bib for Medical Safety)",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF475569)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                bloodGroups.forEach { bg ->
                                    val isSel = bloodGroup == bg
                                    FilterChip(
                                        selected = isSel,
                                        onClick = { viewModel.formBloodGroup.value = bg },
                                        label = { Text(bg, fontWeight = FontWeight.Bold) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFDC2626),
                                            selectedLabelColor = Color.White
                                        )
                                    )
                                }
                            }
                        }

                        // Emergency Contact
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = emergencyName,
                                onValueChange = { viewModel.formEmergencyName.value = it },
                                label = { Text("Emergency Contact Name") },
                                placeholder = { Text("Spouse / Parent / Friend") },
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("input_emergency_name")
                            )

                            OutlinedTextField(
                                value = emergencyPhone,
                                onValueChange = { viewModel.formEmergencyPhone.value = it },
                                label = { Text("Emergency Phone") },
                                placeholder = { Text("+91 99000...") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.weight(1f).testTag("input_emergency_phone")
                            )
                        }

                        // T-Shirt Size
                        Column {
                            Text(
                                text = "Finisher Technical Dry-Fit T-Shirt Size",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF475569)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                tShirtSizes.forEach { sz ->
                                    val isSel = tShirtSize == sz
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSel) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                                        border = if (isSel) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                        modifier = Modifier.clickable { viewModel.formTShirtSize.value = sz }
                                    ) {
                                        Text(
                                            text = sz,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSel) Color.White else Color(0xFF334155),
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Medical Notes
                        OutlinedTextField(
                            value = medicalNotes,
                            onValueChange = { viewModel.formMedical.value = it },
                            label = { Text("Medical History / Allergies (Optional)") },
                            placeholder = { Text("e.g. Asthma, Penicillin allergy, None") },
                            modifier = Modifier.fillMaxWidth().testTag("input_medical_notes")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Live Digital Bib Preview
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LIVE DIGITAL RACE BIB PREVIEW",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = Color(0xFF334155)
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE2E8F0)
                    ) {
                        Text(
                            text = "AUTOGENERATED",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your official digital race bib is generated immediately with high-precision timing RFID and gate verification QR matrix.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Render Digital Bib Preview
                DigitalBibCard(
                    registration = previewEntity,
                    isPresentationMode = false
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Complete Registration CTA
                Button(
                    onClick = {
                        viewModel.registerRunner { reg ->
                            newlyCreatedBib = reg
                            showSuccessDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("submit_registration_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = selectedCategory.badgeColor)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Badge, contentDescription = null, tint = Color.White)
                        Text(
                            text = "CONFIRM REGISTRATION • ${selectedCategory.fee}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Success Dialog
        if (showSuccessDialog && newlyCreatedBib != null) {
            val reg = newlyCreatedBib!!
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = "Registration Confirmed!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black
                        )
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Congratulations, ${reg.fullName}! You are officially registered for Bengaluru City Marathon 2026.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF334155)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF1F5F9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "OFFICIAL BIB NUMBER",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B)
                                )
                                Text(
                                    text = reg.bibNumber,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Corral: ${reg.corralGate} • ${reg.wave}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF475569)
                                )
                            }
                        }
                        Text(
                            text = "Your digital race bib is activated with real-time runner tracking enabled on the course.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            onNavigateToBib()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
                    ) {
                        Text("View Digital Bib")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            onNavigateToTracking()
                        }
                    ) {
                        Text("Live Tracker")
                    }
                }
            )
        }
    }
}
