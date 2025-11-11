package com.student.Compass_Abroad.Utils


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.encrytion.encryptData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.net.URLConnection
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.Callable
import java.util.regex.Pattern
import kotlin.math.abs

object CommonUtils {
    private var alertDialog: AlertDialog? = null

    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (permissions != null) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        context!!,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return true
                }
            }
        }
        return false
    }

    fun convertDate33(dateStr: String, format: String): String {
        return try {
            // Parse input date string as UTC
            val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC") // Set to UTC

            val date = utcFormat.parse(dateStr) ?: return "Invalid Date"

            // Convert to local timezone
            val localFormat = SimpleDateFormat(format, Locale.getDefault())
            localFormat.timeZone = TimeZone.getDefault() // Convert to local time

            localFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            "Invalid Date"
        }
    }
    fun mediaScanner(
        context: Context?,
        newFilePath: String,
        oldFilePath: String?,
        fileType: String,
    ) {
        try {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(newFilePath + File(oldFilePath).name),
                arrayOf(fileType)
            ) { path, uri -> }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getBack(paramString1: String?, paramString2: String?): String {
        val localMatcher = Pattern.compile(paramString2).matcher(paramString1 ?: "")
        return if (localMatcher.find()) {
            localMatcher.group(1) ?: ""
        } else ""
    }

    fun stripHtml(html: String): String {
        return html.replace(Regex("<[^>]*>"), "")
    }


    fun shareIntent(context: Context, title: String?, message: String?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    fun showProgress(activity: Activity) {
        alertDialog?.dismiss()
        val alert = AlertDialog.Builder(activity)
        val mview = activity.layoutInflater.inflate( R.layout.dialog_progress, null)
        alert.setView(mview)
        alertDialog = alert.create()
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.setCanceledOnTouchOutside(false)
        alertDialog?.show()
    }

    fun dismissProgress() {
        alertDialog?.dismiss()
    }

    fun isImageFile(path: String?): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("image")
    }

    fun isVideoFile(path: String?): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType != null && mimeType.startsWith("video")
    }

    fun getDir(context: Context, folder: String): File {
        val rootFile = File(
            Environment.getExternalStorageDirectory().toString()
                    + File.separator + "Download" + File.separator + context.getString(R.string.app_name) + File.separator + folder
        )
        rootFile.mkdirs()
        return rootFile
    }

    fun stringToRequest(s: String?): RequestBody {
        return RequestBody.create("".toMediaTypeOrNull(), s ?: "")
    }

    fun imageToMultiPart(parameter: String?, imagePath: String): MultipartBody.Part {
        val file = File(imagePath)
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(parameter ?: "", file.name, requestFile)
    }

    val userId: String
        get() = sharedPre?.getString(AppConstants.USER_ID, "") ?: ""

    val accessToken: String?
        get() = sharedPre?.getString(AppConstants.ACCESS_TOKEN, "")

    val refreshToken: String?
        get() = sharedPre?.getString(AppConstants.REFRESH_TOKEN, "")


    fun toast(context: Context?, msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun RecyclerView.addOnPaginationListener(
        nextPage: Int,
        onLoadMore: (nextPage: Int) -> Unit,
    ) {
        var isScrolling = false
        var currentVisibleItems: Int
        var totalItemsInAdapter: Int
        var scrolledOutItems: Int

        val layoutManager = this.layoutManager as LinearLayoutManager

        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentVisibleItems = layoutManager.childCount
                totalItemsInAdapter = layoutManager.itemCount
                scrolledOutItems = layoutManager.findFirstVisibleItemPosition()
                if (isScrolling && scrolledOutItems + currentVisibleItems == totalItemsInAdapter) { isScrolling = false
                     onLoadMore(nextPage)
                }
            }
        })
    }


    fun getTimeAgo(value: String?): String? {
        if (value == null) return null

        val inputPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val format = SimpleDateFormat(inputPattern, Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("IST") // Ensure the input is parsed as UTC

        return try {
            val date = format.parse(value)

            // Convert to Indian Standard Time (IST)
            val istTimeZone = TimeZone.getTimeZone("Asia/Kolkata")
            val formatIST = SimpleDateFormat(inputPattern, Locale.getDefault())
            formatIST.timeZone = istTimeZone
            val istDateString = formatIST.format(date)
            val istDate = formatIST.parse(istDateString)

            val now = System.currentTimeMillis()
            val diffMillis = now - istDate.time

            val diffSeconds = abs(diffMillis   / 1000)
            val diffMinutes = abs(diffSeconds / 60)
            val diffHours = abs(diffMinutes / 60)
            val diffDays = abs(diffHours / 24)
            val diffWeeks = abs(diffDays     / 7)
            val diffMonths = abs(diffDays  / 30)
            val diffYears = abs(diffDays / 365)

            when {
                diffYears >  0 -> "$diffYears years ago"
                diffMonths > 0 -> "$diffMonths months ago"
                diffWeeks >  0 -> "$diffWeeks weeks ago"
                diffDays >   0 -> "$diffDays days ago"
                diffHours >  0 -> "$diffHours hours ago"
                diffMinutes > 0 -> "$diffMinutes minutes ago"
                else -> "just now"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun removeHtmlTags(input: String): String {
        return input.replace(Regex("<.*?>"), "")
    }


    fun convertDate(inputDate: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        // Parse the input date string to a Date object
        val date: Date = inputFormat.parse(inputDate)

        // Format the Date object to the desired output format
        return outputFormat.format(date)
    }

    fun convertDate3(inputDate: String, outputPattern: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Set input timezone to UTC

        val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())

        // Parse the input date string to a Date object
        val date: Date = inputFormat.parse(inputDate)

        // Format the Date object to the desired output format
        return outputFormat.format(date)
    }

    fun convertDate2(inputDate: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        // Parse the input date string to a Date object
        val date: Date = inputFormat.parse(inputDate)

        // Format the Date object to the desired output format
        return outputFormat.format(date)
    }

    fun convertDate4(inputDate: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        // Parse the input date string to a Date object
        val date: Date = inputFormat.parse(inputDate)

        // Format the Date object to the desired output format
        return outputFormat.format(date)
    }

    fun calculateAge(dateOfBirth: String): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val birthDate: Date = sdf.parse(dateOfBirth) ?: return 0

        val today = Calendar.getInstance()
        val dob = Calendar.getInstance()
        dob.time = birthDate

        var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }

    fun removeEmptyTags(html: String): String {
        // Define an array of tags you want to remove if empty
        val tagsToRemove = arrayOf("p", "div", "span", "a") // Add more tags as needed

        // Build the regular expression to match empty tags
        val tagsRegex = tagsToRemove.joinToString("|") { "<$it>\\s*</$it>" }.toRegex()

        // Replace all occurrences of empty tags with an empty string
        val cleanedHtml = html.replace(tagsRegex, "")

        return cleanedHtml
    }
    fun hasReadAndWriteStoragePermissions(activity: Activity?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager()
        } else {
            val readPermission = ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            val writePermission = ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            return readPermission && writePermission
        }
    }

    fun hasReadStoragePermission(activity: Activity?): Boolean {
        return ContextCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private var progressDialog: ProgressDialog? = null

    fun progressDialog(activity: Activity?, title: String?, message: String?, time: Int) {
        progressDialog = ProgressDialog(activity)
        if (title != null) {
            progressDialog!!.setTitle(title)
        }
        progressDialog!!.setMessage(message)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.show()
        progressDialog!!.setCancelable(false)
        Thread {
            try {
                Thread.sleep(time.toLong())
            } catch (e: java.lang.Exception) {
                toast(activity,e.message)

            }
        }.start()
    }

    fun progressDialogueDismiss() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: IllegalArgumentException) {
                Log.e("Error", "Exception Blocked")
            }
        }
    }

    fun alertDialog(
        activity: Activity?,
        icon: String?,
        title: String?,
        msg: String?,
        cancelableValue: Boolean,
        positiveBtnText: String?,
        negativeBtnText: String?,
        positiveMethod: Callable<Void?>?,
        negativeMethod: Callable<Void?>?,
    ) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(
            activity!!
        )

        if (icon != null) {
            when (icon) {
                "info" -> builder.setIcon(R.drawable.ic_info_alert_dialog)
                "warning" -> builder.setIcon(R.drawable.ic_warning_alert_dialog)
                "error" -> builder.setIcon(R.drawable.ic_error_alert_dialog)
                "success" -> builder.setIcon(R.drawable.ic_success_alert_dialog)
                "location" -> builder.setIcon(R.drawable.ic_location_alert_dialog)
                "storage" -> builder.setIcon(R.drawable.ic_storage_alert_dialog)
                "camera" -> builder.setIcon(R.drawable.ic_camera_alert_dialog)
            }
        }

        if (title != null) {
            builder.setTitle(title)
        }

        if (msg != null) {
            builder.setMessage(msg)
        }

        builder.setCancelable(cancelableValue)

        if (positiveBtnText != null) {
            builder.setPositiveButton(positiveBtnText, null)
        }

        if (negativeBtnText != null) {
            builder.setNegativeButton(negativeBtnText, null)
        }

        val dialog = builder.create()
        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                if (positiveMethod != null) {
                    try {
                        if (progressDialog != null && progressDialog!!.isShowing()) {
                            progressDialogueDismiss()
                        }
                        dialog.dismiss()
                        positiveMethod.call()
                    } catch (e: java.lang.Exception) {
                        toast(
                            activity,
                            e.message,

                        )
                    }
                } else if (!cancelableValue) {
                    if (progressDialog != null && progressDialog!!.isShowing()) {
                        progressDialogueDismiss()
                    }
                    dialog.dismiss()
                }
            }
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener {
                if (negativeMethod != null) {
                    try {
                        if (progressDialog != null && progressDialog!!.isShowing()) {
                            progressDialogueDismiss()
                        }
                        dialog.dismiss()
                        negativeMethod.call()
                    } catch (e: java.lang.Exception) {
                        toast(
                            activity,
                            e.message,

                            )
                    }
                } else if (!cancelableValue) {
                    if (progressDialog != null &&progressDialog!!.isShowing()) {
                        progressDialogueDismiss()
                    }
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    class ProgressDialogue(private val context: Context) {
        private var progressDialog: ProgressDialog? = null
        private var title: String? = null
        private var message: String? = null
        private var progressStyle = 0

        fun setContentTitle(title: String?): ProgressDialogue {
            this.title = title
            return this
        }

        fun setContentText(message: String?): ProgressDialogue {
            this.message = message
            return this
        }

        fun setProgressStyle(progressStyle: Int): ProgressDialogue {
            this.progressStyle = progressStyle
            return this
        }

        fun show() {
            progressDialog = ProgressDialog(context)
            progressDialog!!.setTitle(if (title != null) title else null)
            progressDialog!!.setMessage(if (message != null) message else null)
            progressDialog!!.setProgressStyle(if (progressStyle == ProgressDialog.STYLE_HORIZONTAL) ProgressDialog.STYLE_HORIZONTAL else ProgressDialog.STYLE_SPINNER)
            progressDialog!!.setCancelable(false)
            progressDialog!!.isIndeterminate = false
            progressDialog!!.progress = 0
            progressDialog!!.max = 100
            progressDialog!!.show()
        }

        fun update(newTitle: String?, newText: String?, newProgress: Int) {
            if (progressDialog != null && progressDialog!!.isShowing) {
                if (newTitle != null) {
                    progressDialog!!.setTitle(newTitle)
                }
                if (newText != null) {
                    progressDialog!!.setMessage(newText)
                }
                if (progressStyle == ProgressDialog.STYLE_HORIZONTAL) {
                    progressDialog!!.progress = newProgress
                }
            }
        }

        fun dismiss() {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        }
    }

    private fun matchDeviceIdentifier(identifier: String?, pattern: String?): Boolean {
        if (identifier == null) {
            return false
        }
        val compiledPattern = Pattern.compile(pattern)
        val matcher = compiledPattern.matcher(identifier)
        return matcher.matches()
    }
    fun createContentCheckUser(
        context: Context?,
        publicKey: String,
        email: String?,
        requestFrom: String?
    ): String? {
        val userInfoIdentifier = AppConstants.User_IDENTIFIER
        val pattern = "^([A-Za-z]{1,3})(\\d+)([A-Za-z]{1})(\\d{2})([A-Za-z0-9]{5})(\\d{2})$"
        val hasDeviceIdentifier = matchDeviceIdentifier(userInfoIdentifier, pattern)

        val formData = JSONObject()
        try {
            formData.put("request_from", requestFrom)
            formData.put("data_type", "email")
            formData.put("username", email)
            formData.put("has_device_identifier", if (hasDeviceIdentifier) "yes" else "no")

            if (!hasDeviceIdentifier) {
                val deviceInfo = JSONObject()
                deviceInfo.put("device_os", "android")
                deviceInfo.put("device_client", "app")
                deviceInfo.put("device_client_name", "student_app")
                deviceInfo.put("device_client_version", "v1.0.0.0-beta")
                formData.put("device_info", deviceInfo)
            } else {
                formData.put("device_identifier", userInfoIdentifier)
            }

            return encryptData(
                formData.toString(),
                AppConstants.appSecret,
                AppConstants.privateKey + publicKey
            )
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            return null
        }
    }

    fun convertUTCToLocal(utcDateTimeStr: String): String {
        // Define the input and output format
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")

        return try {
            val utcDateTime = LocalDateTime.parse(utcDateTimeStr, inputFormatter)
            val utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"))
            val userZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault())
            userZonedDateTime.format(outputFormatter)
        } catch (e: DateTimeParseException) {
            // Handle parsing exception
            "Invalid Date Format"
        }
    }

    fun convertUTCToLocal1(utcDate: String?): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(utcDate ?: "")
            outputFormat.format(date!!)
        } catch (e: Exception) {
            ""
        }
    }

}
