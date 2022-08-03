package com.example.eraofband.ui.main.home.lessonlike

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemLessonBinding
import com.example.eraofband.remote.lesson.getLikeLessonList.GetLessonLikeListResult

class HomeLessonLikeRVAdapter(list: List<GetLessonLikeListResult>) :
    RecyclerView.Adapter<HomeLessonLikeRVAdapter.ViewHolder>() {
    private var lessonLikeList = list
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    interface MyItemClickListener {
        fun onShowDetail(lessonIdx: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLessonBinding = ItemLessonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeLessonLikeRVAdapter.ViewHolder, position: Int) {
        holder.bind(lessonLikeList[position])

        holder.binding.lessonLayout.setOnClickListener {  // 레슨 상세정보
           mItemClickListener.onShowDetail(lessonLikeList[position].lessonIdx)
        }
    }
    override fun getItemCount(): Int = lessonLikeList.size

    inner class ViewHolder(val binding: ItemLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(lessonList: GetLessonLikeListResult) {

            Glide.with(itemView).load(lessonList.lessonImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.lessonImgIv) // 레슨 이미지

            binding.lessonImgIv.clipToOutline = true  // 이미지 모서리 라운딩

            binding.lessonRegionTv.text = lessonList.lessonRegion
            binding.lessonTitleTv.text = lessonList.lessonTitle
            binding.lessonIntroduceTv.text = lessonList.lessonIntroduction
            binding.lessonMemberCntTv.text = "${lessonList.memberCount}/${lessonList.capacity}"
        }
    }
}