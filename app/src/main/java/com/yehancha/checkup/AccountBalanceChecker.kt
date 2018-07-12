package com.yehancha.checkup

import android.app.IntentService
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.telephony.SmsManager
import java.util.*

const val PHONE_NUMBER = "7678"
const val MESSAGE = "DB"

fun startDBChecking(context: Context) {
    runDBChecker(context) // check initially
    scheduleDBChecker(context) // schedule for future
}

fun runDBChecker(context: Context): ComponentName? =
    context.startService(Intent(context, AccountBalanceChecker::class.java))

class AccountBalanceChecker : IntentService("AccountBalanceCheckService") {
    override fun onHandleIntent(intent: Intent) {
        if (!isTimePassed()) return // we skip the check since there is more time

        val sms = SmsManager.getDefault()
        sms.sendTextMessage(PHONE_NUMBER, null, MESSAGE, null, null)
        showDbCheckedNotification()
        setLastDbCheckTime(this, Date())
    }

    private fun isTimePassed() = getLastDbCheckTime(this).let {
        if (it == null) true
        else (Date().time - it.time) > (MILLIS_TO_DAY / 2)
        // We'll check for half of the day. Otherwise the sms will not be sent even if the last sms
        // has been sent 23 hours earlier. Which is not a mindful decision.
    }

    private fun showDbCheckedNotification() {
        val pendingIntent = Intent(this, MainActivity::class.java).let {
            PendingIntent.getActivity(this, 0, it, 0)
        }

        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.checkbox_on_background)
                .setContentTitle("Sent \"$MESSAGE\" to $PHONE_NUMBER to check data balance.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

        NotificationManagerCompat.from(this).notify(100, mBuilder.build())
    }
}