package io.github.kaczmarek.ipcalculator.feature.settings.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.kaczmarek.ipcalculator.R
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.Language
import io.github.kaczmarek.ipcalculator.feature.settings.domain.model.ThemeType
import io.github.kaczmarek.ipcalculator.ui.theme.AppTheme
import io.github.kaczmarek.ipcalculator.ui.widget.HeadlineItem

@Composable
fun SettingsScreen(
    component: SettingsComponent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        val uiState: SettingsUiState by component.uiState.collectAsStateWithLifecycle()

        HeadlineItem(
            text = stringResource(id = R.string.settings_language),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 8.dp),
        )

        LocaleRadioGroup(
            availableLanguages = uiState.availableLanguages,
            onClick = { component.onLanguageItemClick(it) },
            modifier = Modifier.fillMaxWidth(),
        )

        HeadlineItem(
            text = stringResource(id = R.string.settings_theme),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 24.dp, bottom = 8.dp),
        )

        ThemeRadioGroup(
            availableThemes = uiState.availableThemes,
            onClick = { component.onThemeItemClick(it) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}



@Composable
private fun RadioOption(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton,
            )
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
            text = text,
            style = MaterialTheme.typography.bodyLarge.merge(),
        )
        RadioButton(
            colors = RadioButtonDefaults.colors().copy(
                selectedColor = MaterialTheme.colorScheme.secondary,
            ),
            selected = selected,
            onClick = null,
        )
    }
}

@Composable
private fun LocaleRadioGroup(
    availableLanguages: List<SettingsUiState.LanguageItem>,
    onClick: (language: Language) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
        ) {
            availableLanguages.forEachIndexed { index, item ->
                RadioOption(
                    selected = item.selected,
                    text = stringResource(id = item.titleRes),
                    onClick = { onClick(item.language) },
                    modifier = Modifier.fillMaxWidth(),
                )
                if (index != availableLanguages.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Composable
private fun ThemeRadioGroup(
    availableThemes: List<SettingsUiState.ThemeItem>,
    onClick: (themeType: ThemeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
        ) {
            availableThemes.forEachIndexed { index, item ->
                RadioOption(
                    selected = item.selected,
                    text = stringResource(id = item.titleRes),
                    onClick = { onClick(item.themeType) },
                    modifier = Modifier.fillMaxWidth(),
                )
                if (index != availableThemes.lastIndex) {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(PreviewSettingsComponent())
    }
}
