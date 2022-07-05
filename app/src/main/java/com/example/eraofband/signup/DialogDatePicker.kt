package com.example.eraofband.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogDatepickerBinding
import java.text.SimpleDateFormat
import java.util.*

class DialogDatePicker() : DialogFragment() {

    private lateinit var binding : DialogDatepickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        // 화면 밖 클릭 혹은 뒤로가기 해도 dialog가 없어지지 않도록
        isCancelable = false

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogDatepickerBinding.inflate(inflater, container, false)

        val today = setDate()

        // 날짜 저장
        binding.dialogDatepicker.init(today.substring(0, 4).toInt(), today.substring(5, 7).toInt(), today.substring(8).toInt(), object : DatePicker.OnDateChangedListener{
            @SuppressLint("SetTextI18n")
            override fun onDateChanged(view: DatePicker?, year: Int, month: Int, day: Int) {
                // string으로 넣어주기 편하게 미리 변환
                val selectedYear = year.toString()

                val selectedMonth =
                    if(month + 1 < 10) {
                    "0" + (month + 1).toString()
                    }
                    else {
                        (month + 1).toString()
                    }

                val selectedDay =
                    if(day < 10) {
                        "0$day"
                    }
                    else {
                        day.toString()
                    }

                mItemClickListener.saveBirthday("$selectedYear-$selectedMonth-$selectedDay")
            }

        })

        binding.dialogNextTv.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDate() : String {  // 오늘 날짜 불러오기
        val today = System.currentTimeMillis()  // 현재 날짜, 시각 불러오기
        val date = Date(today)
        val mFormat = SimpleDateFormat("yyyy-MM-dd")

        return mFormat.format(date)
    }

    interface MyItemClickListener {
        fun saveBirthday(birthday : String)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }
}