package com.example.eraofband.login

import android.app.Application
import com.example.eraofband.R
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){
    companion object {
        const val BASE_URL = "https://eraofband.shop"
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}