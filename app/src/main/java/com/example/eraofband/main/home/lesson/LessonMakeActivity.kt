package com.example.eraofband.main.home.lesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.eraofband.data.Lesson
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ActivityLessonMakeBinding
import com.example.eraofband.remote.makeLesson.MakeLessonResult
import com.example.eraofband.remote.makeLesson.MakeLessonService
import com.example.eraofband.remote.makeLesson.MakeLessonView
import com.example.eraofband.remote.makePofol.MakePofolResult
import com.example.eraofband.remote.makePofol.MakePofolService

class LessonMakeActivity : AppCompatActivity(), MakeLessonView {

    private lateinit var binding: ActivityLessonMakeBinding
    private var lesson: Lesson = Lesson(
        1, "", "", "", "",
        "", 0, "", 0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeLessonMakeBackIb.setOnClickListener { finish() }

        binding.homeLessonMakeRegisterBtn.setOnClickListener {
            val makeLessonService = MakeLessonService()
            makeLessonService.setMakeLessonView(this)
            makeLessonService.makeLesson(getJwt()!!, lesson)

            finish()
        }
    }

    private fun getJwt(): String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onMakeLessonSuccess(code: Int, result: MakeLessonResult) {
        Log.d("MAKE/SUCCESS", result.toString())
    }

    override fun onMakeLessonFailure(code: Int, message: String) {
        Log.d("MAKE/FAIL", "$code $message")
    }
}
