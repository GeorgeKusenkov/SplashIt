package com.example.splashit.presentation.main

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.splashit.R
import com.example.splashit.databinding.MainListPhotoItemBinding
import com.example.splashit.domain.repository.Launch

class UsersPhotoPageAdapter(
    private val likeOnClick: (Launch, Int) -> Unit,
    private val imageOnClick: (String) -> Unit

): PagingDataAdapter<Launch, UsersPhotoPageAdapter.ViewHolder>(DiffUtilCallback()) {
    class ViewHolder(val binding: MainListPhotoItemBinding): RecyclerView.ViewHolder(binding.root)

    class DiffUtilCallback: DiffUtil.ItemCallback<Launch>() {
        override fun areItemsTheSame(oldItem: Launch, newItem: Launch): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Launch, newItem: Launch): Boolean {
            return oldItem.equals(newItem)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.userName.text = item?.name ?: ""
        holder.binding.userUrl.text = item?.username ?: ""
        holder.binding.numberOfLikes.text = item?.likes.toString()
        holder.binding.toggleButton.isChecked = item?.likedByUser == true
        holder.binding.userImage.setOnClickListener {
            item?.let {
                imageOnClick(item.id)
            }
        }

        val drawable = CircularProgressDrawable(holder.binding.userImage.context)
        drawable.setColorSchemeColors(
            R.color.purple_200,
            R.color.purple_500,
            R.color.teal_700
        )
        drawable.centerRadius = 30f
        drawable.strokeWidth = 5f
        drawable.start()

        holder.binding.toggleButton.setOnClickListener {
            item?.let {
                likeOnClick(item, position)
            }
        }

        holder.binding.userImage.adjustViewBounds
        item?.let {
            Glide
                .with(holder.binding.userAvatar.context)
                .load(it.profileImage)
                .circleCrop()
                .into(holder.binding.userAvatar)

            Glide
                .with(holder.binding.userImage.context)
                .load(it.photo)
                .placeholder(drawable)
                .override(200, 300)
                .error(R.drawable.placeholder_no_image)
                .into(holder.binding.userImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MainListPhotoItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(binding)
    }
}
