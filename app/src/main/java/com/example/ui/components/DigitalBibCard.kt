package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.RegistrationEntity
import com.example.data.model.MarathonCategory

@Composable
fun DigitalBibCard(
    registration: RegistrationEntity,
    modifier: Modifier = Modifier,
    isPresentationMode: Boolean = false
) {
    val category = try {
        MarathonCategory.valueOf(registration.category)
    } catch (e: Exception) {
        MarathonCategory.FULL_MARATHON
    }

    val primaryColor = category.badgeColor
    val bibBackgroundColor = if (isPresentationMode) Color.White else Color(0xFFFAFAFA)
    val cornerRadius = if (isPresentationMode) 0.dp else 16.dp

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (!isPresentationMode) Modifier.shadow(12.dp, RoundedCornerShape(cornerRadius))
                else Modifier
            ),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = bibBackgroundColor),
        border = if (!isPresentationMode) BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(if (isPresentationMode) 20.dp else 16.dp)
        ) {
            // Pin hole row & Event Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Top-Left Pin hole
                BibPinHole()

                // Header Branding
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsRun,
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "BENGALURU CITY MARATHON",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp,
                            color = Color(0xFF0F172A)
                        )
                    }
                    Text(
                        text = "GARDEN CITY • 2026 EDITION • AIMS CERTIFIED",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        letterSpacing = 0.5.sp
                    )
                }

                // Top-Right Pin hole
                BibPinHole()
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category ribbon banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(primaryColor)
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.title.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = category.distanceLabel,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mega Bib Number
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = registration.bibNumber,
                    fontSize = if (isPresentationMode) 44.sp else 38.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 2.sp,
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Center
                )
            }

            // Runner Full Name
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = registration.fullName.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = primaryColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Wave, Start Corral & Timing Chip
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF1F5F9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "START TIME",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = category.startTime,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(1.dp)
                            .background(Color(0xFFCBD5E1))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ASSIGNED WAVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = registration.wave.take(12),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(1.dp)
                            .background(Color(0xFFCBD5E1))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "CORRAL GATE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF64748B),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = registration.corralGate.take(10),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Middle section: Digital QR Code + Verification & Check-in Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // QR Code
                DigitalQrCode(
                    data = "${registration.bibNumber}|${registration.qrVerificationCode}|${registration.fullName}",
                    size = if (isPresentationMode) 130.dp else 110.dp,
                    qrColor = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.width(14.dp))

                // Security & Chip info column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Check-in status pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                if (registration.isCheckedIn) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = if (registration.isCheckedIn) Icons.Default.CheckCircle else Icons.Default.QrCode,
                            contentDescription = null,
                            tint = if (registration.isCheckedIn) Color(0xFF16A34A) else Color(0xFFD97706),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (registration.isCheckedIn) "CHECKED-IN" else "READY TO SCAN",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (registration.isCheckedIn) Color(0xFF15803D) else Color(0xFFB45309)
                        )
                    }

                    Text(
                        text = "SECURE PASS: ${registration.qrVerificationCode}",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF475569)
                    )

                    Text(
                        text = "TIMING RFID: BLR-CHIP-${registration.bibNumber.takeLast(4)}",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF64748B)
                    )

                    // Medical & Blood Group Pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFE4E6))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Emergency,
                            contentDescription = null,
                            tint = Color(0xFFE11D48),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "BLOOD: ${registration.bloodGroup} • ICE: ${registration.emergencyPhone}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBE123C)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Perforated Baggage Drop Tear-off Strip
            DashedPerforationDivider()

            Spacer(modifier = Modifier.height(10.dp))

            // Baggage Strip Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Bottom-Left Pin hole
                BibPinHole()

                Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "BAGGAGE CLAIM TAG",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569)
                        )
                        Text(
                            text = registration.baggageTag,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF0F172A)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    DigitalBarcode(
                        code = registration.baggageTag,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                    )
                }

                // Bottom-Right Pin hole
                BibPinHole()
            }
        }
    }
}

@Composable
fun BibPinHole() {
    Box(
        modifier = Modifier
            .size(14.dp)
            .border(1.dp, Color(0xFFCBD5E1), CircleShape)
            .background(Color(0xFFE2E8F0), CircleShape)
    )
}

@Composable
fun DashedPerforationDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFCBD5E1))
    )
}
