package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun HeaderCardInverted(
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
    title: String,
    subTitle: String = "",
    onClick: () -> Unit,
) {
    val isDark = isSystemInDarkTheme()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(4.dp),
    ) {
        if (leftIcon != null) {
            Box(
                modifier =
                    Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onClick)
                        .background(
                            if (isDark) Color(0xFF2E3134) else MaterialTheme.colorScheme.background,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    leftIcon,
                    contentDescription = "Icono",
                    tint = if (isDark) Color(0xFFA2F7A1) else Color.Black,
                )
            }
        }
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                // fontSize = 22.sp,
                // fontWeight = FontWeight.Medium,
                color = if (isDark) Color(0xFF27300A) else Color.Black,
            )
            if (subTitle.isNotEmpty()) {
                Text(
                    text = subTitle.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isDark) Color(0xFF565F1E) else Color.DarkGray,
                )
            }
        }
        if (rightIcon != null) {
            Box(
                modifier =
                    Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onClick)
                        .background(
                            if (isDark) Color(0xFFDDE869) else MaterialTheme.colorScheme.background,
                            shape = CircleShape,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    rightIcon,
                    contentDescription = "Icono",
                    // tint = if (isDark) Color(0xFFA9F582) else Color.Black,
                    tint = if (isDark) Color(0xFF27300A) else Color.Black,
                )
            }
        }
    }
}
