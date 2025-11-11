package com.student.Compass_Abroad.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.FragmentWisePaymentBinding


class WisePaymentFragment : Fragment() {
    private lateinit var binding: FragmentWisePaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentWisePaymentBinding.inflate(inflater, container, false)

        onClicks()
        return binding.root
    }

    private fun onClicks() {

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }


}