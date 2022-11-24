package com.example.eraofband.ui.login

import android.app.Application
import com.example.eraofband.R
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){
    companion object {
        const val BASE_URL = "https://eraofband.shop"
        var width = 0
        var height = 0
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}