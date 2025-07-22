package com.rockandcode.prodefutbolero.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.rockandcode.prodefutbolero.ui.components.AppHeader

@Composable
fun InstructionsScreen(navController: NavController) {
    Column(
        modifier =
            Modifier
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppHeader(title = "Instrucciones", onBack = { navController.popBackStack() })
    }
}
