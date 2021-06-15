package io.github.kaczmarek.ipcalculator

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        snrCIDRPrefix = findViewById(R.id.snr_main_mask)
        val adapter = SubnetMasksArrayAdapter(
            this,
            R.layout.custom_spinner_layout,
            resources.getStringArray(R.array.subnet_masks_prefix).toList(),
            resources.getStringArray(R.array.subnet_masks).toList()
        )
        snrCIDRPrefix.adapter = adapter
        etArrayIpAddress = arrayOf(
            findViewById(R.id.et_main_first_octet),
            findViewById(R.id.et_main_second_octet),
            findViewById(R.id.et_main_third_octet),
            findViewById(R.id.et_main_fourth_octet)
        )
        clContainer = findViewById(R.id.cl_main_container)
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

        nsvIpInfoContainer.visibility = View.GONE

        setListenerEditableViews()

        ViewCompat.setOnApplyWindowInsetsListener(clContainer) { _, insets ->
            val sysBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            clContainer.updatePadding(0, 0, 0, sysBarsInsets.bottom)
            tvIpAddressTitle.updatePadding(0, sysBarsInsets.top, 0, 0)
            insets
        }

        val btnCalculate = findViewById<MaterialButton>(R.id.btn_main_calculate)
        btnCalculate.setOnClickListener {
            calculate()
        }
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
                            it.length == 3 -> moveCursorToNextOctet(index)
                            it.length > 3 -> {
                                val lastChar = it.toString()[it.length - 1]
                                it.delete(it.length - 1, it.length)
                                moveCursorToNextOctetAndSetChar(index, lastChar)
                            }
                        }
                    }
                }
                setOnEditorActionListener { v, actionId, _ ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_NEXT -> moveCursorToNextOctet(index)
                        EditorInfo.IME_ACTION_DONE -> v.clearFocus()
                    }
                    false
                }
                setOnKeyListener { _, keyCode, _ ->
                    if (keyCode == KeyEvent.KEYCODE_DEL && editText.text.isNullOrEmpty()) {
                        moveCursorToForwardOctet(index)
                    }
                    false
                }
            }
        }
    }

    private fun moveCursorToNextOctet(currentIndex: Int) {
        val nextIndex = currentIndex + 1
        if (nextIndex < etArrayIpAddress.size) {
            val nextOctet = etArrayIpAddress[nextIndex]
            with(nextOctet) {
                setSelection(selectionStart, selectionStart)
                requestFocus()
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
        }
    }

    private fun calculate() {
        try {
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

    companion object {
        const val TAG = "MainActivity"
    }
}