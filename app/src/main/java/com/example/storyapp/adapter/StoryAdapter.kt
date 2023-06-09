package com.example.storyapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.example.storyapp.R
import com.example.storyapp.data.remote.response.ListStory
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.DetailActivity

class StoryAdapter(private val listStory: ArrayList<ListStory>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listStory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvItemName.text = holder.itemView.context.getString(R.string.posted_by, listStory[position].name)

        Glide.with(holder.itemView.context)
            .load(listStory[position].photoUrl)
            .format(DecodeFormat.PREFER_RGB_565)
            .into(holder.binding.ivItemPhoto)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_STORY, listStory[holder.adapterPosition])
            holder.itemView.context.startActivity(intent)
        }
    }

}