package io.github.kaczmarek.ipcalculator

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import io.github.kaczmarek.ipcalculator.feature.root.presentation.DefaultRootComponent
import io.github.kaczmarek.ipcalculator.feature.root.presentation.RootScreen
import io.github.kaczmarek.ipcalculator.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableRealEdgeToEdge()
        super.onCreate(savedInstanceState)

        val rootComponent = DefaultRootComponent(componentContext = defaultComponentContext())

        setContent {
            AppTheme {
                RootScreen(component = rootComponent, modifier = Modifier.fillMaxSize())
            }
        }
    }

    private fun enableRealEdgeToEdge() {
        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
    }
}
