package com.student.Compass_Abroad.Utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object PdfGenerator {
    private const val CHANNEL_ID = "pdf_generation_channel"

    fun createPdfFromRecyclerViewItems(context: Context, recyclerView: RecyclerView, onPdfGenerated: () -> Unit) {
        if (!hasStoragePermissions(context)) {
            Toast.makeText(context, "Storage permissions are required to save the PDF", Toast.LENGTH_SHORT).show()
            return
        }

        createNotificationChannel(context)

        val document = PdfDocument()
        val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = context.getString(R.string.unideft_pdf, timestamp)
        val pdfFile = File(directory, fileName)

        recyclerView.adapter?.let { adapter ->
            for (i in 0 until adapter.itemCount) {
                val viewHolder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i))
                adapter.bindViewHolder(viewHolder, i)

                val itemView = viewHolder.itemView
                itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(recyclerView.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                itemView.layout(0, 0, itemView.measuredWidth, itemView.measuredHeight)

                val pageInfo = PdfDocument.PageInfo.Builder(itemView.width, itemView.height, i + 1).create()
                val page = document.startPage(pageInfo)

                val canvas = page.canvas
                canvas.drawColor(Color.WHITE)
                itemView.draw(canvas)

                document.finishPage(page)
            }

            try {
                FileOutputStream(pdfFile).use { outputStream ->
                    document.writeTo(outputStream)

                    val fileUri: Uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        pdfFile
                    )

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(fileUri, "application/pdf")
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    }

                    val pendingIntent = PendingIntent.getActivity(
                        context, 0, intent,
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                            PendingIntent.FLAG_IMMUTABLE
                        else
                            PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    val completionNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("PDF Generation")
                        .setContentText("PDF saved to Documents")
                        .setSmallIcon(R.drawable.ic_notification)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .build()

                    NotificationManagerCompat.from(context).notify(2, completionNotification)

                    // Call the callback to show the toast after PDF generation is done
                    onPdfGenerated.invoke()

                }
            } catch (e: IOException) {
                e.printStackTrace()
                val failureNotification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("PDF Generation")
                    .setContentText("Failed to save PDF")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build()

                NotificationManagerCompat.from(context).notify(2, failureNotification)
            } finally {
                document.close()
                NotificationManagerCompat.from(context).cancel(1)
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PDF Generation Channel"
            val descriptionText = "Channel for PDF generation notifications"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                lightColor = Color.GREEN
                enableLights(true)
                enableVibration(true)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun hasStoragePermissions(context: Context): Boolean {
        val readPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        val readGranted = ActivityCompat.checkSelfPermission(context, readPermission) == PackageManager.PERMISSION_GRANTED
        return readGranted
    }
}

