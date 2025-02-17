package io.github.kaczmarek.ipcalculator.feature.calculator.presentation

import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PreviewCalculatorComponent : CalculatorComponent {

    override val uiState: StateFlow<CalculatorUiState> = MutableStateFlow(CalculatorUiState())

    override fun onCalculateClick() = Unit

    override fun onShareClick() = Unit

    override fun onOctetChange(index: Int, value: TextFieldValue) = Unit

    override fun onOctetDeleteImeClick(index: Int) = Unit

    override fun onOctetNextImeActionClick(index: Int) = Unit

    override fun onOctetFocusChange(index: Int) = Unit

    override fun onCIDRPrefixClick() = Unit
}