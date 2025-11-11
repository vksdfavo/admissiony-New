package com.student.Compass_Abroad.Utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import androidx.core.graphics.drawable.toDrawable

class CommonDialog<T : ViewBinding>(context: Context, binding: T) {
    private val dialog: Dialog

    init {
        dialog = Dialog(context)
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
    }

    fun show() {
        dialog.show()
    }

    fun dismiss()
    {
        dialog.dismiss()
    }
}