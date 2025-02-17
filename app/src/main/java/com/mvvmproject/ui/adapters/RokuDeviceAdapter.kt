package com.mvvmproject.ui.adapters

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mvvmproject.domain.RokuDevice

// RokuDeviceAdapter.kt
class RokuDeviceAdapter(private val onDeviceClick: (RokuDevice) -> Unit) :
    RecyclerView.Adapter<RokuDeviceAdapter.DeviceViewHolder>() {

    private val devices = mutableListOf<RokuDevice>()

    class DeviceViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
        }
        return DeviceViewHolder(textView)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = devices[position]
        holder.view.text = device.name
        holder.view.setOnClickListener { onDeviceClick(device) }
    }

    override fun getItemCount() = devices.size

    fun updateDevices(newDevices: List<RokuDevice>) {
        devices.clear()
        devices.addAll(newDevices)
        notifyDataSetChanged()
    }
}