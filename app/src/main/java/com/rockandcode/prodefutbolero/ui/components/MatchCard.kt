package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.domain.match.models.Match
import com.rockandcode.prodefutbolero.utils.formatFechaHora
import com.rockandcode.prodefutbolero.utils.statusName

@Composable
fun MatchCard(
    modifier: Modifier = Modifier,
    match: Match,
    onClick: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) Color(0xFF27292D) else Color.White

//    val shadowAmbient = if (isDark) Color(0x22FFFFFF) else Color(0x22000000)
//    Card(
//        modifier =
//            modifier
//                .fillMaxWidth()
//                .clickable(onClick = onClick)
//                .padding(horizontal = 16.dp, vertical = 4.dp)
//                .shadow(
//                    elevation = 4.dp,
//                    shape = RoundedCornerShape(36.dp),
//                    ambientColor = shadowAmbient,
//                    spotColor = shadowAmbient,
//                ),
//        shape = RoundedCornerShape(36.dp),
//        colors = CardDefaults.cardColors(containerColor = cardColor),
//    ) {
//    ElevatedCard(
//        elevation =
//            CardDefaults.cardElevation(
//                defaultElevation = 12.dp,
//            ),
//        colors = CardDefaults.cardColors(containerColor = cardColor),
//        shape = RoundedCornerShape(36.dp),
//        modifier =
//            Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 4.dp),
//    ) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(36.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        onClick = onClick,
    ) {
        // Contenido principal: logos + estado + goles + fecha
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Local
            TeamLogoAndName(
                name = match.localName,
                imageUrl = match.localPictureUrl,
            )

            // Centro: estado, goles, fecha
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = statusName(match.statusMatchId),
                    style = MaterialTheme.typography.labelSmall,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${match.localGoals ?: ""}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "vs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${match.visitorGoals ?: ""}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    text = formatFechaHora(match.date),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Visitante
            TeamLogoAndName(
                name = match.visitorName,
                imageUrl = match.visitorPictureUrl,
            )
        }
        // Pronósticos
//        if (match.predictions > 0) {
//            Spacer(modifier = Modifier.height(10.dp))
//            Text(
//                text = "Pronósticos: ${match.predictions}",
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.secondary,
//            )
//        }
    }
}

@Composable
fun TeamLogoAndName(
    name: String,
    imageUrl: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp), // Limita el ancho para no desbalancear
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Escudo $name",
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
