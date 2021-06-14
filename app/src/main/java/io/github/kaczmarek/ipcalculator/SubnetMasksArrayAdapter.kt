package io.github.kaczmarek.ipcalculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SubnetMasksArrayAdapter(
    context: Context,
    resource: Int,
    private val prefixes: List<String>,
    private val fullMasks: List<String>
) : ArrayAdapter<String>(context, resource, prefixes) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_spinner_layout, parent, false)

        val textView = view.findViewById<TextView>(R.id.tv_simple_spinner_item)
        textView.text = prefixes[position]
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.subnet_mask_dialog_item, parent, false)

        val textView = view.findViewById<TextView>(R.id.tv_subnet_mask_dialog_item)
        textView.text = fullMasks[position]
        return view
    }
}