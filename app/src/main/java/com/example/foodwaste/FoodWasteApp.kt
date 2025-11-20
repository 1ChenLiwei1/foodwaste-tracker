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

    //  Inventory Repository
    lateinit var repository: FoodRepository
        private set

    //   Shopping List Repository
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

        // Initialise two warehouses
        repository = FoodRepository(db.foodItemDao())
        shoppingRepository = ShoppingRepository(db.shoppingDao())

        // Initiate periodic background tasks
        scheduleExpiryReminder()

        // Initiate a one-off test task
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



