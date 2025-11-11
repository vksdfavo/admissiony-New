package com.student.Compass_Abroad.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.FragmentApplicationFilterBinding

class ApplicationFilterFragment : BaseFragment() {
    private lateinit var binding: FragmentApplicationFilterBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
     binding = FragmentApplicationFilterBinding.inflate(inflater, container, false)

        binding.backBtn.setOnClickListener{

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = false

    }
}