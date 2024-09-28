package com.example.weatherapp.AppViews

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ItemtimeBinding

import com.example.weatherapp.forcastmodel.List

class TodayAdapter:ListAdapter<List,TodayAdapter.VH,>(object : DiffUtil.ItemCallback<List>() {
    override fun areItemsTheSame(oldItem: List, newItem: List): Boolean {
        return oldItem==newItem
    }
    override fun areContentsTheSame(oldItem: List, newItem: List): Boolean {
        return oldItem==newItem
    }
})
{
    class VH(val itemTimeBinding: ItemtimeBinding): RecyclerView.ViewHolder(itemTimeBinding.root){
        fun bind(list: List){
            Log.d("fasdsadadasdasaddasdasdasTAG", "bind: ${list.weather[0].description}")
            itemTimeBinding.viewModel=list
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
       return VH(ItemtimeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}