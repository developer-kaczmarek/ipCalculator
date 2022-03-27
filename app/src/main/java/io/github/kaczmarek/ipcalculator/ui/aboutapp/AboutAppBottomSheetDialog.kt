package io.github.kaczmarek.ipcalculator.ui.aboutapp

import android.app.Dialog
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import io.github.kaczmarek.ipcalculator.R
import io.github.kaczmarek.ipcalculator.utils.view.snackbar.TopSnackbar

class AboutAppBottomSheetDialog : BottomSheetDialogFragment() {
    lateinit var clContainer: CoordinatorLayout

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = View.inflate(requireContext(), R.layout.bottomsheet_fragment_about_app, null)
        with(dialog) {
            setContentView(view)
            setOnShowListener {
                val bottomSheetDialog = it as BottomSheetDialog
                val parentLayout = bottomSheetDialog.findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet
                )
                parentLayout?.let { bottomSheet ->
                    val behaviour = BottomSheetBehavior.from(bottomSheet)
                    setupFullHeight(bottomSheet)
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    behaviour.skipCollapsed = true
                }
            }
        }
        clContainer = view.findViewById(R.id.cl_about_app_container)
        val ivBackButton = view.findViewById<ImageView>(R.id.iv_about_app_back)
        val tvAppVersion = view.findViewById<MaterialTextView>(R.id.tv_about_app_version)
        val tvFeedback = view.findViewById<MaterialTextView>(R.id.tv_about_app_feedback)
        val tvShowCode = view.findViewById<MaterialTextView>(R.id.tv_about_app_show_code)
        val tvShowPrivacyPolicy =
            view.findViewById<MaterialTextView>(R.id.tv_about_app_show_privacy_policy)
        val tvRateApp = view.findViewById<MaterialTextView>(R.id.tv_about_app_rate)

        tvAppVersion.text = getAppVersion(view.context.packageName)
        ivBackButton.setOnClickListener {
            dismiss()
        }
        tvFeedback.setOnClickListener {
            sendFeedback()
        }
        tvShowCode.setOnClickListener {
            openUrl(getString(R.string.fragment_about_app_url_github))
        }
        tvShowPrivacyPolicy.setOnClickListener {
            openUrl(getString(R.string.fragment_about_app_url_privacy_policy))
        }
        tvRateApp.setOnClickListener {
            searchAppInGooglePlay()
        }

        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    private fun searchAppInGooglePlay() {
        val packageName = activity?.packageName
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        getString(
                            R.string.fragment_about_app_search_in_google_play,
                            packageName
                        )
                    )
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        getString(
                            R.string.fragment_about_app_search_in_browser,
                            packageName
                        )
                    )
                )
            )
        }
    }

    private fun sendFeedback() {
        val email = getString(R.string.fragment_about_app_email_for_feedback)
        val emailIntent = Intent(
            Intent.ACTION_SENDTO,
            Uri.fromParts("mailto", email, null)
        )
        if (context?.packageManager?.resolveActivity(emailIntent, 0) != null) {
            startActivity(
                Intent.createChooser(
                    emailIntent,
                    getString(R.string.fragment_about_app_mail_chooser_title)
                )
            )
        } else {
            val clipboard = context?.getSystemService(
                Context.CLIPBOARD_SERVICE
            ) as? ClipboardManager
            val clip = ClipData.newPlainText(null, email)
            clipboard?.setPrimaryClip(clip)

            TopSnackbar.make(
                clContainer,
                getString(R.string.fragment_about_app_app_for_intent_mail_to_not_found),
                Snackbar.LENGTH_LONG
            )?.show()
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (context?.packageManager?.resolveActivity(intent, 0) != null) {
            startActivity(intent)
        } else {
            TopSnackbar.make(
                clContainer,
                getString(R.string.fragment_about_app_app_for_intent_not_found),
                Snackbar.LENGTH_LONG
            )?.show()
        }
    }

    @Suppress("DEPRECATION")
    private fun getAppVersion(packageName: String): String {
        val packageInfo = context?.packageManager?.getPackageInfo(packageName, 0)
        val versionCode: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo?.longVersionCode.toString()
        } else {
            packageInfo?.versionCode.toString()
        }
        return getString(R.string.fragment_about_app_version, packageInfo?.versionName, versionCode)
    }

    companion object {
        const val TAG = "AboutAppBottomSheetDialog"
    }
}