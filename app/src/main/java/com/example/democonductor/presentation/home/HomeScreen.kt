package com.example.democonductor.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.democonductor.domain.model.ThemeMode
import com.example.democonductor.ui.theme.DemoConductorTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeContent(
        state = state,
        onIntent = viewModel::handleIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Demo Text Section
            Text(
                text = "Welcome to DemoConductor",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "This is a demo application",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Built with Clean Architecture & MVI Pattern",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Theme Toggle Button
            Button(
                onClick = { onIntent(HomeIntent.ToggleThemeSelector) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle theme"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Current Theme: ${state.currentTheme.displayName}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Click the button to change theme",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Theme Selector Bottom Sheet
        if (state.showThemeSelector) {
            ModalBottomSheet(
                onDismissRequest = { onIntent(HomeIntent.DismissThemeSelector) },
                sheetState = sheetState
            ) {
                ThemeSelectorContent(
                    availableThemes = state.availableThemes,
                    currentTheme = state.currentTheme,
                    onThemeSelected = { theme ->
                        onIntent(HomeIntent.SelectTheme(theme))
                    }
                )
            }
        }
    }
}

@Composable
private fun ThemeSelectorContent(
    availableThemes: List<ThemeMode>,
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "Select Theme",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        availableThemes.forEach { theme ->
            ThemeItem(
                themeMode = theme,
                isSelected = theme == currentTheme,
                onClick = { onThemeSelected(theme) }
            )
        }
    }
}

@Composable
private fun ThemeItem(
    themeMode: ThemeMode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = themeMode.displayName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    DemoConductorTheme {
        HomeContent(
            state = HomeState(),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ThemeSelectorPreview() {
    DemoConductorTheme {
        ThemeSelectorContent(
            availableThemes = ThemeMode.entries,
            currentTheme = ThemeMode.LIGHT,
            onThemeSelected = {}
        )
    }
}