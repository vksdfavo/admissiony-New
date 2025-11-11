package com.student.bt_global.Utils

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import com.student.Compass_Abroad.R
import com.student.bt_global.Utils.Common.isConnectedToInternet

class NeTWorkChange(private val activity: Activity) : BroadcastReceiver() {
    private var dialog: AlertDialog? = null
    override fun onReceive(context: Context, intent: Intent) {
        if (!isConnectedToInternet(activity)) {
            if (dialog?.isShowing == true) return

            val builder = AlertDialog.Builder(activity)
            val layout_dialog = LayoutInflater.from(activity).inflate(R.layout.check_internet, null)
            builder.setView(layout_dialog)

            val btnRetry = layout_dialog.findViewById<TextView>(R.id.btnRetry)
            dialog = builder.create()
            dialog?.setCancelable(false)
            dialog?.show()
            dialog?.window?.setGravity(Gravity.CENTER)

            btnRetry.setOnClickListener {
                dialog?.dismiss()
                onReceive(context, intent)
            }
        } else {
            dialog?.dismiss()
        }
    }
}
