package com.example.scratchcardexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scratchcardexample.feature.startscreen.StartScreen
import com.example.scratchcardexample.ui.theme.ScratchCardExampleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavHost()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppNavHost() {
    val navController = rememberNavController()

    ScratchCardExampleTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = ROUTE_START,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(ROUTE_START) {
                    StartScreen(
                        innerPadding = innerPadding,
                        onNavigateToScratchScreen = {
                            navController.navigate(ROUTE_SCRATCH)
                        },
                        onNavigateToActivationScreen = {
                            navController.navigate(ROUTE_ACTIVATION)
                        }
                    )
                }
            }
        }
    }
}