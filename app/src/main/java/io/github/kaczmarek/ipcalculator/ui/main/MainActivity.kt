package io.github.kaczmarek.ipcalculator.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.*
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import io.github.kaczmarek.ipcalculator.R
import io.github.kaczmarek.ipcalculator.utils.manager.IpManager
import io.github.kaczmarek.ipcalculator.utils.updateStatusBar
import io.github.kaczmarek.ipcalculator.utils.view.snackbar.TopSnackbar
import io.github.kaczmarek.ipcalculator.utils.view.spinner.SubnetMasksArrayAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var etArrayIpAddress: Array<TextInputEditText>
    private lateinit var snrCIDRPrefix: Spinner
    private lateinit var tvIpAddressValue: MaterialTextView
    private lateinit var tvCidrPrefixValue: MaterialTextView
    private lateinit var tvSubnetMaskValue: MaterialTextView
    private lateinit var tvWildcardMaskValue: MaterialTextView
    private lateinit var tvNetworkIpAddressValue: MaterialTextView
    private lateinit var tvBroadcastIpAddressValue: MaterialTextView
    private lateinit var tvCountPossibleHostsValue: MaterialTextView
    private lateinit var tvCountUsableHostsValue: MaterialTextView
    private lateinit var tvFirstHostIpAddressValue: MaterialTextView
    private lateinit var tvLastHostIpAddressValue: MaterialTextView
    private lateinit var clContainer: ConstraintLayout
    private lateinit var tvIpAddressTitle: MaterialTextView
    private lateinit var nsvIpInfoContainer: NestedScrollView
    private lateinit var clEmptyPlaceholder: ConstraintLayout
    private lateinit var cvControlPanel: MaterialCardView
    private lateinit var btnCalculate: MaterialButton
    private lateinit var clSnackbarContainer: CoordinatorLayout
    private var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.updateStatusBar()
        snrCIDRPrefix = findViewById(R.id.snr_main_mask)
        val adapter = SubnetMasksArrayAdapter(
            this,
            R.layout.layout_subnet_mask_spinner_main_item,
            resources.getStringArray(R.array.subnet_masks).toList(),
        )
        snrCIDRPrefix.adapter = adapter
        etArrayIpAddress = arrayOf(
            findViewById(R.id.et_main_first_octet),
            findViewById(R.id.et_main_second_octet),
            findViewById(R.id.et_main_third_octet),
            findViewById(R.id.et_main_fourth_octet)
        )
        clContainer = findViewById(R.id.cl_main_container)
        clSnackbarContainer = findViewById(R.id.cl_main_snackbar_container)
        nsvIpInfoContainer = findViewById(R.id.nsv_main_ip_info_container)
        tvIpAddressTitle = findViewById(R.id.tv_main_ip_address_title)
        tvIpAddressValue = findViewById(R.id.tv_main_ip_address)
        tvCidrPrefixValue = findViewById(R.id.tv_main_cidr_prefix)
        tvSubnetMaskValue = findViewById(R.id.tv_main_subnet_mask)
        tvWildcardMaskValue = findViewById(R.id.tv_main_wildcard_mask)
        tvNetworkIpAddressValue = findViewById(R.id.tv_main_network_ip_address)
        tvBroadcastIpAddressValue = findViewById(R.id.tv_main_broadcast_ip_address)
        tvCountPossibleHostsValue = findViewById(R.id.tv_main_count_possible_hosts)
        tvCountUsableHostsValue = findViewById(R.id.tv_main_count_usable_hosts)
        tvFirstHostIpAddressValue = findViewById(R.id.tv_main_first_host)
        tvLastHostIpAddressValue = findViewById(R.id.tv_main_last_host)
        clEmptyPlaceholder = findViewById(R.id.cl_empty_placeholder_container)
        cvControlPanel = findViewById(R.id.cv_main_control_panel)
        btnCalculate = findViewById(R.id.btn_main_calculate)
        nsvIpInfoContainer.visibility = View.GONE
        snrCIDRPrefix.setSelection(24) // Установка по умолчанию 24 маски

        setListenerEditableViews()

        ViewCompat.setOnApplyWindowInsetsListener(clContainer) { _, insets ->
            val sysBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            if (imeVisible) {
                val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                clContainer.updatePadding(0, 0, 0, imeHeight)
            } else {
                currentFocus?.clearFocus()
                clContainer.updatePadding(0, 0, 0, sysBarsInsets.bottom)
            }
            tvIpAddressTitle.updatePadding(0, sysBarsInsets.top, 0, 0)
            clSnackbarContainer.updatePadding(0, sysBarsInsets.top, 0, 0)
            insets
        }

        btnCalculate.setOnClickListener {
            if (isSomeFieldsEmpty()) {
                TopSnackbar.make(
                    clSnackbarContainer,
                    getString(R.string.main_activity_calculate_some_fields_empty),
                    Snackbar.LENGTH_LONG
                )?.show()
            } else {
                calculate()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onGlobalLayoutListener?.let {
            clContainer.viewTreeObserver.removeOnGlobalLayoutListener(it)
        }
        onGlobalLayoutListener = null
    }

    private fun setListenerEditableViews() {
        etArrayIpAddress.forEachIndexed { index, editText ->
            with(editText) {
                addTextChangedListener { view ->
                    view?.let {
                        when {
                            it.toString().endsWith(getString(R.string.common_dot)) -> {
                                it.delete(it.length - 1, it.length)
                                moveCursorToNextOctet(index)
                            }
                            it.toString().isNotEmpty() && it.toString().toInt() > 255 -> {
                                val lastChar = it.toString()[it.length - 1]
                                it.delete(it.length - 1, it.length)
                                moveCursorToNextOctetAndSetChar(index, lastChar)
                            }
                            it.length == 3 -> moveCursorToNextOctet(index)
                            it.length > 3 -> {
                                val lastChar = it.toString()[it.length - 1]
                                it.delete(it.length - 1, it.length)
                                moveCursorToNextOctetAndSetChar(index, lastChar)
                            }
                        }
                    }
                }
                setOnEditorActionListener { _, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_NEXT -> moveCursorToNextOctet(index)
                        EditorInfo.IME_ACTION_DONE -> hideKeyboard()
                    }

                    false
                }
                setOnKeyListener { _, keyCode, _ ->
                    if (keyCode == KeyEvent.KEYCODE_DEL && editText.text.isNullOrEmpty()) {
                        moveCursorToForwardOctet(index)
                    }
                    false
                }
                customSelectionActionModeCallback = object : ActionMode.Callback {
                    override fun onCreateActionMode(
                        mode: ActionMode?,
                        menu: Menu?
                    ): Boolean {
                        return false
                    }

                    override fun onPrepareActionMode(
                        mode: ActionMode?,
                        menu: Menu?
                    ): Boolean {
                        return false
                    }

                    override fun onActionItemClicked(
                        mode: ActionMode?,
                        item: MenuItem?
                    ): Boolean {
                        return false
                    }

                    override fun onDestroyActionMode(mode: ActionMode?) {}
                }
            }
        }
    }

    private fun moveCursorToNextOctet(currentIndex: Int) {
        val nextIndex = currentIndex + 1
        if (nextIndex < etArrayIpAddress.size) {
            val nextOctet = etArrayIpAddress[nextIndex]
            with(nextOctet) {
                requestFocus()
                setSelection(selectionStart, selectionStart)
            }
        }
    }

    private fun moveCursorToNextOctetAndSetChar(currentIndex: Int, char: Char) {
        val nextIndex = currentIndex + 1
        if (nextIndex < etArrayIpAddress.size) {
            val nextOctet = etArrayIpAddress[nextIndex]
            with(nextOctet) {
                setText(char.toString())
                val position = text?.length ?: 0
                setSelection(position, position)
                requestFocus()
            }
        }
    }

    private fun moveCursorToForwardOctet(currentIndex: Int) {
        val forwardIndex = currentIndex - 1
        if (forwardIndex >= 0) {
            val forwardOctet = etArrayIpAddress[forwardIndex]
            with(forwardOctet) {
                setSelection(selectionStart, selectionStart)
                requestFocus()
            }
        } else {
            hideKeyboard()
        }
    }

    private fun isSomeFieldsEmpty(): Boolean {
        val emptyFields = etArrayIpAddress.filter {
            it.text.isNullOrEmpty()
        }
        return emptyFields.isNotEmpty()
    }

    private fun calculate() {
        try {
            hideKeyboard()
            val octets = arrayListOf<Int>()
            etArrayIpAddress.map {
                octets.add(it.text.toString().toInt())
            }
            val prefix = snrCIDRPrefix.selectedItemPosition
            val ipManager = IpManager(octets, prefix)
            tvIpAddressValue.text = octets.toString()
            tvCidrPrefixValue.text = prefix.toString()
            tvSubnetMaskValue.text = ipManager.getSubnetMask()
            tvWildcardMaskValue.text = ipManager.getWildcardMask()
            tvNetworkIpAddressValue.text = ipManager.getNetworkIpAddress()
            tvBroadcastIpAddressValue.text = ipManager.getBroadcastIpAddress()
            tvCountPossibleHostsValue.text = ipManager.getCountMaxPossibleHosts().toString()
            tvCountUsableHostsValue.text = ipManager.getCountUsableHosts().toString()
            tvFirstHostIpAddressValue.text = ipManager.getFirstUsableHost()
            tvLastHostIpAddressValue.text = ipManager.getLastUsableHost()
            nsvIpInfoContainer.visibility = View.VISIBLE
            clEmptyPlaceholder.visibility = View.GONE
        } catch (e: Exception) {
            nsvIpInfoContainer.visibility = View.GONE
            clEmptyPlaceholder.visibility = View.VISIBLE
            Log.e(TAG, "e = ${e.message}")
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        currentFocus?.clearFocus()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}