package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.ReactionAdapter
import com.student.Compass_Abroad.databinding.FragmentReactionBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.reactionModel.Data
import com.student.Compass_Abroad.modal.reactionModel.REactionResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass
import java.util.ArrayList


private  var binding:FragmentReactionBinding?=null

class ReactionFragment : BaseFragment() {

companion object{
    var LikeReactionDataPass: com.student.Compass_Abroad.modal.getAllPosts.Record ?=null
}
    private var binding: FragmentReactionBinding? = null
    private lateinit var reactionAdapter: ReactionAdapter
    private val reactionsList = ArrayList<Data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding=FragmentReactionBinding.inflate(inflater,container,false)

        setupRecyclerView()
        val postIdentifier = LikeReactionDataPass!!.identifier // Replace with actual post identifier
        onGetAllReactions(requireActivity(), postIdentifier)

        binding!!.fabFpBack.setOnClickListener { v:View->
            requireActivity().onBackPressed()
        }


        // Inflate the layout for this fragment
        return binding!!.root
    }



    private fun setupRecyclerView() {
        reactionAdapter = ReactionAdapter(ArrayList())
        binding?.rvReaction?.apply {
            adapter = reactionAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun onGetAllReactions(requireActivity: FragmentActivity, postIdentifier: String) {
        ViewModalClass().reactionResponseLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, postIdentifier, "like"
        ).observe(requireActivity) { getReactionResponse: REactionResponse? ->
            getReactionResponse?.let { nonNullAllCommentModal ->
                if (getReactionResponse.statusCode == 200) {
                    if (getReactionResponse.data!!.rows.isEmpty()) {
                        Toast.makeText(requireActivity, "No Reaction Found", Toast.LENGTH_LONG).show()
                    } else {
                        reactionsList.add(getReactionResponse.data!!)
                        // Update the adapter with new reactions
                        reactionAdapter.updateReactions(reactionsList)
                    }
                } else {
                    CommonUtils.toast(
                        requireActivity,
                        nonNullAllCommentModal.message ?: "Failed"
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = false

    }
}