package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rockandcode.prodefutbolero.domain.tournament.models.AverageByDate
import com.rockandcode.prodefutbolero.utils.AppConstants
import com.rockandcode.prodefutbolero.utils.ordinalEs

@Composable
fun HomeAverageByDateCard(
    title: String,
    averageList: List<AverageByDate>,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    myPosition: String,
) {
    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) Color(0xFF27292D) else Color.White
    val maxChartHeight = 80.dp
//    val shadowAmbient = if (isDark) Color(0x22FFFFFF) else Color(0x22000000)
//    val shadowSpot = shadowAmbient

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = AppConstants.CARD_HORIZONTAL_PADDING.dp, vertical = 4.dp),
        shape = RoundedCornerShape(36.dp),
        // elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
//    Card(
//        modifier =
//            modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 4.dp)
//                .shadow(
//                    elevation = 4.dp,
//                    shape = RoundedCornerShape(36.dp),
//                    ambientColor = shadowAmbient,
//                    spotColor = shadowSpot,
//                ),
//        shape = RoundedCornerShape(36.dp),
//        colors = CardDefaults.cardColors(containerColor = cardColor),
//    ) {
        Column(
            modifier =
                Modifier
                    .padding(bottom = 16.dp),
        ) {
            HeaderCard(
                // leftIcon = Icons.Outlined.EmojiEvents,
                // rightIcon = Icons.AutoMirrored.Filled.ArrowForward,
                rightIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                title = title,
                subTitle = "Mis estadísticas",
                onClick = onMoreClick,
            )

            Spacer(modifier = Modifier.height(8.dp))

            val maxFecha = averageList.maxByOrNull { it.points }
            val maxPoints = maxFecha?.points ?: 0

            ScoresSummary(
                totalPoints = averageList.sumOf { it.points },
                myPosition = myPosition,
            )

            Spacer(modifier = Modifier.height(16.dp))
            val filteredAverageList = averageList.filter { it.points > 0 }

            // Gráfico de barras
            if (filteredAverageList.isNotEmpty()) {
                BoxWithConstraints(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                ) {
                    val containerWidth = this.maxWidth
                    val itemCount = filteredAverageList.size
                    val minBars = 7
                    val spacing = 12.dp
                    val totalSpacing = spacing * (itemCount - 1)
                    val totalHorizontalPadding = 32.dp // 16.dp each side (porque ya aplicamos .padding)

                    // Cálculo dinámico del ancho
                    val barWidth =
                        if (itemCount < minBars) {
                            (containerWidth - totalHorizontalPadding - totalSpacing) / itemCount
                        } else {
                            40.dp
                        }

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                    ) {
                        items(filteredAverageList) { item ->
                            val barColor = if (isDark) Color(0xFFF1FD72) else Color(0xFF4270F6)
                            val barHeight =
                                if (item.points > 0) {
                                    ((item.points.toFloat() / maxPoints) * maxChartHeight.value).dp
                                } else {
                                    8.dp
                                }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.width(barWidth),
                            ) {
                                // Texto de puntos (solo si tiene puntos)
                                if (item.points > 0) {
                                    Text(
                                        text = item.points.toString(),
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = if (isDark) Color.White else Color.Black,
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(14.dp))
                                }
                                Box(
                                    modifier =
                                        Modifier
                                            .height(barHeight)
                                            .fillMaxWidth()
                                            .background(
                                                barColor,
                                                shape = RoundedCornerShape(12.dp),
                                            ),
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ordinalEs(item.number),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isDark) Color(0xFFB3B3B3) else Color.DarkGray,
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
fun ScoresSummary(
    totalPoints: Int,
    myPosition: String,
    modifier: Modifier = Modifier,
) {
    val isDark = isSystemInDarkTheme()
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Card Total Puntaje
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {
            ScoreItem(value = myPosition, title = "Posición", isDark = isDark)
        }

        // Card Total Puntaje
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        ) {
            ScoreItem(value = totalPoints.toString(), title = "Puntaje total", isDark = isDark)
        }
    }
}

@Composable
fun ScoreItem(
    title: String,
    value: String,
    isDark: Boolean,
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value,
            modifier = Modifier.width(80.dp),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            color = if (isDark) Color(0xFFB3B3B3) else Color.DarkGray,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
