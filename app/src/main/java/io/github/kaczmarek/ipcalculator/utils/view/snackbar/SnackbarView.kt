package io.github.kaczmarek.ipcalculator.utils.view.snackbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.google.android.material.snackbar.ContentViewCallback
import com.google.android.material.textview.MaterialTextView
import io.github.kaczmarek.ipcalculator.R

class SnackbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), ContentViewCallback {
    init {
        View.inflate(context, R.layout.layout_view_snackbar, this)
    }

    override fun animateContentIn(delay: Int, duration: Int) {}

    override fun animateContentOut(delay: Int, duration: Int) {}

    fun setMessage(message: String) {
        val tvMessage = findViewById<MaterialTextView>(R.id.tv_top_snackbar_message)
        tvMessage.text = message
    }
}