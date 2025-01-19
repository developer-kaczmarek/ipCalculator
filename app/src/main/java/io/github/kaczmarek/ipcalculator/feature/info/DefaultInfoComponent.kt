package io.github.kaczmarek.ipcalculator.feature.info

import com.arkivanov.decompose.ComponentContext

class DefaultInfoComponent(
    componentContext: ComponentContext,
) : ComponentContext by componentContext, InfoComponent