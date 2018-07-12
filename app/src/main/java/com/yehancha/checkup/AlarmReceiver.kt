package com.yehancha.checkup

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

fun scheduleDBChecker(context: Context) {
    val intent = Intent(context, AlarmReceiver::class.java)
    val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
    val manager = (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
    manager.setInexactRepeating(AlarmManager.RTC, getAlarmStartTime(), AlarmManager.INTERVAL_DAY, alarmIntent)
}

fun getAlarmStartTime() = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 20)
    set(Calendar.MINUTE, 0)
}.let { it.time.time }

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        runDBChecker(context)
    }
}