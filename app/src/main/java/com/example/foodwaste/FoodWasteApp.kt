package com.example.foodwaste

import android.app.Application
import androidx.room.Room
import com.example.foodwaste.data.FoodRepository
import com.example.foodwaste.data.local.AppDatabase
import com.jakewharton.threetenabp.AndroidThreeTen

class FoodWasteApp : Application() {

    // 全局持有数据库和仓库（Repository）
    lateinit var repository: FoodRepository
        private set

    override fun onCreate() {
        super.onCreate()

        // 初始化 ThreeTenABP（支持 API 24）
        AndroidThreeTen.init(this)

        // 构建 Room 数据库
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "foodwaste.db"
        ).build()

        // 初始化仓库
        repository = FoodRepository(db.foodItemDao())
    }
}