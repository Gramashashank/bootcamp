package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CourseCheckpoint
import com.example.tracking.LiveRunnerState

@Composable
fun CourseMapVisualizer(
    checkpoints: List<CourseCheckpoint>,
    runnerState: LiveRunnerState,
    onCheckpointSelected: (CourseCheckpoint) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val totalKm = runnerState.totalDistanceKm.coerceAtLeast(1f)
    val currentKm = runnerState.currentDistanceKm.coerceIn(0f, totalKm)
    val progress = (currentKm / totalKm).coerceIn(0f, 1f)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseRadius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAlpha"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E))
                        )
                        Text(
                            text = "LIVE BENGALURU COURSE RADAR",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                    Text(
                        text = "${runnerState.runnerName} (${runnerState.bibNumber})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1E293B)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsRun,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${(progress * 100).toInt()}% COMPLETED",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF38BDF8)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Canvas drawing the Bengaluru Marathon Circuit
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1E293B))
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    val w = size.width
                    val h = size.height

                    // Grid lines (subtle radar aesthetic)
                    val gridColor = Color(0xFF334155)
                    for (x in 1..4) {
                        drawLine(
                            color = gridColor.copy(alpha = 0.4f),
                            start = Offset(w * (x / 5f), 0f),
                            end = Offset(w * (x / 5f), h),
                            strokeWidth = 1f
                        )
                    }
                    for (y in 1..3) {
                        drawLine(
                            color = gridColor.copy(alpha = 0.4f),
                            start = Offset(0f, h * (y / 4f)),
                            end = Offset(w, h * (y / 4f)),
                            strokeWidth = 1f
                        )
                    }

                    // Authentic Bengaluru Marathon Course Loop Path
                    // Starts at Kanteerava (left center) -> loops via Cubbon Park, Vidhana Soudha, MG Rd, Ulsoor, Lalbagh, return
                    val points = listOf(
                        Offset(w * 0.12f, h * 0.70f), // Kanteerava Start
                        Offset(w * 0.22f, h * 0.35f), // Cubbon Park
                        Offset(w * 0.36f, h * 0.22f), // Vidhana Soudha
                        Offset(w * 0.52f, h * 0.30f), // MG Road Boulevard
                        Offset(w * 0.70f, h * 0.20f), // Ulsoor Lake
                        Offset(w * 0.88f, h * 0.45f), // Domlur
                        Offset(w * 0.76f, h * 0.78f), // Lalbagh Botanical
                        Offset(w * 0.48f, h * 0.82f), // Richmond Circle
                        Offset(w * 0.28f, h * 0.80f), // Hudson Circle
                        Offset(w * 0.15f, h * 0.72f)  // Kanteerava Finish
                    )

                    // Draw total course path in slate grey
                    val path = Path().apply {
                        moveTo(points[0].x, points[0].y)
                        for (i in 1 until points.size) {
                            val prev = points[i - 1]
                            val curr = points[i]
                            val cx = (prev.x + curr.x) / 2
                            val cy = (prev.y + curr.y) / 2
                            quadraticTo(prev.x, prev.y, cx, cy)
                        }
                        lineTo(points.last().x, points.last().y)
                    }

                    drawPath(
                        path = path,
                        color = Color(0xFF475569),
                        style = Stroke(
                            width = 6.dp.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )

                    // Draw completed segment in glowing electric cyan
                    val completedCount = (progress * (points.size - 1)).toInt().coerceIn(0, points.size - 1)
                    if (completedCount > 0) {
                        val compPath = Path().apply {
                            moveTo(points[0].x, points[0].y)
                            for (i in 1..completedCount) {
                                val prev = points[i - 1]
                                val curr = points[i]
                                val cx = (prev.x + curr.x) / 2
                                val cy = (prev.y + curr.y) / 2
                                quadraticTo(prev.x, prev.y, cx, cy)
                            }
                        }
                        drawPath(
                            path = compPath,
                            color = Color(0xFF38BDF8),
                            style = Stroke(
                                width = 6.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }

                    // Checkpoint dots
                    for (i in points.indices) {
                        val pt = points[i]
                        val isPassed = i <= completedCount
                        val ptColor = if (isPassed) Color(0xFF38BDF8) else Color(0xFF64748B)
                        drawCircle(
                            color = ptColor,
                            radius = if (i == 0 || i == points.size - 1) 5.dp.toPx() else 3.5.dp.toPx(),
                            center = pt
                        )
                    }

                    // Calculate current runner point along the interpolated path
                    val segIndex = (progress * (points.size - 1)).toInt().coerceIn(0, points.size - 2)
                    val segRatio = (progress * (points.size - 1) - segIndex).coerceIn(0f, 1f)
                    val p1 = points[segIndex]
                    val p2 = points[segIndex + 1]
                    val runnerX = p1.x + (p2.x - p1.x) * segRatio
                    val runnerY = p1.y + (p2.y - p1.y) * segRatio
                    val runnerPos = Offset(runnerX, runnerY)

                    // Pulse effect
                    drawCircle(
                        color = Color(0xFFF97316).copy(alpha = pulseAlpha),
                        radius = pulseRadius.dp.toPx(),
                        center = runnerPos
                    )
                    // Core runner marker
                    drawCircle(
                        color = Color(0xFFF97316),
                        radius = 6.dp.toPx(),
                        center = runnerPos
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 2.5.dp.toPx(),
                        center = runnerPos
                    )
                }

                // Landmark overlay labels
                Text(
                    text = "Kanteerava (Start/Finish)",
                    color = Color(0xFFE2E8F0),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 12.dp, bottom = 8.dp)
                )

                Text(
                    text = "Vidhana Soudha (10K)",
                    color = Color(0xFF94A3B8),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 8.dp)
                )

                Text(
                    text = "Ulsoor Lake (21K)",
                    color = Color(0xFF94A3B8),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 8.dp, end = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Current Runner Live Telemetry Ribbon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TelemetryCard(
                    title = "DISTANCE",
                    value = String.format("%.2f", currentKm),
                    unit = "/ ${String.format("%.1f", totalKm)} KM",
                    icon = Icons.Default.LocationOn,
                    iconTint = Color(0xFF38BDF8),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TelemetryCard(
                    title = "PACE",
                    value = runnerState.currentPaceMinSec,
                    unit = "avg ${runnerState.avgPaceMinSec}",
                    icon = Icons.Default.Speed,
                    iconTint = Color(0xFF34D399),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TelemetryCard(
                    title = "ELAPSED",
                    value = formatTime(runnerState.elapsedSeconds),
                    unit = "ETA: ${formatTime(runnerState.projectedFinishSeconds)}",
                    icon = Icons.Default.Timer,
                    iconTint = Color(0xFFFBBF24),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Checkpoint Milestones Progress List
            Text(
                text = "COURSE CHECKPOINTS & TIMING MATS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8),
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                checkpoints.take(5).forEach { cp ->
                    val isCompleted = currentKm >= cp.distanceKm
                    val isNext = !isCompleted && (runnerState.nextCheckpoint?.id == cp.id)

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = when {
                            isCompleted -> Color(0xFF1E293B)
                            isNext -> Color(0xFF1E3A5F)
                            else -> Color(0xFF141E2F)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCheckpointSelected(cp) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = when {
                                        isCompleted -> Color(0xFF22C55E)
                                        isNext -> Color(0xFFF97316)
                                        else -> Color(0xFF64748B)
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                                Column {
                                    Text(
                                        text = cp.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCompleted || isNext) Color.White else Color(0xFF94A3B8)
                                    )
                                    Text(
                                        text = cp.landmark,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (cp.hasWater) {
                                    Icon(
                                        imageVector = Icons.Default.LocalDrink,
                                        contentDescription = "Hydration station",
                                        tint = Color(0xFF38BDF8),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                if (cp.hasMedical) {
                                    Icon(
                                        imageVector = Icons.Default.LocalHospital,
                                        contentDescription = "Medical station",
                                        tint = Color(0xFFF43F5E),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = "${String.format("%.1f", cp.distanceKm)} KM",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCompleted) Color(0xFF22C55E) else Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TelemetryCard(
    title: String,
    value: String,
    unit: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1E293B),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                color = Color(0xFF64748B)
            )
        }
    }
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
