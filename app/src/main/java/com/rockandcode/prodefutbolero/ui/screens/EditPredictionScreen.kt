package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rockandcode.prodefutbolero.domain.prediction.models.Prediction
import com.rockandcode.prodefutbolero.ui.components.AppHeader

@Composable
fun EditPredictionScreen(
    prediction: Prediction,
    onBack: () -> Unit,
    onSave: (Int, Int) -> Unit, // callback para guardar los goles
    viewModel: MyPredictionsViewModel = hiltViewModel(),
) {
    // var localGoals by remember { mutableStateOf(prediction.localGoals) }
    // var visitorGoals by remember { mutableStateOf(prediction.visitorGoals) }
    val localGoals by viewModel.localGoals.collectAsState()
    val visitorGoals by viewModel.visitorGoals.collectAsState()

    Scaffold(
        topBar = {
            AppHeader(
                title = "Predicciónes",
                subTitle = "Realizar predicción",
                onBack = onBack,
                showBackButton = true,
            )
        },
    ) { padding ->
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(500)),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                // 🟦 Sección 1: Datos del partido
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text("Datos del partido", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp))
                    InfoLine(label = "Estado", value = prediction.statusMatchId.toString())
                    InfoLine(label = "Fecha", value = prediction.date)
                    InfoLine(label = "Local", value = prediction.localName)
                    InfoLine(label = "Visitante", value = prediction.visitorName)
                }

                // 🟨 Sección 2: Mi Pronóstico
                Text(
                    text = "Mi Pronóstico",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp),
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    // Nombres e imágenes (opcional)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        TeamWithScoreNullable(
                            teamName = prediction.localName,
                            pictureUrl = prediction.localPictureUrl,
                            goals = localGoals,
                            onIncrement = { viewModel.setLocalGoals((localGoals ?: -1) + 1) },
                            onDecrement = {
                                if ((localGoals ?: 1) > 0) viewModel.setLocalGoals((localGoals ?: 1) - 1)
                            },
                        )

                        Text("vs", style = MaterialTheme.typography.titleLarge)

                        TeamWithScoreNullable(
                            teamName = prediction.visitorName,
                            pictureUrl = prediction.visitorPictureUrl,
                            goals = visitorGoals,
                            onIncrement = { viewModel.setVisitorGoals((visitorGoals ?: -1) + 1) },
                            onDecrement = {
                                if ((visitorGoals ?: 1) > 0) viewModel.setVisitorGoals((visitorGoals ?: 1) - 1)
                            },
                        )
                    }
                }
                // Solo mostrar si ambos son no nulos
                Button(
                    onClick = {
                        onSave(localGoals!!, visitorGoals!!)
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = localGoals != null && visitorGoals != null,
                ) {
                    Text("Guardar predicción")
                }
            }
        }
    }
}

@Composable
fun TeamWithScoreNullable(
    teamName: String,
    pictureUrl: String,
    goals: Int?,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        AsyncImage(
            model = pictureUrl,
            contentDescription = teamName,
            modifier = Modifier.size(64.dp).clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.height(8.dp))

        Text(teamName, style = MaterialTheme.typography.labelMedium)

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDecrement) {
                Icon(Icons.Default.Remove, contentDescription = "Menos")
            }

            Text(
                goals?.toString() ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.width(24.dp),
                textAlign = TextAlign.Center,
            )

            IconButton(onClick = onIncrement) {
                Icon(Icons.Default.Add, contentDescription = "Más")
            }
        }
    }
}

@Composable
fun InfoLine(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
