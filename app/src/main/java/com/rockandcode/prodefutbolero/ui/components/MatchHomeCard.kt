package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rockandcode.prodefutbolero.domain.match.models.Match
import com.rockandcode.prodefutbolero.utils.formatFechaHora
import com.rockandcode.prodefutbolero.utils.statusName

@Composable
fun MatchHomeCard(
    match: Match,
    onClick: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) Color(0xFF27292D) else Color.White

    val chipColor =
        when (match.statusMatchId) {
            1 -> Color(0xFF9E9E9E) // No inicia (gris)
            3 -> Color(0xFF4CAF50) // En juego (verde)
            2 -> Color(0xFFEA622C) // Finalizado (rojo)
            // 2 -> Color(0xFFF44336)
            4 -> Color(0xFF4CAF50)
            else -> Color(0xFF9E9E9E)
        }

    val groupText =
        if (match.groupName.contains("Grupo", ignoreCase = true)) {
            match.groupName
        } else {
            "Grupo ${match.groupName}"
        }

    Box {
        Card(
            modifier =
                Modifier
                    .width(320.dp),
            shape = RoundedCornerShape(36.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            onClick = onClick,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Local
                    TeamLogoAndNameColumn(
                        name = match.localName,
                        imageUrl = match.localPictureUrl,
                    )
                    Text(
                        text = "${match.localGoals ?: ""}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "vs",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "${match.visitorGoals ?: ""}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    // Visitante
                    TeamLogoAndNameColumn(
                        name = match.visitorName,
                        imageUrl = match.visitorPictureUrl,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.weight(1f))
                // Row inferior con fondo
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(
                                if (isDark) Color(0xFF2E3134) else MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(24.dp),
                            ).padding(vertical = 8.dp, horizontal = 12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = groupText,
                            style = MaterialTheme.typography.labelSmall,
                        )
                        Text(
                            text = formatFechaHora(match.date),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }
        Surface(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 8.dp),
            // .offset(y = (-12).dp, x = 12.dp),
            shape = RoundedCornerShape(50),
            color = chipColor,
            shadowElevation = 4.dp,
        ) {
            Text(
                text = statusName(match.statusMatchId),
                modifier =
                    Modifier
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}
