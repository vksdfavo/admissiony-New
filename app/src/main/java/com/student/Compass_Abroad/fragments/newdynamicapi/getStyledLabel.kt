package com.student.Compass_Abroad.newdynamicapi

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.graphics.Color

internal fun getStyledLabel(label: String, isRequired: Boolean): SpannableStringBuilder {
    return if (isRequired) {
        SpannableStringBuilder("$label *").apply {
            setSpan(
                ForegroundColorSpan(Color.RED),
                length - 1,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    } else {
        SpannableStringBuilder(label)
    }
}
