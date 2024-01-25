package com.github.snuffix.flickrapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.snuffix.flickrapp.databinding.ItemFlickrBinding
import com.github.snuffix.flickrapp.repository.FlickrItem

class FlickItemsAdapter(
    private val onItemClick: (String) -> Unit
) :
    ListAdapter<FlickrItem, FlickItemsAdapter.ViewHolder>(FlickrItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFlickrBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFlickrBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FlickrItem) {
            binding.root.setOnClickListener { onItemClick.invoke(item.imageUrl) }

            with(binding.imageView) {
                isInvisible = true
                load(item.imageUrl) {
                    listener { _, _ -> isVisible = true }
                }
            }
        }
    }
}

internal class FlickrItemDiffCallback : DiffUtil.ItemCallback<FlickrItem>() {
    override fun areItemsTheSame(oldItem: FlickrItem, newItem: FlickrItem): Boolean {
        return oldItem.imageUrl == newItem.imageUrl
    }

    override fun areContentsTheSame(oldItem: FlickrItem, newItem: FlickrItem): Boolean {
        return oldItem.imageUrl == newItem.imageUrl && newItem.description == oldItem.description
    }
}