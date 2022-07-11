package com.example.eraofband.main.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eraofband.databinding.ActivityChatContentBinding

class ChatContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}