package com.example.splashit.presentation.curentuser.likedphotos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.splashit.R
import com.example.splashit.databinding.UserLikedPhotoItemBinding
import com.example.splashit.domain.models.CurrentUserLikedPhotos

class LikedPhotosPageAdapter(private val imageOnClick: (String) -> Unit):
    PagingDataAdapter<CurrentUserLikedPhotos, LikedPhotosPageAdapter.ViewHolder>(DiffUtilCallback()) {

    class ViewHolder(val binding: UserLikedPhotoItemBinding): RecyclerView.ViewHolder(binding.root)

    class DiffUtilCallback: DiffUtil.ItemCallback<CurrentUserLikedPhotos>() {
        override fun areItemsTheSame(oldItem: CurrentUserLikedPhotos, newItem: CurrentUserLikedPhotos): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: CurrentUserLikedPhotos, newItem: CurrentUserLikedPhotos): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
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
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LikedPhotosPageAdapter.ViewHolder {
        val binding = UserLikedPhotoItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(binding)
    }
}