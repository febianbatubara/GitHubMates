package com.febian.android.githubmates.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.febian.android.githubmates.R
import com.febian.android.githubmates.activity.SplashActivity
import java.util.*

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_MESSAGE = "message"

        const val TYPE_REPEATING = "Daily Reminder"
        private const val ID_REPEATING = 101
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        message?.let { showReminderNotification(context, it) }
    }

    private fun showReminderNotification(context: Context, message: String) {
        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"

        val intent = Intent(context, SplashActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_bookmark_24)
            .setContentTitle(TYPE_REPEATING)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setSound(alarmSound)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            builder.setChannelId(channelId)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()

        notificationManagerCompat.notify(ID_REPEATING, notification)
    }

    fun setRepeatingReminder(context: Context, time: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(
            context,
            context.getString(R.string.daily_reminder_activated),
            Toast.LENGTH_SHORT
        ).show()
    }

    fun cancelRepeatingReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)

        Toast.makeText(
            context,
            context.getString(R.string.daily_reminder_removed),
            Toast.LENGTH_SHORT
        ).show()
    }

}