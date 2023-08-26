package io.github.kaczmarek.ipcalculator.utils.view.snackbar

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import io.github.kaczmarek.ipcalculator.R
import io.github.kaczmarek.ipcalculator.utils.dpToPx

class TopSnackbar(
    parent: ViewGroup,
    customView: SnackbarView
) : BaseTransientBottomBar<TopSnackbar>(parent, customView, customView) {
    init {
        animationMode = ANIMATION_MODE_FADE
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP

        ViewCompat.setElevation(view, context.dpToPx(ELEVATION))

        view.setBackgroundResource(R.drawable.layer_list_snackbar_background)
    }

    companion object {
        private const val ELEVATION = 6f

        fun make(
            view: ViewGroup,
            text: String,
            @Duration duration: Int
        ): TopSnackbar? {
            return try {
                val customView = LayoutInflater.from(view.context).inflate(
                    R.layout.layout_top_snackbar,
                    view,
                    false
                ) as SnackbarView

                customView.setMessage(text)

                TopSnackbar(view, customView).setDuration(duration)
            } catch (e: Exception) {
                Log.v("TopSnackbar ", e.message ?: "")
                null
            }
        }
    }
}