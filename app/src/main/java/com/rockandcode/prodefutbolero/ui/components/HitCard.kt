package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.domain.prediction.models.Hit

@Composable
fun HitCard(
    hit: Hit,
    isDark: Boolean,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(36.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors =
            CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF2C2C2C) else Color.White),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Local team
                TeamColumn(hit.localPictureUrl, hit.localName)

                // Real vs Predicted
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${hit.realLocalGoals} - ${hit.realVisitorGoals}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isDark) Color.White else Color.Black,
                    )
                    Text(
                        text = "Pred: ${hit.myLocalGoals} - ${hit.myVisitorGoals}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) Color.Gray else Color.DarkGray,
                    )
                }

                // Visitor team
                TeamColumn(hit.visitorPictureUrl, hit.visitorName)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "${hit.dateName} | ${hit.groupName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) Color.Gray else Color.DarkGray,
                )
                Text(
                    text = "${hit.points} pts",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color =
                        if (hit.points > 0) {
                            Color(0xFF4CAF50)
                        } else if (isDark) {
                            Color.White
                        } else {
                            Color.Black
                        },
                )
            }
        }
    }
}

@Composable
fun TeamColumn(
    pictureUrl: String,
    name: String,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = pictureUrl,
            contentDescription = name,
            modifier =
                Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
            contentScale = ContentScale.Crop,
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
