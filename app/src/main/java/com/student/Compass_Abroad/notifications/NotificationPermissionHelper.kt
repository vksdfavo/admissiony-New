package com.student.Compass_Abroad.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class NotificationPermissionHelper(private val context: Context, private val permissionLauncher: ActivityResultLauncher<String>) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)

    fun isPermissionGranted(): Boolean {

        return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestNotificationPermission() {

        if (!isPermissionGranted()) {

            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

}
