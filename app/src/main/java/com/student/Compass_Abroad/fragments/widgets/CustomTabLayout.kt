package com.student.Compass_Abroad.fragments.widgets

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout

class CustomTabLayout : TabLayout {
    constructor(context: Context?) : super(context!!)

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    )

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        try {
            if (tabCount == 0) {
                return
            }

            val name = "mTabMinWidth"
            val field = TabLayout::class.java.getDeclaredField(name)
            field.isAccessible = true
            field[this] = (measuredWidth / tabCount.toFloat()).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
