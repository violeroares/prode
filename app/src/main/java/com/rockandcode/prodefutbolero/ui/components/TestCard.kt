package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.domain.user.models.User

@Composable
fun TestCard(
    user: User?,
    modifier: Modifier = Modifier,
) {
    val isDark = isSystemInDarkTheme()
    val cardColor = if (isDark) Color(0xFF27292D) else Color.White
    val shadowAmbient = if (isDark) Color(0x22FFFFFF) else Color(0x22000000)
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(36.dp),
                    ambientColor = shadowAmbient,
                    spotColor = shadowAmbient,
                ),
        shape = RoundedCornerShape(36.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Column {
            Row(
                modifier = modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = user?.avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(48.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )

                Text(
                    text = "${user?.firstName} ${user?.lastName}",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                )

//            HeaderCard(
//                rightIcon = Icons.AutoMirrored.Filled.ArrowForward,
//                title = "Calendario",
//                subTitle = "Partidos de la fecha",
//                onClick = { onMoreClick() },
//            )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(8.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(32.dp),
                            ambientColor = shadowAmbient,
                            spotColor = shadowAmbient,
                        ),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1FD72)),
            ) {
                Text("Test card", color = Color.Black, modifier = Modifier.padding(16.dp))
            }
        }
    }
}
