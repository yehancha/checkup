package com.yehancha.checkup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.*

const val MILLIS_TO_DAY = 24 * 60 * 60 * 1000

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        println("AlarmTest: BootCompleteReceiver.onReceive")
        startDBChecking(context)
    }
}