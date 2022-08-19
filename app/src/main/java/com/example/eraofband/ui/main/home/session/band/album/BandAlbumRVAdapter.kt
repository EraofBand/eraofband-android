package com.example.eraofband.ui.main.home.session.band.album

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemBandAlbumBinding
import com.example.eraofband.remote.band.getAlbumBand.GetAlbumBandResult
import com.example.eraofband.remote.band.getLikedBand.GetLikedBandResult

class BandAlbumRVAdapter : RecyclerView.Adapter<BandAlbumRVAdapter.ViewHolder>() {
    private var albumList = arrayListOf<GetAlbumBandResult>()

    @SuppressLint("NotifyDataSetChanged")
    fun initAlbum(albumList : List<GetAlbumBandResult>) {
        this.albumList.addAll(albumList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBandAlbumBinding = ItemBandAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
    }
    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemBandAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: GetAlbumBandResult) {
            Glide.with(itemView).load(album.albumImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.bandAlbumPictureIv)
            binding.bandAlbumTitleTv.text = album.albumTitle
            binding.bandAlbumDateTv.text = album.albumDate
        }
    }
}