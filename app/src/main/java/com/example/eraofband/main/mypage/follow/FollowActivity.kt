package com.example.eraofband.main.mypage.follow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.databinding.ActivityFollowBinding
import com.google.android.material.tabs.TabLayoutMediator

class FollowActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityFollowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var current = intent.getIntExtra("current", 0)

        //뷰페이저 어뎁터 연결
        val fragmentList = listOf(FollowingFragment(), FollowerFragment())
        val followVPAdapter = FollowVPAdapter(this)
        followVPAdapter.fragments = fragmentList
        binding.followVp.adapter = followVPAdapter

        //탭레이아웃 연결
        TabLayoutMediator(binding.followTl, binding.followVp) { tab, position ->
            when (position) {
                0 -> tab.text = "팔로잉"
                1 -> tab.text = "팔로워"
            }
        }.attach()

        if(current == 0){
            binding.followVp.post{
                binding.followVp.currentItem = 0
            }
        } else if (current == 1){
            binding.followVp.post{
                binding.followVp.currentItem = 1
            }
        }

        binding.followBackIb.setOnClickListener{
            finish()
        }
    }
}