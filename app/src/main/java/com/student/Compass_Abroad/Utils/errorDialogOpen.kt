package com.student.Compass_Abroad.Utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.ErrorAleartLayoutBinding

fun errorDialogOpen(activity: Activity, errorMessage: String) {
    val itemBinding = ErrorAleartLayoutBinding.inflate(activity.layoutInflater)
    val dialog = Dialog(activity)
    dialog.requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    dialog.setCancelable(true)
    dialog.setContentView(itemBinding.root)
    dialog.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    val layoutParams = WindowManager.LayoutParams()
    layoutParams.copyFrom(dialog.window!!.attributes)
    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
    val margin = activity.resources.getDimensionPixelSize(R.dimen.dp_30)
    layoutParams.horizontalMargin = 0f
    dialog.window!!.decorView.setPadding(margin, 0, margin, 0)
    dialog.window!!.attributes = layoutParams


    itemBinding.backBtn.setOnClickListener {

        dialog.dismiss()
    }

    itemBinding.backBtn.setTextColor(ContextCompat.getColor(activity, R.color.theme_color))
    itemBinding.appName.text = activity.getString(R.string.app_name)
    val cleanedMessages = errorMessage.lines()
        .map { line ->

            line.substringAfter(":", line).trim()
        }
        .filter { it.isNotEmpty() }

        .joinToString("\n")

    Log.d("errorDialogOpen", cleanedMessages)
    itemBinding.content.text = cleanedMessages

    dialog.show()
}
