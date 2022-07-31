package com.example.eraofband.ui.main.home.lessonlike

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemLessonBinding


class HomeLessonLikeRVAdapter(private var likeLessonList: ArrayList<Band>) :
    RecyclerView.Adapter<HomeLessonLikeRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLessonBinding = ItemLessonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeLessonLikeRVAdapter.ViewHolder, position: Int) {
        holder.bind(likeLessonList[position])
    }
    override fun getItemCount(): Int = likeLessonList.size

    inner class ViewHolder(val binding: ItemLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(band: Band) {
            binding.lessonImgIv.setImageResource(0)
            binding.lessonTitleTv.text = "찜 레슨의 타이틀"
            binding.lessonIntroduceTv.text = "찜 레슨의 한 줄 소개"
            binding.lessonRegionTv.text = "강남구"
            binding.lessonMemberCntTv.text = "4/5"
        }
    }
}