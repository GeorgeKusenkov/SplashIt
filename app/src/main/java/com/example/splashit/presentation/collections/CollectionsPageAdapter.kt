package com.example.splashit.presentation.collections

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.splashit.databinding.CollectionsListItemBinding
import com.example.splashit.domain.models.CollectionsList

class CollectionsPageAdapter(private val itemOnClick: (CollectionInfoForDetails) -> Unit) :
    PagingDataAdapter<CollectionsList, CollectionsPageAdapter.ViewHolder>(DiffUtilCallback()) {
    class ViewHolder(val binding: CollectionsListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class DiffUtilCallback : DiffUtil.ItemCallback<CollectionsList>() {
        override fun areItemsTheSame(oldItem: CollectionsList, newItem: CollectionsList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CollectionsList,
            newItem: CollectionsList
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.numbersOfPhoto.text = "${item.totalPhotos.toString()} ФОТОГРАФИЙ"
            holder.binding.collectionsName.text = item.collectionsName
            holder.binding.userName.text = item.username
            holder.binding.userUrl.text = item.user
            Glide
                .with(holder.binding.collectionsImage.context)
                .load(it.coverPhoto)
                .into(holder.binding.collectionsImage)

            Glide
                .with(holder.binding.userAvatar.context)
                .load(it.profileImage)
                .circleCrop()
                .into(holder.binding.userAvatar)
        }

        holder.binding.collectionsImage.setOnClickListener {
            item?.let {
                val collectionInfoForDetails = CollectionInfoForDetails(
                    id = item.id,
                    title = item.collectionsName,
                    tags = item.tags,
                    description = item.description,
                    author = item.username,
                    totalPhotos = item.totalPhotos,
                    coverPhoto = item.coverPhoto
                )
                itemOnClick(collectionInfoForDetails)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CollectionsListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }
}