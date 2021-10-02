package com.yudev.tesandroiddevkaspin.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewAdapter<T, V : ViewBinding>(var listItem: MutableList<T>) :
    RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private var onItemSelectedListener: ((T, Int) -> Unit)? = null
    lateinit var layout: V
    abstract fun setViewBinding(layoutInflater: LayoutInflater, parent: ViewGroup): V
    abstract fun bind(item: T, view: V, position: Int)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        layout = setViewBinding(LayoutInflater.from(parent.context), parent)
        return ViewHolder(layout.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(listItem[position], layout, holder.adapterPosition)
        onItemSelectedListener?.let {
            holder.itemView.setOnClickListener { v ->
                it(listItem[holder.adapterPosition], holder.adapterPosition)
            }
        }
    }

    override fun getItemCount() = listItem.size

}