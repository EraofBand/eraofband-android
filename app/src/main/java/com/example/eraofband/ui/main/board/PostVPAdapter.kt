package com.example.eraofband.ui.main.board

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eraofband.databinding.ItemPostPictureBinding

class PostVPAdapter(private val context: Context): RecyclerView.Adapter<PostVPAdapter.ViewHolder>() {
    private val image = arrayListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun addImage(image: String) {
        this.image.add(image)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPostPictureBinding = ItemPostPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(image[position])
    }

    override fun getItemCount(): Int = image.size

    inner class ViewHolder(val binding: ItemPostPictureBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(picture: String) {
            Glide.with(context).load(picture).into(binding.postPictureIv)
            binding.postPictureIv.clipToOutline = true
        }
    }
}