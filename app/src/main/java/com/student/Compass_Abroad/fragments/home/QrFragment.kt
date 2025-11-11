package com.student.Compass_Abroad.fragments.home

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

import com.student.Compass_Abroad.databinding.FragmentQrBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class QrFragment : BaseFragment() {

    companion object {
        var data: String? = null
        private const val REQUEST_WRITE_STORAGE = 112
    }

    private lateinit var binding: FragmentQrBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentQrBinding.inflate(inflater, container, false)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), android.R.color.white)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR

        setData()
        return binding.root
    }

    private fun setData() {
        Glide.with(this)
            .load(data)
            .into(binding.IvQrCode)

        binding.fabFpBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btDownloads.setOnClickListener {
            if (data != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    downloadImage(data!!)
                } else {
                    checkPermissionAndDownload(data!!)
                }
            } else {
                Toast.makeText(requireContext(), "No URL to download", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissionAndDownload(url: String) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_STORAGE)
        } else {
            DownloadImageTask().execute(url)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun downloadImage(url: String) {
        DownloadImageTask().execute(url)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (data != null) {
                    DownloadImageTask().execute(data)
                }
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class DownloadImageTask : AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg urls: String): Bitmap? {
            val url = urls[0]
            return try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveImage(result)
                } else {
                    saveImageLegacy(result)
                }
            } else {
                Toast.makeText(requireContext(), "Error downloading image", Toast.LENGTH_SHORT).show()
            }
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        private fun saveImage(image: Bitmap) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "qr_code.png")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp")
            }

            val resolver = requireContext().contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                try {
                    val outputStream: OutputStream? = resolver.openOutputStream(uri)
                    outputStream?.use {
                        image.compress(Bitmap.CompressFormat.PNG, 100, it)
                        it.flush()
                    }
                    Toast.makeText(requireContext(), "Image Downloaded", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error saving image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun saveImageLegacy(image: Bitmap) {
            val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyApp")
            if (!storageDir.exists()) {
                storageDir.mkdirs()
            }
            val file = File(storageDir, "qr_code.png")
            try {
                val outputStream = FileOutputStream(file)
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                Toast.makeText(requireContext(), "Image Downloaded", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error saving image", Toast.LENGTH_SHORT).show()
            }
        }
    }
}