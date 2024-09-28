package com.example.weatherapp.AppViews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.alerts.MyAlerts
import com.example.weatherapp.databinding.AlarmitemBinding

class AlertIems ( val ed:(item: MyAlerts)->Unit) : ListAdapter<MyAlerts, AlertIems.VH, >(object : DiffUtil.ItemCallback<MyAlerts>() {
    override fun areItemsTheSame(oldItem: MyAlerts, newItem: MyAlerts): Boolean {
        return oldItem==newItem
    }
    override fun areContentsTheSame(oldItem: MyAlerts, newItem: MyAlerts): Boolean {
        return oldItem==newItem
    }
}) {
    class VH(private val itemTimeBinding: AlarmitemBinding) : RecyclerView.ViewHolder(itemTimeBinding.root) {
        fun bind(list: MyAlerts, ed:(item: MyAlerts)->Unit) {
            itemTimeBinding.viewmodel = list
            itemTimeBinding.imageButton.setOnClickListener {
                ed(list)
            }
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(AlarmitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position),ed)
    }
}