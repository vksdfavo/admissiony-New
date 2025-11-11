package com.student.Compass_Abroad.fragments.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.Utils.PdfGenerator

import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterComparison
import com.student.Compass_Abroad.databinding.FragmentComparisonBinding
import com.student.Compass_Abroad.fragments.BaseFragment

class Comparison : BaseFragment() {
    private var binding: FragmentComparisonBinding? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapterComparison: AdapterComparison

    companion object {
        var comparisonList: ArrayList<com.student.Compass_Abroad.modal.AllProgramModel.Record>? =
            null
    }

    private val REQUEST_CODE_PERMISSIONS = 101
    private val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.POST_NOTIFICATIONS)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComparisonBinding.inflate(inflater, container, false)
        setAllCompareRecyclerview(comparisonList)

        binding?.btnDownload?.setOnClickListener {
            if (allPermissionsGranted()) {
                PdfGenerator.createPdfFromRecyclerViewItems(requireActivity(), binding!!.rvFpAp) {
                    Toast.makeText(requireActivity(), "Downloading....", Toast.LENGTH_SHORT).show()
                }
                Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show()

            } else {
                requestPermissions()
            }
        }


        binding?.backBtn?.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding!!.root
    }

    private fun setAllCompareRecyclerview(arrayList1: ArrayList<com.student.Compass_Abroad.modal.AllProgramModel.Record>?) {
        linearLayoutManager = LinearLayoutManager(requireActivity())
        binding?.rvFpAp?.layoutManager = linearLayoutManager

        adapterComparison = AdapterComparison(requireActivity(), arrayList1!!)
        binding?.rvFpAp?.adapter = adapterComparison
    }

    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav?.isVisible = false
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {

        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED

    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                PdfGenerator.createPdfFromRecyclerViewItems(requireActivity(), binding!!.rvFpAp) {
                    // This block is called after the PDF is generated
                    Toast.makeText(requireActivity(), "Download completed", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(context, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
