package com.student.Compass_Abroad.fragments.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdaptorStudent
import com.student.Compass_Abroad.databinding.FragmentStudentBinding
import com.student.Compass_Abroad.fragments.BaseFragment

class StudentFragment : BaseFragment() {
    var binding: FragmentStudentBinding? = null
    var adaptorStudent: AdaptorStudent? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentBinding.inflate(inflater, container, false)
        setStudentRecyclerview(requireContext())

        // Inflate the layout for this fragment
        return binding!!.getRoot()
    }

    private fun setStudentRecyclerview(context: Context) {
        adaptorStudent = AdaptorStudent(context)
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.rvStudent.setLayoutManager(layoutManager)
        binding!!.rvStudent.setAdapter(adaptorStudent)
    }
    override fun onResume() {
        super.onResume()
        MainActivity.bottomNav!!.isVisible=true
    }
}