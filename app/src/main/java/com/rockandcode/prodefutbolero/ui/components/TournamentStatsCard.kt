package com.rockandcode.prodefutbolero.ui.components

import android.graphics.CornerPathEffect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.rockandcode.prodefutbolero.domain.prediction.models.PredictionSummary
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TournamentStatsCard(
    title: String,
    modifier: Modifier = Modifier,
    predictionSummary: PredictionSummary?,
    onMoreClick: () -> Unit,
) {
    val currentValue = predictionSummary?.userPoints?.toFloat() ?: 0f
    val targetValue = predictionSummary?.maxPointsSoFar?.toFloat() ?: 0f
    val maxValue = predictionSummary?.maxPointsSoFar?.toFloat() ?: 0f

    val tips =
        listOf(
            "Tenés ${currentValue.toInt()} puntos de los ${targetValue.toInt()} en juego.",
            "Restan por jugarse ${predictionSummary?.remainingMatches} partidos.",
            "¡Mucha suerte!.",
        )

    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) Color(0xFF27292D) else Color.White
    val iconColor = if (isDark) Color(0xFFF1FD72) else Color(0xFF4270F6)
//    val shadowAmbient = if (isDark) Color(0x22FFFFFF) else Color(0x22000000)
//    val shadowSpot = shadowAmbient

    val sizeDp = 220.dp
    val strokeWidthPx = 120f
    val density = LocalDensity.current

    // Convertimos tamaño y centro a px dentro de composición
    val sizePx = with(density) { sizeDp.toPx() }
    val centerX = sizePx / 2f
    val centerY = sizePx / 2f
    val radius = sizePx / 2f - strokeWidthPx

    // Ángulo para la meta (extremo derecho del semicírculo)
    val angleTarget = 360f
    val radianTarget = Math.toRadians(angleTarget.toDouble())
    val chipRadius = radius + strokeWidthPx / 2 + 20f

    // Posición en px del chip
    val chipX = centerX + chipRadius * cos(radianTarget).toFloat()
    val chipY = centerY + chipRadius * sin(radianTarget).toFloat()

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(36.dp),
        // elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(bottom = 4.dp),
        ) {
            HeaderCard(
                rightIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                title = title,
                subTitle = predictionSummary?.dateName ?: "",
                onClick = onMoreClick,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(width = sizeDp, height = sizeDp / 2)) {
                    val strokeWidth = strokeWidthPx
                    val center = Offset(size.width / 2, size.height)
                    val radius = size.width / 2 - strokeWidth / 2

                    // Fondo gris
//                    drawArc(
//                        color = Color.Gray.copy(alpha = 0.3f),
//                        startAngle = 180f,
//                        sweepAngle = 180f,
//                        useCenter = false,
//                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
//                        topLeft = Offset(center.x - radius, center.y - radius),
//                        size = Size(radius * 2, radius * 2),
//                    )

                    // Fondo gris ray ado
                    drawArc(
                        color = Color.Gray.copy(alpha = 0.3f),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        style =
                            Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Butt,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f),
                            ),
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                    )

                    // Progreso amarillo
                    val sweepCurrent = (currentValue / maxValue) * 180f
                    drawArc(
                        color = iconColor,
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

                    val baseRadius = indicatorRadius + 90f // más largo para darle altura
                    val angleOffset = Math.PI / 40 // más estrecho (4.5° aprox)

                    val left =
                        Offset(
                            x = center.x + baseRadius * cos(radian + angleOffset).toFloat(),
                            y = center.y + baseRadius * sin(radian + angleOffset).toFloat(),
                        )
                    val right =
                        Offset(
                            x = center.x + baseRadius * cos(radian - angleOffset).toFloat(),
                            y = center.y + baseRadius * sin(radian - angleOffset).toFloat(),
                        )

                    val path =
                        Path().apply {
                            moveTo(tip.x, tip.y)
                            lineTo(left.x, left.y)
                            lineTo(right.x, right.y)
                            close()
                        }
                    drawIntoCanvas { canvas ->
                        val paint =
                            Paint().asFrameworkPaint().apply {
                                isAntiAlias = true
                                // color = "#EEFD72".toColorInt()
                                color = if (isDark) ("#EEFD72").toColorInt() else ("#4270F6").toColorInt()
                                style = android.graphics.Paint.Style.FILL
                                pathEffect = CornerPathEffect(40f)
                            }
                        canvas.nativeCanvas.drawPath(path.asAndroidPath(), paint)
                    }
                }

                // Texto central
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center).padding(top = 64.dp),
                ) {
                    Text(
                        "${currentValue.toInt()}",
                        color = if (isDark) Color.White else Color.Black,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text("Ganados", color = Color.Gray, fontSize = 14.sp)
                }

                // Chip objetivo posicionado con offset calculado
                val offsetX = chipX / density.density - (sizeDp.value / 3) - 15f
                val offsetY = chipY / density.density - (sizeDp.value / 3) + 10f

                Box(
                    modifier =
                        Modifier
                            .offset(x = offsetX.dp, y = offsetY.dp)
                            // .rotate(-15f)
                            .background(iconColor, RoundedCornerShape(24.dp))
                            .padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end = 16.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier =
                                Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isDark) Color.Black else MaterialTheme.colorScheme.background,
                                        shape = CircleShape,
                                    ),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                Icons.Outlined.Flag,
                                contentDescription = "Icono",
                                tint = if (isDark) Color(0xFFA2F7A1) else Color.Black,
                            )
                        }

                        // Icon(Icons.Default.Flag, contentDescription = null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${targetValue.toInt()}",
                            color = if (isDark) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            // Lista de tips
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp, top = 32.dp),
                // .offset(y = (-70).dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(
                                if (isDark) Color(0xFF2E3134) else MaterialTheme.colorScheme.background,
                                RoundedCornerShape(32.dp),
                            ).padding(16.dp),
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
                                        .background(iconColor, CircleShape),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Check",
                                    tint = if (isDark) Color.Black else Color.White,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Text(tip, color = if (isDark) Color.LightGray else Color.DarkGray, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}
