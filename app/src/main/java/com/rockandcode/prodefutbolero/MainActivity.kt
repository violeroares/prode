package com.rockandcode.prodefutbolero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.rockandcode.prodefutbolero.ui.navigation.AppStack
import com.rockandcode.prodefutbolero.ui.navigation.AuthStack
import com.rockandcode.prodefutbolero.ui.screens.MainViewModel
import com.rockandcode.prodefutbolero.ui.theme.ProdeFutboleroTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            ProdeFutboleroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val controller = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    val mainViewModel: MainViewModel = hiltViewModel()
    val isAuthenticated by mainViewModel.isAuthenticated.collectAsState()

    if (isAuthenticated) {
        AppStack(
            navController = controller,
            snackbarHostState = snackbarHostState,
            mainViewModel = mainViewModel,
        )
    } else {
        AuthStack(
            navController = controller,
        )
    }
}
