package com.example.foodwaste.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.foodwaste.receiver.ExpiryReceiver

class ExpiryWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        android.util.Log.d("ExpiryWorker", "Worker is running!")

        ExpiryReceiver().triggerCheck(applicationContext)

        return Result.success()
    }
}


