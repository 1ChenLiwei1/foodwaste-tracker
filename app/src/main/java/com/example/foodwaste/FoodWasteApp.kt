package com.example.foodwaste

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class FoodWasteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化 ThreeTenABP，支持 API 24 的 LocalDate
        AndroidThreeTen.init(this)
    }
}