package io.github.kaczmarek.ipcalculator.utils

import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import io.github.kaczmarek.ipcalculator.R

fun Context.dpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}

/**
 * Данный метод помогает красить статус бар в полупрозрачный цвет
 */
@Suppress("DEPRECATION")
fun Window.updateStatusBar() {
    apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setDecorFitsSystemWindows(false)
            insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            decorView.systemUiVisibility = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or 0
                else -> View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
        statusBarColor =  ContextCompat.getColor(context, R.color.BackgroundSystemBarsColor)
    }
}