package com.rockandcode.prodefutbolero.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rockandcode.prodefutbolero.utils.AppConstants

@Composable
fun IncompleteCard(
    modifier: Modifier = Modifier,
    value: Int,
    dateName: String,
    onClick: () -> Unit = {},
) {
    val cardColor = Color(0xFFF1FD72)

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = AppConstants.CARD_HORIZONTAL_PADDING.dp, vertical = 4.dp),
        shape = RoundedCornerShape(36.dp),
        // elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(bottom = 8.dp),
        ) {
            HeaderCardInverted(
                // leftIcon = Icons.Outlined.CalendarToday,
                // rightIcon = Icons.AutoMirrored.Filled.ArrowForward,
                rightIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                title = "¡Comenzó la fecha!",
                subTitle = dateName,
                onClick = onClick,
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Tienes $value partidos por completar",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "!Apurate antes que comiencen!",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF565F1E),
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
