package com.example.eraofband.ui.main.home.session.band.album

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.databinding.ItemBandAlbumBinding

class BandAlbumRVAdapter(private val context: Context) : RecyclerView.Adapter<BandAlbumRVAdapter.ViewHolder>() {
    private var albumList = arrayListOf<String>()

    interface MyItemClickListener {
        // 클릭 이벤트
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initAlbum(albumList : List<String>) {
        this.albumList.addAll(albumList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBandAlbumBinding = ItemBandAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])

        // 클릭 이벤트
    }
    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemBandAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: String) {
            // 글라이드를 이용한 프로필사진 연동
//            Glide.with(context).load(member.profileImgUrl).into(binding.bandAlbumPictureIv)

        }
    }
}