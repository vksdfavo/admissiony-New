package com.student.Compass_Abroad.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.FragmentPaynetDetailsBinding


class PaynetDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentPaynetDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      binding = FragmentPaynetDetailsBinding.inflate(inflater, container, false)


        return binding.root
    }

}