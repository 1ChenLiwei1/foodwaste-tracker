package com.example.foodwaste.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.foodwaste.MainActivity
import com.example.foodwaste.R
import com.example.foodwaste.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class ExpiryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        //Log.d("ExpiryReceiver", "Broadcast received!")
        triggerCheck(context)
    }

    /** Unified entry point for Worker or broadcast calls */
    fun triggerCheck(context: Context) {
        //Log.d("ExpiryReceiver", "triggerCheck() called")

        val db = AppDatabase.get(context)
        val dao = db.foodItemDao()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val today = LocalDate.now()
                val soon = today.plusDays(3)

                // 查数据库
                val list = dao.getExpiringStatic(
                    today.toEpochDay(),
                    soon.toEpochDay()
                )

                //Log.d("ExpiryReceiver", "Expiring items count = ${list.size}")

                if (list.isNotEmpty()) {
                    showNotification(context, list.size)
                }
            } catch (e: Exception) {
                //Log.e("ExpiryReceiver", "Error: ${e.message}")
            }
        }
    }

    private fun showNotification(context: Context, count: Int) {
        //Log.d("ExpiryReceiver", "showNotification() called")

        val channelId = "expiry_alerts"
        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Food Expiry Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminds you of items expiring soon"
                enableLights(true)
                lightColor = Color.RED
            }
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        val pending = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Expiry Alert")
            .setContentText("You have $count item(s) expiring soon!")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pending)
            .setAutoCancel(true)
            .build()

        manager.notify(1001, notification)
    }
}

