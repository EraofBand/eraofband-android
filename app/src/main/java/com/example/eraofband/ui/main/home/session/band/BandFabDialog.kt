package com.example.eraofband.ui.main.home.session.band

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogBandFabBinding
import com.example.eraofband.ui.login.GlobalApplication

class BandFabDialog : DialogFragment() {
    private lateinit var binding: DialogBandFabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogBandFabBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val params: WindowManager.LayoutParams? = dialog?.window?.attributes

        params?.x = GlobalApplication.width / 2 - 75.toPx()
        params?.y = GlobalApplication.height / 2 - 113.toPx()

        dialog?.window?.attributes = params

        binding.bandFabBandIv.setOnClickListener {
            startActivity(Intent(activity, BandMakeActivity::class.java))
            dismiss()
        }

        binding.bandFabMakeTv.setOnClickListener {
            startActivity(Intent(activity, BandMakeActivity::class.java))
            dismiss()
        }

        return binding.root
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}