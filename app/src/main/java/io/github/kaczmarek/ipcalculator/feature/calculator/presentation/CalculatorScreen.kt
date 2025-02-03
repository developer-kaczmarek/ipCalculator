package io.github.kaczmarek.ipcalculator.feature.calculator.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kaczmarek.ipcalculator.common.ui.theme.AppTheme

@Composable
fun CalculatorScreen(
    component: CalculatorComponent,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = "Calculator Tab")
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CalculatorScreenPreview() {
    AppTheme {
        CalculatorScreen(PreviewCalculatorComponent())
    }
}
