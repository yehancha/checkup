package com.yehancha.checkup

import android.content.Context
import android.preference.PreferenceManager
import java.util.*

private const val KEY_LAST_DB_CHECK_TIME = "KEY_LAST_DB_CHECK_TIME"

fun getLastDbCheckTime(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context).getLong(KEY_LAST_DB_CHECK_TIME, 0L).let {
            if (it == 0L) null else Date(it)
        }

fun setLastDbCheckTime(context: Context, time: Date) =
    PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(KEY_LAST_DB_CHECK_TIME, time.time).apply()