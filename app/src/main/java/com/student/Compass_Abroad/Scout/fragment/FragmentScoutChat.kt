package com.student.Compass_Abroad.Scout.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Scout.adaptor.AdapterScoutApplicationDetailConversation
import com.student.Compass_Abroad.databinding.FragmentScoutChatBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import androidx.navigation.findNavController


class FragmentScoutChat : BaseFragment() {

    private lateinit var binding: FragmentScoutChatBinding
    private lateinit var adapterScoutApplicationDetailConversation: AdapterScoutApplicationDetailConversation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentScoutChatBinding.inflate(inflater,container,false)

        setupChatAdapter()

        binding.llFcWriteMsg.setOnClickListener {
            it.findNavController().navigate(R.id.fragmentScoutMessage)
        }
        return binding.root
    }

    private fun setupChatAdapter() {

        adapterScoutApplicationDetailConversation = AdapterScoutApplicationDetailConversation(requireActivity())
        binding.rvMessages.adapter = adapterScoutApplicationDetailConversation

    }
    override fun onResume() {
        super.onResume()
        ScoutMainActivity.bottomNav!!.visibility = View.GONE
    }


}