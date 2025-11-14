package com.student.Compass_Abroad.fragments.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterApplicationsActive
import com.student.Compass_Abroad.databinding.FragmentApplicationActiveBinding
import com.student.Compass_Abroad.modal.getApplicationResponse.Record
import com.student.Compass_Abroad.modal.staffProfile.StaffProfileModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import androidx.navigation.findNavController
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.fragments.BaseFragment


@Suppress("DEPRECATION")
class ApplicationActiveFragment : Fragment() {
    var binding: FragmentApplicationActiveBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var adapterApplicationsActive: AdapterApplicationsActive? = null
    private val applicationList: MutableList<Record> = mutableListOf()
    private var recyclerViewPosition: Int = 0

    private val viewModel: ViewModalClass by lazy {
        ViewModalClass()     
    }

    companion object{
      var  data:String?=null
        var data2:com.student.Compass_Abroad.modal.getLeads.Record?=null
    }
    private var currentPage = 1
    private var perPage = 25
    private var isLoading = false
    private var hasNextPage = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentApplicationActiveBinding.inflate(inflater, container, false)


        setApplicationActiveRecyclerview(requireContext())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }





        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.white)

        binding!!.civFApplications.setOnClickListener {
            MainActivity.drawer!!.open()

        }
        binding!!.swipeRefreshLayout.setOnRefreshListener {
            applicationList.clear()
            currentPage = 1
            hasNextPage = true
            fetchDataFromApi(data)
        }


        sharedViewModel.refreshDataEvent.observe(viewLifecycleOwner) { shouldRefresh ->
            if (shouldRefresh) {
                applicationList.clear()
                currentPage = 1
                hasNextPage = true
                fetchDataFromApi(data)
                sharedViewModel.triggerRefresh()
            }
        }

        fetchDataFromApi(data)
        if (AppConstants.profileStatus.equals("0")) {
            hitApiUserDetails()
        } else {
            binding!!.tvEmail.text = data2?.email ?: "N/A"
            val countryCode = data2?.country_code ?: "N/A"
            val mobile = data2?.mobile ?: "N/A"
            val fullPhoneNumber = if (countryCode != "N/A" && mobile != "N/A") "$countryCode $mobile" else "N/A"
            binding!!.number.text = fullPhoneNumber

            val firstName = data2?.first_name ?: "N/A"
            val lastName =data2?.last_name ?: "N/A"
            binding!!.tvName.text = "$firstName $lastName"

            binding!!.tvItemStudentsStudentId.text = "Student Id:${data2?.id ?: "N/A"}"

            App.singleton?.studentIdentifier = data2?.identifier
            App.sharedPre!!.saveString(AppConstants.USER_IDENTIFIER, data2?.identifier ?: "")
        }


        binding!!.fabFpNotificationStu.setOnClickListener {

            binding!!.root.findNavController().navigate(R.id.fragmentNotification)

        }





        binding!!.fabCreateApplication.setOnClickListener{

            binding!!.root.findNavController().navigate(R.id.createApplicationFragment)

        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    @SuppressLint("SetTextI18n")
    private fun hitApiUserDetails() {
        ViewModalClass().getStaffProfileData(
            requireActivity(),AppConstants.fiClientNumber, App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, ""
            )!!, "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity()) { staffData: StaffProfileModal? ->
            staffData?.let { nonNullForgetModal ->
                if (staffData.statusCode == 200) {
                    binding!!.tvName.text = listOfNotNull(
                        staffData.data?.userInfo?.first_name?.takeIf { it.isNotBlank() },
                        staffData.data?.userInfo?.last_name?.takeIf { it.isNotBlank() }
                    ).joinToString(" ")
                    val mobile = staffData.data!!.userInfo.mobile.toString() ?: ""
                    val countryCode = staffData.data.userInfo.country_code.toString()?: ""
                    val contact = "+$countryCode $mobile"

                    binding!!.tvEmail.text = staffData.data!!.userInfo.email
                    binding!!.number.text = contact
                    binding!!.tvItemStudentsStudentId.text = "Student Id:${staffData.data.studentProfileInfo.student_id}"

                    App.singleton?.studentIdentifier = staffData.data?.studentProfileInfo?.identifier

                    App.sharedPre!!.saveString(AppConstants.USER_IDENTIFIER,staffData.data?.studentProfileInfo?.identifier)
                } else {
                    CommonUtils.toast(requireActivity(), nonNullForgetModal.message ?: "Failed")
                }
            }
        }
    }

    private fun setApplicationActiveRecyclerview(context: Context) {
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.rvFaActive.layoutManager = layoutManager

        adapterApplicationsActive = AdapterApplicationsActive(context, applicationList)
        binding!!.rvFaActive.adapter = adapterApplicationsActive

        binding!!.rvFaActive.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && hasNextPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                        firstVisibleItemPosition >= 0) {
                        fetchDataFromApi(data)
                    }
                }
            }
        })

    }

    private fun fetchDataFromApi(data1: String?) {
        if (!hasNextPage || isLoading) return
        if (applicationList.isNotEmpty() && currentPage == 1) return

        isLoading = true
        if (currentPage == 1) {
            binding!!.pbFaActive.visibility = View.VISIBLE
        } else {
            binding!!.pbFaActivePagination.visibility = View.VISIBLE
        }
        viewModel.getApplicationResponseLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            currentPage,
            perPage,
            "desc",
            "id",
            data
        ).observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val programResponse = it.data?.records ?: emptyList()
                    applicationList.addAll(programResponse)
                    adapterApplicationsActive?.notifyDataSetChanged()

                    hasNextPage = it.data?.metaInfo?.hasNextPage ?: false
                    if (hasNextPage) {
                        currentPage++
                    }

                    binding!!.llFaActiveNoApplications.isVisible = applicationList.isEmpty()
                    binding!!.rvFaActive.isVisible = applicationList.isNotEmpty()
                } else {
                    CommonUtils.toast(requireContext(), it.message ?: "Failed")
                    binding!!.llFaActiveNoApplications.isVisible = true
                    binding!!.rvFaActive.isVisible = false
                }
            } ?: run {
                binding!!.llFaActiveNoApplications.isVisible = true
                binding!!.rvFaActive.isVisible = false
            }

            isLoading = false
            binding!!.pbFaActive.isVisible = false
            binding!!.pbFaActivePagination.isVisible = false
            binding!!.swipeRefreshLayout.isRefreshing = false

        }
    }

    override fun onResume() {
        super.onResume()



        val currentFlavor = BuildConfig.FLAVOR.lowercase()


        if (currentFlavor=="admisiony")
        {
            requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)

        }else{
            requireActivity().window.navigationBarColor = requireActivity().getColor(R.color.navigationBarColor)

        }


        if (applicationList.isNotEmpty()) {
            binding?.rvFaActive?.post {

                val layoutManager = binding?.rvFaActive?.layoutManager as? LinearLayoutManager
                layoutManager?.scrollToPositionWithOffset(recyclerViewPosition, 0)

            }
        } else {

            fetchDataFromApi(data)
        }

//        val imageUrl = App.sharedPre!!.getString(AppConstants.USER_IMAGE, "")!!.trim('"')
//        Glide.with(requireActivity())
//            .load(imageUrl).error(R.drawable.test_image)
//            .into(binding!!.civFApplications)
        if (BuildConfig.FLAVOR.equals("MavenConsulting", ignoreCase = true)) {
            if(App.sharedPre!!.getString(AppConstants.SCOUtLOGIN,"")=="true"){
                ScoutMainActivity.bottomNav!!.isVisible = false
                binding?.ablFd?.visibility=View.GONE
                binding?.fabCreateApplication?.isVisible=false

            }else{
                MainActivity.bottomNav!!.isVisible = true
                binding?.ablFd?.visibility=View.VISIBLE
                binding?.fabCreateApplication?.isVisible=false

            }
        } else {
            if(App.sharedPre!!.getString(AppConstants.SCOUtLOGIN,"")=="true"){
                ScoutMainActivity.bottomNav!!.isVisible = false
                binding?.ablFd?.visibility=View.GONE
                binding?.rrl?.visibility=View.VISIBLE
                binding?.fabCreateApplication?.isVisible=false

            }else{
                MainActivity.bottomNav!!.isVisible = true
                binding?.ablFd?.visibility=View.VISIBLE
                binding?.rrl?.visibility=View.GONE
                binding?.fabCreateApplication?.isVisible=true

            }
        }

    }

    override fun onPause() {
        super.onPause()
        val layoutManager = binding?.rvFaActive?.layoutManager as? LinearLayoutManager
        recyclerViewPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
    }
}
