package com.example.eraofband

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.eraofband.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 바텀 네비게이션 모서리 둥글게 만들기
        val bottomNavBg = binding.mainBottomNav.background as MaterialShapeDrawable
        bottomNavBg.shapeAppearanceModel = bottomNavBg.shapeAppearanceModel.toBuilder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 70.0f)
            .setTopRightCorner(CornerFamily.ROUNDED, 70.0f).build()

    }
}