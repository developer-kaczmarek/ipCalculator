package io.github.kaczmarek.ipcalculator.feature.info.presentation

import io.github.kaczmarek.ipcalculator.common.model.link.AppLinkType

interface InfoComponent {

    sealed interface Output {

        class OpenLink(val linkType: AppLinkType) : Output

        class ShareText(val text: String) : Output

        data object RateApp : Output
    }

    fun onOpenGithubPageClick()

    fun onReadPrivacyPolicyClick()

    fun onContactDeveloperClick()

    fun onRateAppClick()

    fun onShareAppClick(text: String)
}