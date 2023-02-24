package com.example.splashit.presentation.usercollection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.splashit.R
import com.example.splashit.databinding.CollectionsPhotoItemBinding
import com.example.splashit.domain.models.CollectionPhotos

class UserCollectionPageAdapter(
    private val likeOnClick: (CollectionPhotos, Int) -> Unit,
    private val imageOnClick: (String) -> Unit
) :
    PagingDataAdapter<CollectionPhotos, UserCollectionPageAdapter.ViewHolder>(
        DiffUtilCallback()
    ) {

    class ViewHolder(val binding: CollectionsPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class DiffUtilCallback : DiffUtil.ItemCallback<CollectionPhotos>() {
        override fun areItemsTheSame(
            oldItem: CollectionPhotos,
            newItem: CollectionPhotos
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CollectionPhotos,
            newItem: CollectionPhotos
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        item?.let {
            holder.binding.userName.text = item.userName
            holder.binding.userUrl.text = item.userNick
            holder.binding.numberOfDownloads.text = " (${item.totalDownloads.toString()})"
            holder.binding.numberOfLikes.text = it.totalLikes.toString()

            Glide
                .with(holder.binding.userAvatar.context)
                .load(it.profilePhoto)
                .circleCrop()
                .into(holder.binding.userAvatar)

            Glide
                .with(holder.binding.userImage.context)
                .load(it.photo)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.binding.userImage)
        }

        holder.binding.userImage.setOnClickListener {
            item?.let {
                imageOnClick(item.id)
            }
        }

        holder.binding.toggleButton.setOnClickListener {
            item?.let {
                likeOnClick(item, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CollectionsPhotoItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(binding)
    }


}