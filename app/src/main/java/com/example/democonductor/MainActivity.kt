package com.example.democonductor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.democonductor.domain.model.ThemeMode
import com.example.democonductor.presentation.home.HomeScreen
import com.example.democonductor.presentation.home.HomeViewModel
import com.example.democonductor.ui.theme.DemoConductorTheme
import com.example.democonductor.ui.theme.toAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by homeViewModel.state.collectAsStateWithLifecycle()

            DemoConductorTheme(
                theme = state.currentTheme.toAppTheme()
            ) {
                HomeScreen()
            }
        }
    }
}