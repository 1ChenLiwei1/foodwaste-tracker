package com.example.foodwaste

import android.app.Application
import androidx.room.Room
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.foodwaste.data.FoodRepository
import com.example.foodwaste.data.local.AppDatabase
import com.example.foodwaste.worker.ExpiryWorker
import com.jakewharton.threetenabp.AndroidThreeTen
import java.util.concurrent.TimeUnit
import androidx.work.OneTimeWorkRequestBuilder
import com.example.foodwaste.data.repo.ShoppingRepository
import com.example.foodwaste.data.local.ShoppingDao


class FoodWasteApp : Application() {

    //  库存 Repository
    lateinit var repository: FoodRepository
        private set

    //  购物清单 Repository（MainActivity 需要）
    lateinit var shoppingRepository: ShoppingRepository
        private set

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "foodwaste.db"
        ).build()

        // 初始化两个仓库
        repository = FoodRepository(db.foodItemDao())
        shoppingRepository = ShoppingRepository(db.shoppingDao())  // ← ★ 就是这里

        // 启动周期性后台任务（每 15 分钟运行一次）
        scheduleExpiryReminder()

        // 启动一次性的测试任务（启动后 5 秒左右触发）
        debugTestWork()
    }

    private fun scheduleExpiryReminder() {
        val request = PeriodicWorkRequestBuilder<ExpiryWorker>(
            15, TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "expiry_check_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    private fun debugTestWork() {
        val testWork = OneTimeWorkRequestBuilder<ExpiryWorker>().build()
        WorkManager.getInstance(this).enqueue(testWork)
    }
}



