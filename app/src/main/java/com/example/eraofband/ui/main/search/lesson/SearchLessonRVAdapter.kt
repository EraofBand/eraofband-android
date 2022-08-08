package com.example.eraofband.ui.main.search.lesson

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemSearchBandBinding
import com.example.eraofband.remote.search.getLesson.GetSearchLessonResult

class SearchLessonRVAdapter : RecyclerView.Adapter<SearchLessonRVAdapter.ViewHolder>() {
    private var lessonList = arrayListOf<GetSearchLessonResult>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun lessonInfo(lessonIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLessonList(lesson : List<GetSearchLessonResult>) {
        this.lessonList.addAll(lesson)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<GetSearchLessonResult>) {
        lessonList = filteredList as ArrayList<GetSearchLessonResult>
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        lessonList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchLessonRVAdapter.ViewHolder {
        val binding: ItemSearchBandBinding = ItemSearchBandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchLessonRVAdapter.ViewHolder, position: Int) {
        holder.bind(lessonList[position])

        holder.binding.searchBandCl.setOnClickListener {
            mItemClickListener.lessonInfo(lessonList[position].lessonIdx)
        }
    }
    override fun getItemCount(): Int = lessonList.size

    inner class ViewHolder(val binding: ItemSearchBandBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: GetSearchLessonResult) {
            Glide.with(itemView).load(list.lessonImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.searchBandImgIv)
            binding.searchBandImgIv.clipToOutline = true

            binding.searchBandNameTv.text = list.lessonTitle
            binding.searchBandRegionTv.text = list.lessonRegion
            binding.searchBandMemberCntTv.text = "${list.memberCount} / ${list.capacity}"


        }
    }
}