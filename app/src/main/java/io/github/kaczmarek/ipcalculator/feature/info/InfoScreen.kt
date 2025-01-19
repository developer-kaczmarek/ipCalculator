package io.github.kaczmarek.ipcalculator.feature.info

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kaczmarek.ipcalculator.ui.theme.AppTheme

@Composable
fun InfoScreen(
    component: InfoComponent,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = "Info Tab")
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun InfoScreenPreview() {
    AppTheme {
        InfoScreen(PreviewInfoComponent())
    }
}
