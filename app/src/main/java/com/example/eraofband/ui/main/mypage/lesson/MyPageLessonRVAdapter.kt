package com.example.eraofband.ui.main.mypage.lesson

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eraofband.databinding.ItemMypageBandBinding
import com.example.eraofband.remote.user.getMyPage.GetUserLesson

class MyPageLessonRVAdapter(private val context: Context) : RecyclerView.Adapter<MyPageLessonRVAdapter.ViewHolder>() {
    private var lessonList = arrayListOf<GetUserLesson>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowDetail(lessonIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initLessonList(lessonList : List<GetUserLesson>) {
        this.lessonList.addAll(lessonList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMypageBandBinding = ItemMypageBandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lessonList[position])

        // 클릭 이벤트
        holder.binding.bandListLayout.setOnClickListener { mItemClickListener.onShowDetail(lessonList[position].lessonIdx) }  // 나중에는 밴드 아이디를 넣어서 정보 연동
    }
    override fun getItemCount(): Int = lessonList.size

    inner class ViewHolder(val binding: ItemMypageBandBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lesson: GetUserLesson) {
            Glide.with(context).load(lesson.lessonImgUrl).into(binding.bandListImgIv)
            binding.bandListImgIv.clipToOutline = true  // 모서리 깎기

            binding.bandListRegionTv.text = lesson.lessonRegion
            binding.bandListTitleTv.text = lesson.lessonTitle
            binding.bandListIntroduceTv.text = lesson.lessonIntroduction
        }
    }
}