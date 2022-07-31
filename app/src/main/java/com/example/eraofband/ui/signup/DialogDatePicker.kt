package com.example.eraofband.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogDatepickerBinding

class DialogDatePicker(private val selectedDate : String) : DialogFragment() {

    private lateinit var binding : DialogDatepickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogDatepickerBinding.inflate(inflater, container, false)

        val date = selectedDate  // 넘겨받은 날짜를 기본으로

        // 날짜 저장
        binding.dialogDatepicker.init(date.substring(0, 4).toInt(),date.substring(5,7).toInt() - 1 ,date.substring(8,10).toInt()) { _, year, month, day -> // string으로 넣어주기 편하게 미리 변환
            val selectedYear = year.toString()

            val selectedMonth =
                if (month < 10) {
                    "0" + (month + 1).toString()
                } else {
                    (month + 1).toString()
                }

            val selectedDay =
                if (day < 10) {
                    "0$day"
                } else {
                    day.toString()
                }

            mItemClickListener.saveBirthday("$selectedYear-$selectedMonth-$selectedDay")
        }

        binding.dialogNextTv.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    interface MyItemClickListener {
        fun saveBirthday(birthday : String)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }
}