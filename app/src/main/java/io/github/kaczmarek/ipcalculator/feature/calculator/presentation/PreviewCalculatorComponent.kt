package io.github.kaczmarek.ipcalculator.feature.calculator.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewCalculatorComponent : CalculatorComponent {

    override val uiState: StateFlow<CalculatorUiState> = MutableStateFlow(CalculatorUiState())

    override fun onShareClick() = Unit
}