package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rockandcode.prodefutbolero.ui.components.AppHeader

@Composable
fun MyPredictionsScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            AppHeader(
                title = "Mis predicciones",
                onBack = { navController.popBackStack() },
                showBackButton = true,
            )
        },
        contentWindowInsets = WindowInsets(0),
        containerColor = Color.Transparent,
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = paddingValues,
        ) {
        }
    }
}
