package com.example.eraofband.ui.main.home.lesson

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemLessonBinding
import com.example.eraofband.remote.lesson.getLessonList.GetLessonListResult

class LessonListRVAdapter : RecyclerView.Adapter<LessonListRVAdapter.ViewHolder>() {
    private var lessonList = arrayListOf<GetLessonListResult>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowDetail(lessonIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLessonList(lessonList : List<GetLessonListResult>) {
        this.lessonList.addAll(lessonList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLessonBinding = ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lessonList[position])

        // 클릭 이벤트
        holder.binding.lessonLayout.setOnClickListener { mItemClickListener.onShowDetail(lessonList[position].lessonIdx) }  // 나중에는 레슨 인덱스를 넣어서 정보 연동
    }
    override fun getItemCount(): Int = lessonList.size

    inner class ViewHolder(val binding: ItemLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(lessonList: GetLessonListResult) {
            Glide.with(itemView).load(lessonList.lessonImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.lessonImgIv) // 레슨 이미지
            binding.lessonImgIv.clipToOutline = true  // 모서리 깎기

            binding.lessonRegionTv.text = lessonList.lessonRegion
            binding.lessonTitleTv.text = lessonList.lessonTitle
            binding.lessonIntroduceTv.text = lessonList.lessonIntroduction
            binding.lessonMemberCntTv.text = "${lessonList.memberCount} / ${lessonList.capacity}"
        }
    }
}