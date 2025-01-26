package io.github.kaczmarek.ipcalculator.feature.calculator.presentation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultCalculatorComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, CalculatorComponent {

    override val uiState = MutableStateFlow(CalculatorUiState())

    override fun onShareClick() {

    }
}