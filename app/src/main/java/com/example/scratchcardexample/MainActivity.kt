package com.example.scratchcardexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.scratchcardexample.feature.activation.ActivationScreen
import com.example.scratchcardexample.feature.scratchscreen.ScratchScreen
import com.example.scratchcardexample.feature.startscreen.StartScreen
import com.example.scratchcardexample.ui.theme.ScratchCardExampleTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.State

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
private fun AppNavHost(
    viewModel: MainActivityViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    ScratchCardExampleTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AppBar(
                    titleId = viewModel.header,
                    onBack = navController::navigateUp,
                    showBackButton = viewModel.showBackButton
                )
            },
            ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = ROUTE_START,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(ROUTE_START) {
                    StartScreen(
                        onStart = {
                            viewModel.updateHeader(ROUTE_START)
                        },
                        innerPadding = innerPadding,
                        onNavigateToScratchScreen = {
                            navController.navigate(ROUTE_SCRATCH)
                        },
                        onNavigateToActivationScreen = {
                            navController.navigate(ROUTE_ACTIVATION)
                        }
                    )
                }
                composable(ROUTE_ACTIVATION) {
                    ActivationScreen(
                        onStart = {
                            viewModel.updateHeader(ROUTE_ACTIVATION)
                        },
                        //innerPadding = innerPadding,
                    )
                }
                composable(ROUTE_SCRATCH) {
                    ScratchScreen(
                        onStart = {
                            viewModel.updateHeader(ROUTE_SCRATCH)
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    titleId: State<Int>,
    onBack: () -> Unit,
    showBackButton: Boolean) {
    TopAppBar(
        title = {
            Text(text = stringResource(titleId.value))
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
    )
}