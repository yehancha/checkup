package com.yehancha.checkup

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.telephony.SmsManager
import java.util.*

fun startDBChecking(context: Context) {
    checkDBIfTimePassed(context)
    scheduleDBChecker(context)
}

private fun checkDBIfTimePassed(context: Context) {
    if (isTimePassed(context)) runDBChecker(context)
}

private fun isTimePassed(context: Context) = getLastDbCheckTime(context).let {
    if (it == null) true
    else Date().time - it.time > MILLIS_TO_DAY
}

fun runDBChecker(context: Context) {
    println("AlarmTest: startAccountBalanceChecker")
    context.startService(Intent(context, AccountBalanceChecker::class.java))
}

class AccountBalanceChecker : IntentService("AccountBalanceCheckService") {
    override fun onHandleIntent(intent: Intent) {
        println("AlarmTest: AccountBalanceChecker.onHandleIntent")
        val sms = SmsManager.getDefault()
        sms.sendTextMessage("7678", null, "DB", null, null)
        showDbCheckedNotification()
        storeLastDbCheckTime(this, Date())
    }

    private fun showDbCheckedNotification() {
        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.checkbox_on_background)
                .setContentTitle("Sent data balance check message")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent(this))
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(100, mBuilder.build())
    }

    private fun getPendingIntent(context: Context) = Intent(context, MainActivity::class.java).let {
        PendingIntent.getActivity(context, 0, it, 0)
    }
}