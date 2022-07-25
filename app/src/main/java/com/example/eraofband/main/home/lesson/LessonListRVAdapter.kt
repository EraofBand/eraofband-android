package com.example.eraofband.main.home.lesson

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemLessonBinding

class LessonListRVAdapter : RecyclerView.Adapter<LessonListRVAdapter.ViewHolder>() {
    private var lessonList = arrayListOf<Band>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowDetail(lessonIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLessonList(lessonList : List<Band>) {
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
        holder.binding.lessonLayout.setOnClickListener { mItemClickListener.onShowDetail(position) }  // 나중에는 레슨 아이디를 넣어서 정보 연동
    }
    override fun getItemCount(): Int = lessonList.size

    inner class ViewHolder(val binding: ItemLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lessonList: Band) {
            binding.lessonImgIv.setImageResource(R.drawable.band_profile)
            binding.lessonImgIv.clipToOutline = true  // 모서리 깎기

            binding.lessonTitleTv.text = "제목입니다"
            binding.lessonIntroduceTv.text = "소개입니다"
        }
    }
}