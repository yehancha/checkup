package com.yehancha.checkup

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat

val FORMAT: DateFormat = SimpleDateFormat.getDateTimeInstance()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSendPermissionAndContinue()
        showVersion()
    }

    private fun getSendPermissionAndContinue() = if (!isSendSmsPermissionGranted()) {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 100)
    } else {
        startDBChecking(this)
    }

    private fun isSendSmsPermissionGranted() =
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED

    private fun showVersion() {
        version.text = packageManager.getPackageInfo(packageName, 0).versionName;
    }

    override fun onResume() {
        super.onResume()
        showLastDBCheckedTime()
    }

    private fun showLastDBCheckedTime() = getLastDbCheckTime(this).let {
        message.text = getString(R.string.last_db_check, if (it != null) FORMAT.format(it) else "None")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startDBChecking(this)
        } else {
            finish()
        }
    }
}
