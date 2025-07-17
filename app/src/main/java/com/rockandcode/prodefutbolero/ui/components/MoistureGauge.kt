package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

// @Composable
// fun MoistureGauge(
//    modifier: Modifier = Modifier,
//    currentValue: Float = 60f,
//    targetValue: Float = 80f,
//    maxValue: Float = 100f,
//    tips: List<String> =
//        listOf(
//            "Insert the probe 2-3 inches deep.",
//            "Wipe the probe clean between tests.",
//            "Avoid inserting near roots or rocks.",
//        ),
// ) {
//    Column(
//        modifier =
//            modifier
//                .background(Color(0xFF1E1E1E), RoundedCornerShape(36.dp))
//                .padding(16.dp),
//    ) {
//        Text("Moisture Tips", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//            Canvas(modifier = Modifier.size(200.dp)) {
//                val strokeWidth = 30f
//                val centerX = size.width / 2
//                val centerY = size.height / 2
//                val radius = size.minDimension / 2 - strokeWidth
//
//                // Background arc (gray)
//                drawArc(
//                    color = Color.Gray.copy(alpha = 0.3f),
//                    startAngle = 180f,
//                    sweepAngle = 180f,
//                    useCenter = false,
//                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
//                    topLeft = Offset(centerX - radius, centerY - radius),
//                    size = Size(radius * 2, radius * 2),
//                )
//
//                // Current value arc (yellow)
//                val sweepCurrent = (currentValue / maxValue) * 180f
//                drawArc(
//                    color = Color(0xFFE8FF54),
//                    startAngle = 180f,
//                    sweepAngle = sweepCurrent,
//                    useCenter = false,
//                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
//                    topLeft = Offset(centerX - radius, centerY - radius),
//                    size = Size(radius * 2, radius * 2),
//                )
//
//                // Hatched area for difference between current and target
//                val startHatchAngle = 180f + sweepCurrent
//                val sweepHatch = ((targetValue - currentValue) / maxValue) * 180f
//                if (sweepHatch > 0f) {
//                    // Draw hatches as small lines within the arc segment
//                    val hatchCount = 20
//                    val hatchLength = 15f
//                    for (i in 0 until hatchCount) {
//                        val angle = startHatchAngle + sweepHatch * i / hatchCount
//                        val radian = Math.toRadians(angle.toDouble())
//                        val innerX = centerX + (radius - strokeWidth / 2) * cos(radian).toFloat()
//                        val innerY = centerY + (radius - strokeWidth / 2) * sin(radian).toFloat()
//                        val outerX = centerX + (radius + hatchLength) * cos(radian).toFloat()
//                        val outerY = centerY + (radius + hatchLength) * sin(radian).toFloat()
//                        drawLine(
//                            color = Color.Gray,
//                            start = Offset(innerX, innerY),
//                            end = Offset(outerX, outerY),
//                            strokeWidth = 3f,
//                        )
//                    }
//                }
//
// // Indicador en forma de triángulo
//                val angleIndicator = 180f + sweepCurrent
//                val radianIndicator = Math.toRadians(angleIndicator.toDouble())
//
//                val indicatorRadius = radius + strokeWidth / 2
//                val baseX = centerX + indicatorRadius * cos(radianIndicator).toFloat()
//                val baseY = centerY + indicatorRadius * sin(radianIndicator).toFloat()
//
// // Triángulo equilátero
//                val triangleSize = 20f
//                val path =
//                    Path().apply {
//                        moveTo(baseX, baseY) // punta del triángulo
//
//                        // puntos de la base (perpendiculares al radio)
//                        val perpendicularAngle1 = radianIndicator + Math.PI / 2
//                        val perpendicularAngle2 = radianIndicator - Math.PI / 2
//
//                        val basePoint1X = baseX + triangleSize * cos(perpendicularAngle1).toFloat()
//                        val basePoint1Y = baseY + triangleSize * sin(perpendicularAngle1).toFloat()
//
//                        val basePoint2X = baseX + triangleSize * cos(perpendicularAngle2).toFloat()
//                        val basePoint2Y = baseY + triangleSize * sin(perpendicularAngle2).toFloat()
//
//                        lineTo(basePoint1X, basePoint1Y)
//                        lineTo(basePoint2X, basePoint2Y)
//                        close()
//                    }
//                drawPath(path, color = Color(0xFFE8FF54))
//            }
//
//            // Text inside the gauge
//            Row(
//                modifier =
//                    Modifier
//                        .align(Alignment.Center)
//                        .padding(top = 100.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp),
//            ) {
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(
//                        "${currentValue.toInt()}%",
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 24.sp,
//                    )
//                    Text("Current", color = Color.Gray, fontSize = 14.sp)
//                }
//
//                Box(
//                    modifier =
//                        Modifier
//                            .background(Color(0xFFE8FF54), RoundedCornerShape(30.dp))
//                            .padding(horizontal = 12.dp, vertical = 6.dp),
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(
//                            Icons.Default.Flag,
//                            contentDescription = "Target",
//                            tint = Color.Black,
//                            modifier = Modifier.size(18.dp),
//                        )
//                        Spacer(Modifier.width(8.dp))
//                        Text(
//                            "${targetValue.toInt()}%",
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 16.sp,
//                            color = Color.Black,
//                        )
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Tips list
//        Column(
//            modifier =
//                Modifier
//                    .fillMaxWidth()
//                    .background(Color(0xFF2B2B2B), RoundedCornerShape(24.dp))
//                    .padding(16.dp),
//        ) {
//            tips.forEach { tip ->
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.padding(vertical = 8.dp),
//                ) {
//                    Box(
//                        modifier =
//                            Modifier
//                                .size(28.dp)
//                                .background(Color(0xFFE8FF54), CircleShape),
//                        contentAlignment = Alignment.Center,
//                    ) {
//                        Icon(
//                            Icons.Default.Check,
//                            contentDescription = "Check",
//                            tint = Color.Black,
//                            modifier = Modifier.size(18.dp),
//                        )
//                    }
//                    Spacer(Modifier.width(12.dp))
//                    Text(tip, color = Color.White, fontSize = 16.sp)
//                }
//            }
//        }
//    }
// }

@Composable
fun MoistureGaugeFull(
    modifier: Modifier = Modifier,
    currentValue: Float = 23f,
    targetValue: Float = 84f,
    maxValue: Float = 84f,
    tips: List<String> =
        listOf(
            "Tenés ${currentValue.toInt()} puntos de los ${targetValue.toInt()} en juego.",
            "Restan por jugarse 8 partidos.",
            "¡Mucha suerte!.",
        ),
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E), RoundedCornerShape(36.dp))
                .padding(16.dp),
    ) {
        Text("Moisture Tips", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(250.dp)) {
                val strokeWidth = 40f
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.minDimension / 2 - strokeWidth

                // Fondo gris
                drawArc(
                    color = Color.Gray.copy(alpha = 0.3f),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                )

                // Progreso amarillo
                val sweepCurrent = (currentValue / maxValue) * 180f
                drawArc(
                    color = Color(0xFFE8FF54),
                    startAngle = 180f,
                    sweepAngle = sweepCurrent,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                )

                // Indicador (triángulo)
                val angle = 180f + sweepCurrent
                val radian = Math.toRadians(angle.toDouble())

                val indicatorRadius = radius + strokeWidth / 2 + 10f
                val tip =
                    Offset(
                        x = center.x + indicatorRadius * cos(radian).toFloat(),
                        y = center.y + indicatorRadius * sin(radian).toFloat(),
                    )

                val baseRadius = indicatorRadius + 30f
                val left =
                    Offset(
                        x = center.x + baseRadius * cos(radian + Math.PI / 10).toFloat(),
                        y = center.y + baseRadius * sin(radian + Math.PI / 10).toFloat(),
                    )
                val right =
                    Offset(
                        x = center.x + baseRadius * cos(radian - Math.PI / 10).toFloat(),
                        y = center.y + baseRadius * sin(radian - Math.PI / 10).toFloat(),
                    )

                val path =
                    Path().apply {
                        moveTo(tip.x, tip.y)
                        lineTo(left.x, left.y)
                        lineTo(right.x, right.y)
                        close()
                    }
                drawPath(path, color = Color(0xFFE8FF54))
            }

            // Texto central
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.align(Alignment.Center)) {
                Text("${currentValue.toInt()}%", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text("Current", color = Color.Gray, fontSize = 14.sp)
            }

            // Objetivo (chip amarillo)
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(top = 220.dp)
                        .background(Color(0xFFE8FF54), RoundedCornerShape(30.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Flag, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${targetValue.toInt()}%", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de tips
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2B2B2B), RoundedCornerShape(36.dp))
                    .padding(16.dp),
        ) {
            tips.forEach { tip ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(24.dp)
                                .background(Color(0xFFE8FF54), CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Check", tint = Color.Black, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(tip, color = Color.White, fontSize = 14.sp)
                }
            }
        }
    }
}
