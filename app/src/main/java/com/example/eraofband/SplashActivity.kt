package com.example.eraofband

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.eraofband.databinding.ActivitySplashBinding
import com.kakao.sdk.user.UserApiClient

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {  // 토큰이 없으면
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else if (tokenInfo != null) {  // 토큰이 있으면
                    Log.d(
                        "tokenInfo", "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo.id}" +
                                "\n만료시간: ${tokenInfo.expiresIn} 초"
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                }  /* 만약에 태스크에 호출하려는 엑티비티의 인스턴스가 이미 존재하고 있을 경우에
                 새로운 인스턴스를 생성하는 것 대신에 존재하고 있는 액티비티를 포그라운드로 가져온다.
                 그리고 호출한 인스턴스를 포그라운드로 가져올때까지 있었던 위의 인스턴스들을 모두 삭제한다. */
            }
        }, 1000)
    }
}