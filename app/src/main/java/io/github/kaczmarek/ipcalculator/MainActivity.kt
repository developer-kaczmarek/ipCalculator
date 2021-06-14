package io.github.kaczmarek.ipcalculator

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var etArrayIpAddress: Array<EditText>
    private lateinit var snrCIDRPrefix: Spinner
    private lateinit var resNetworkTextView: TextView
    private lateinit var resMaskTextView: TextView
    private lateinit var resWildcardMaskTextView: TextView
    private lateinit var resFirstHostTextView: TextView
    private lateinit var resLastHostTextView: TextView
    private lateinit var resBroadcastTextView: TextView
    private lateinit var resNetSizeTextView: TextView

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
        resNetworkTextView = findViewById(R.id.resNetworkTextView)
        resMaskTextView = findViewById(R.id.resMaskTextView)
        resWildcardMaskTextView = findViewById(R.id.resWildcardMaskTextView)
        resFirstHostTextView = findViewById(R.id.resFirstHostTextView)
        resLastHostTextView = findViewById(R.id.resLastHostTextView)
        resBroadcastTextView = findViewById(R.id.resBroadcastTextView)
        resNetSizeTextView = findViewById(R.id.resNetSizeTextView)
        setListenerEditableViews()
        val btnCalculate = findViewById<MaterialButton>(R.id.btn_main_calculate)
        btnCalculate.setOnClickListener {
            calculate()
        }
    }

    private fun setListenerEditableViews() {
        etArrayIpAddress.forEachIndexed { index, editText ->
            editText.addTextChangedListener { view ->
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
            editText.setOnEditorActionListener { v, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_NEXT -> moveCursorToNextOctet(index)
                    EditorInfo.IME_ACTION_DONE -> v.clearFocus()
                }
                false
            }
            editText.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_DEL && editText.text.isEmpty()) {
                    moveCursorToForwardOctet(index)
                }
                false
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
                setSelection(text.length, text.length)
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
            resNetworkTextView.text = octets.toString()
            resMaskTextView.text = ipManager.getSubnetMask()
            resWildcardMaskTextView.text = ipManager.getWildcardMask()
            resFirstHostTextView.text = ipManager.getFirstUsableHost()
            resLastHostTextView.text = ipManager.getLastUsableHost()
            resBroadcastTextView.text = ipManager.getBroadcastIpAddress()
            resNetSizeTextView.text = ipManager.getCountUsableHosts().toString()
            Log.i(TAG, "Префикс маски подсети: /${prefix}")
            Log.i(TAG, "Маска подсети: ${ipManager.getSubnetMask()}")
            Log.i(TAG, "Обратная маска подсети (wildcard mask): ${ipManager.getWildcardMask()}")
            Log.i(TAG, "IP адрес сети: ${ipManager.getNetworkIpAddress()}")
            Log.i(TAG, "Широковещательный адрес: ${ipManager.getBroadcastIpAddress()}")
            Log.i(
                TAG,
                "Количество доступных адресов в порции хоста: ${ipManager.getCountMaxPossibleHosts()}"
            )
            Log.i(TAG, "Количество рабочих адресов для хостов: ${ipManager.getCountUsableHosts()}")
            Log.i(TAG, "IP адрес первого хоста: ${ipManager.getFirstUsableHost()}")
            Log.i(TAG, "IP адрес последнего хоста: ${ipManager.getLastUsableHost()}")
        } catch (e: Exception) {
            Log.e(TAG, "e = ${e.message}")
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}