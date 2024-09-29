package com.example.weatherapp.AppViews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FavitemBinding
import com.example.weatherapp.databinding.ItemdailyBinding
import com.example.weatherapp.forcastmodel.Favorites


class FavAdp(val addalert:(item:Favorites)->Unit,val e:(item:Favorites)->Unit,val ed:(item:Favorites)->Unit) : ListAdapter<Favorites, FavAdp.VH, >(object : DiffUtil.ItemCallback<Favorites>() {
    override fun areItemsTheSame(oldItem: Favorites, newItem: Favorites): Boolean {
        return oldItem==newItem
    }
    override fun areContentsTheSame(oldItem: Favorites, newItem: Favorites): Boolean {
        return oldItem==newItem
    }
}) {
    class VH(private val itemTimeBinding: FavitemBinding) : RecyclerView.ViewHolder(itemTimeBinding.root) {
        fun bind( addalert:(item:Favorites)->Unit,list: Favorites, e:(item:Favorites)->Unit,ed:(item:Favorites)->Unit) {
            itemTimeBinding.viewmodel = list
            itemTimeBinding.imageButton.setOnClickListener {
                e(list)
            }
            itemTimeBinding.root.setOnClickListener {
                ed(list)
            }
            itemTimeBinding.imageButton2.setOnClickListener {
                addalert(list)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(FavitemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(addalert,getItem(position),e,ed)
    }
}