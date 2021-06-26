package io.github.kaczmarek.ipcalculator.utils.view.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import io.github.kaczmarek.ipcalculator.R

class SubnetMasksArrayAdapter(
    context: Context,
    resource: Int,
    private val fullMasks: List<String>
) : ArrayAdapter<String>(context, resource, fullMasks) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.layout_subnet_mask_spinner_main_item, parent, false)

        val textView = view.findViewById<TextView>(R.id.tv_simple_spinner_item)
        textView.text = position.toString()
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.layout_subnet_mask_spinner_dialog_item, parent, false)

        val textView = view.findViewById<TextView>(R.id.tv_subnet_mask_dialog_item)
        textView.text = fullMasks[position]
        return view
    }
}