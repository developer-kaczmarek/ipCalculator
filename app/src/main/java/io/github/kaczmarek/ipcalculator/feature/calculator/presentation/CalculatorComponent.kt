package io.github.kaczmarek.ipcalculator.feature.calculator.presentation

import kotlinx.coroutines.flow.StateFlow

interface CalculatorComponent {

   val uiState: StateFlow<CalculatorUiState>

   fun onShareClick()
}