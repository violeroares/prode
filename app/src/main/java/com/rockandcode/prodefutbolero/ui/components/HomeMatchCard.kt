package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.domain.tournament.models.Match
import com.rockandcode.prodefutbolero.utils.formatFechaHora

@Composable
fun HomeMatchCard(
    match: Match,
    modifier: Modifier = Modifier,
) {
    val isDark = isSystemInDarkTheme()
    // val cardColor = if (isDark) Color(0xFF2E3034) else Color.White
    val cardColor = if (isDark) Color(0xFF17181C) else Color.White
    val shadowAmbient = if (isDark) Color(0x22FFFFFF) else Color(0x22000000)
    val shadowSpot = shadowAmbient

    Card(
        modifier =
            modifier
                .width(280.dp)
                .height(180.dp)
                .shadow(
                    elevation = 0.dp,
                    shape = RoundedCornerShape(36.dp),
                    ambientColor = shadowAmbient,
                    spotColor = shadowSpot,
                ),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Box {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = formatFechaHora(match.date),
                    // color = Color.LightGray,
                    fontSize = 14.sp,
                    // modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Text(
                    text = "En juego",
                    // color = Color.LightGray,
                    fontSize = 14.sp,
                    // modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
            // Score row
            Row(
                modifier =
                    modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                // Card Total Puntaje
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                ) {
                    TeamItem(name = match.localName, pictureUrl = match.localPictureUrl)
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Row(
                            modifier =
                                modifier
                                    .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "${match.localGoals ?: ""}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )

                            Text(
                                text = "vs",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center,
                            )

                            Text(
                                text = "${match.visitorGoals ?: ""}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                            )
                        }
                        Row(
                            modifier =
                                modifier
                                    .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Text("Grupo A")
                        }
                    }
                }

                // Card Total Puntaje
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                ) {
                    TeamItem(name = match.visitorName, pictureUrl = match.visitorPictureUrl)
                }
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .background(Color.Black.copy(alpha = 0.4f))
                        .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "${match.localName} vs ${match.visitorName}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White),
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun TeamItem(
    name: String,
    pictureUrl: String,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = pictureUrl,
            contentDescription = name,
            modifier =
                Modifier
                    .size(48.dp),
            contentScale = ContentScale.Crop,
        )
//        Spacer(Modifier.height(4.dp))
//        Text(
//            text = name,
//            style = MaterialTheme.typography.labelMedium,
//            textAlign = TextAlign.Center,
//            color = if (isDark) Color(0xFFB3B3B3) else Color.DarkGray,
//            modifier = Modifier.fillMaxWidth(),
//        )
    }
}
