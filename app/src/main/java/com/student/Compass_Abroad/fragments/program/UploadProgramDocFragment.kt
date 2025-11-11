package com.student.Compass_Abroad.fragments.program

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.databinding.FragmentUploadProgramDocBinding
import com.student.Compass_Abroad.fragments.home.SharedViewModel
import androidx.navigation.findNavController
import com.student.Compass_Abroad.fragments.BaseFragment


class UploadProgramDocFragment : BaseFragment() {
    private lateinit var binding: FragmentUploadProgramDocBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    var status: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUploadProgramDocBinding.inflate(inflater, container, false)


         status = arguments?.getString("status")
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.teall)
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.teall)
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.tickView.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(800)
            .setStartDelay(300)
            .setInterpolator(android.view.animation.OvershootInterpolator())
            .start()
        onClick()
        return binding.root
    }

    private fun onClick() {


        binding.uploadDocument.setOnClickListener{

            binding.root.findNavController().navigate(R.id.fragmentProgramUploadDocuments)
        }


        binding.skip.setOnClickListener{
            sharedViewModel.triggerRefreshData()
            if (status == "1"){

                binding.root.findNavController().navigate(R.id.applicationActiveFragment)

            }else{

                binding.root.findNavController().navigate(R.id.applicationActiveFragment)

            }

        }
    }

}