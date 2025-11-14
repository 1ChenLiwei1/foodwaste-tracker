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


class FoodWasteApp : Application() {

    lateinit var repository: FoodRepository
        private set

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "foodwaste.db"
        ).build()

        repository = FoodRepository(db.foodItemDao())

        // 启动周期性后台任务（每 15 分钟运行一次）
        scheduleExpiryReminder()

        debugTestWork()
    }

    private fun scheduleExpiryReminder() {
        val request = PeriodicWorkRequestBuilder<ExpiryWorker>(
            15, TimeUnit.MINUTES      // 系统最短允许间隔
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


