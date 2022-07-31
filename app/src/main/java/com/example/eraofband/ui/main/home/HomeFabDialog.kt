package com.example.eraofband.ui.main.home

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogHomeFabBinding
import com.example.eraofband.ui.login.GlobalApplication
import com.example.eraofband.ui.main.home.lesson.LessonMakeActivity
import com.example.eraofband.ui.main.home.session.band.BandMakeActivity

class HomeFabDialog: DialogFragment() {

    private lateinit var binding: DialogHomeFabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogHomeFabBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val params: WindowManager.LayoutParams? = dialog?.window?.attributes

        params?.x = GlobalApplication.width / 2 - 75.toPx()
        params?.y = GlobalApplication.height / 2 - 225.toPx()

        dialog?.window?.attributes = params

        binding.homeFabBand.setOnClickListener {
            startActivity(Intent(activity, BandMakeActivity::class.java))
            dismiss()
        }

        binding.homeFabLesson.setOnClickListener {
            startActivity(Intent(activity, LessonMakeActivity::class.java))
            dismiss()
        }

        return binding.root
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}