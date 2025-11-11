package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.student.Compass_Abroad.adaptor.AdapterApplicationDetailConversation
import com.student.Compass_Abroad.databinding.FragmentInternalChatBinding
import com.student.Compass_Abroad.fragments.BaseFragment


class FragmentInternalChat : BaseFragment() {
    private lateinit var binding: FragmentInternalChatBinding
    private lateinit var adapterApplicationDetailConversation : AdapterApplicationDetailConversation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding= FragmentInternalChatBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        //setRecyclerView()
        setCliclListeners()

        return binding.root
    }

    private fun setCliclListeners() {
        binding.llFcWriteMsg.setOnClickListener { v:View->
         //   Navigation.findNavController(v).navigate(R.id.fragmentMessage)
        }

    }

    /*private fun setRecyclerView() {

        adapterApplicationDetailConversation = AdapterApplicationDetailConversation(
            context,
            chatRecords
        )
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.rvMessages.setLayoutManager(layoutManager)
        binding!!.rvMessages.setAdapter(adapterApplicationDetailConversation)

    }*/

}