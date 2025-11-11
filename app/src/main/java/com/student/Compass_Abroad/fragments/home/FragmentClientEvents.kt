@file:Suppress("DEPRECATION")

package com.student.Compass_Abroad.fragments.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdaptorWebinarsRecyclerview
import com.student.Compass_Abroad.databinding.FragmentClientEventsBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.clientEventModel.ClientEventResponse
import com.student.Compass_Abroad.modal.clientEventModel.Record
import com.student.Compass_Abroad.retrofit.ViewModalClass

class FragmentClientEvents : BaseFragment() {
    var binding: FragmentClientEventsBinding? = null
    private var adaptorClientEvents: AdaptorWebinarsRecyclerview? = null
    var arrayList1 = ArrayList<Record>()
    var isScrolling = false
    var currentVisibleItems = 0
    var totalItemsInAdapter: Int = 0
    var scrolledOutItems: Int = 0
    var dataPerPage = 20
    var presentPage: Int = 1
    var nextPage: Int = 0


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = FragmentClientEventsBinding.inflate(getLayoutInflater(), container, false)

        onGetClientEvent(requireActivity(), dataPerPage, presentPage)

        arrayList1.clear()

        binding!!.backBtn.setOnClickListener {

            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        // Inflate the layout for this fragment
        return binding!!.getRoot()
    }

    private fun onGetClientEvent(
            requireActivity: FragmentActivity,
            presentPage: Int,
            dataPerPage: Int) {

        ViewModalClass().clientEventsModalLiveData(
                requireActivity,
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
                "Bearer " + CommonUtils.accessToken, dataPerPage, presentPage).observe(requireActivity()) { clientEventModal: ClientEventResponse? ->
            clientEventModal?.let { nonNullForgetModal ->
                if (clientEventModal.statusCode == 200) {
                    if (clientEventModal.data!!.records.isEmpty()) {
                        binding!!.noWebinarFound.visibility = View.VISIBLE                    } else {
                        for (i in 0 until clientEventModal.data!!.records.size) {
                            arrayList1.add(clientEventModal.data!!.records[i])
                        }


                        setClientEventsRecyclerview(arrayList1)
                    }

                } else {
                    CommonUtils.toast(
                            requireActivity(),
                            nonNullForgetModal.message ?: " Failed"
                    )
                }
            }
        }

    }


    private fun setClientEventsRecyclerview(arrayList1: ArrayList<Record>) {
        adaptorClientEvents =
                AdaptorWebinarsRecyclerview(this.requireActivity(), arrayList1, arrayList1.size)
        val layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.rvCE.layoutManager = layoutManager
        binding!!.rvCE.adapter = adaptorClientEvents // Corrected the adapter assignment

        binding!!.rvCE.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            isScrolling = true
                        }
                    }

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        currentVisibleItems = layoutManager.childCount
                        totalItemsInAdapter = layoutManager.getItemCount()
                        scrolledOutItems = layoutManager.findFirstVisibleItemPosition()
                        if (isScrolling && scrolledOutItems + currentVisibleItems == totalItemsInAdapter) {
                            isScrolling = false

                            //fetch data
                            if (presentPage < nextPage) {
                                presentPage += 1
                                binding!!.pbFaActivePagination.setVisibility(View.VISIBLE)
                                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                                    override fun run() {
                                        Handler().removeCallbacks(this, null)
                                        onGetClientEvent(requireActivity(), dataPerPage, presentPage)
                                    }
                                }, 2000)
                            }
                        }
                    }
                })

    }

    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav!!.isVisible = false
    }

}