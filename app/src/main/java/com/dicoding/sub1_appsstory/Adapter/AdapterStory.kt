package com.dicoding.sub1_appsstory.Adapter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.sub1_appsstory.Data.ListStoryItem
import com.dicoding.sub1_appsstory.Detail.DetailStoryActivity
import com.dicoding.sub1_appsstory.Utils.DateConverter
import com.dicoding.sub1_appsstory.databinding.StoryItemBinding

class AdapterStory : PagingDataAdapter<ListStoryItem, AdapterStory.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Storydata = getItem(position)
        if (Storydata != null) {
            holder.bindd(Storydata)
        }
    }

    class ViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bindd(dataa: ListStoryItem) {
            binding.username.text = dataa.name
            Glide.with(itemView.context)
                .load(dataa.photoUrl)
                .into(binding.photo)
            binding.date.text = dataa.createdAt?.let { DateConverter(it) }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra("id", dataa.id)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.photo, "photo"),
                        Pair(binding.username, "name"),
                        Pair(binding.description, "desc"),
                        Pair(binding.date, "date")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())


            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areItemsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}