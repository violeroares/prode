package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rockandcode.prodefutbolero.domain.prediction.models.Ranking

@Composable
fun RankingCard(
    user: Ranking,
    modifier: Modifier = Modifier,
) {
    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) Color(0xFF27292D) else Color.White
    val shadowAmbient = if (isDark) Color(0x22FFFFFF) else Color(0x22000000)
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(36.dp),
                    ambientColor = shadowAmbient,
                    spotColor = shadowAmbient,
                ),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Número de posición con fondo color
            Box(
                modifier =
                    Modifier
                        .size(48.dp)
                        .background(Color(0xFFA9F582), shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = user.posicion.toString(),
                    style =
                        MaterialTheme.typography.titleLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                        ),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "${user.puntos} puntos",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                )
            }

            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Puntos",
                tint = Color(0xFFA9F582),
                modifier = Modifier.size(28.dp),
            )
        }
    }
}
