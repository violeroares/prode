package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.domain.match.models.Match
import com.rockandcode.prodefutbolero.utils.AppConstants
import com.rockandcode.prodefutbolero.utils.formatFechaHora
import com.rockandcode.prodefutbolero.utils.statusName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MatchesTodayCard(
    matchesDate: List<Match>,
    isLoading: Boolean,
    onClick: () -> Unit,
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    val partidosDeHoy =
        remember(matchesDate) {
            matchesDate.filter { match ->
                val matchDate = LocalDateTime.parse(match.date, formatter).toLocalDate()
                matchDate == LocalDate.now()
            }
        }

    val partidosPorDia =
        partidosDeHoy.groupBy { match ->
            val dateTime = LocalDateTime.parse(match.date, formatter)
            dateTime.format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM", Locale("es")))
        }

//    val partidosDeHoy =
//        remember(matchesDate) {
//            matchesDate.filter { match ->
//                val matchDate =
//                    Instant
//                        .parse(match.date)
//                        .atZone(ZoneId.systemDefault())
//                        .toLocalDate()
//                matchDate == LocalDate.now()
//            }
//        }

    // agrupar por dia
//    val partidosPorDia =
//        partidosDeHoy.groupBy { match ->
//            val dateTime =
//                Instant
//                    .parse(match.date)
//                    .atZone(ZoneId.systemDefault())
//            dateTime.format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM", Locale("es")))
//        }
    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) Color(0xFF27292D) else Color.White

    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = AppConstants.CARD_HORIZONTAL_PADDING.dp, vertical = 4.dp),
        shape = RoundedCornerShape(36.dp),
        // elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(bottom = 16.dp),
        ) {
            HeaderCard(
                rightIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                title = "Partidos de hoy",
                subTitle = "",
                onClick = onClick,
            )
//            Text(
//                text = "PARTIDOS DE HOY",
//                style = MaterialTheme.typography.titleLarge,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 12.dp),
//            )

            when {
                isLoading -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularWavyProgressIndicator()
                    }
                }

                partidosPorDia.isEmpty() -> {
                    Text(
                        "NO HAY PARTIDOS HOY",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }

                else -> {
                    partidosPorDia.forEach { (dia, partidos) ->
                        Text(
                            text = dia.uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 6.dp),
                        )
                        partidos.forEach { partido ->
                            PartidoRow(match = partido)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PartidoRow(match: Match) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Hora o estado
        if (match.statusMatchId != 1) {
            StatusChip(statusName(match.statusMatchId))
        } else {
            Text(formatFechaHora(match.date), style = MaterialTheme.typography.bodyMedium)
        }

        // Local
        TeamColumn1(name = match.localName, pictureUrl = match.localPictureUrl)

        // Visitante
        TeamColumn1(name = match.visitorName, pictureUrl = match.visitorPictureUrl)
    }
}

@Composable
fun TeamColumn1(
    name: String,
    pictureUrl: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            model = pictureUrl,
            contentDescription = name,
            modifier =
                Modifier
                    .size(40.dp)
                    .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun StatusChip(status: String) {
    val color =
        when (status.lowercase()) {
            "en juego" -> Color(0xFF4CAF50) // verde
            "finalizado" -> Color(0xFFFF9800) // naranja
            else -> Color(0xFF2196F3) // azul
        }
    Surface(
        color = color,
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
    ) {
        Text(
            status,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}
