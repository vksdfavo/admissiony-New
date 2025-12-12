package com.student.Compass_Abroad.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.student.Compass_Abroad.databinding.FragmentStudentInfoBinding

class StudentInfoFragment : BaseFragment() {
    private lateinit var binding: FragmentStudentInfoBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
    binding = FragmentStudentInfoBinding.inflate(inflater, container, false)

        return binding.root
    }
}