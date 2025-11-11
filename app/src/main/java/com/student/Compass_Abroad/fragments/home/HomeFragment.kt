@file:Suppress("DEPRECATED_IDENTITY_EQUALS")

package com.student.Compass_Abroad.fragments.home

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.razorpay.Checkout
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils

import com.student.Compass_Abroad.activities.MainActivity.Companion.drawer

import com.student.Compass_Abroad.adaptor.AdapterModeOfPaymentVoucherSelector
import com.student.Compass_Abroad.adaptor.AdapterOffersandUpdate
import com.student.Compass_Abroad.adaptor.AdapterScholarships
import com.student.Compass_Abroad.adaptor.AdaptorVouchersRecyclerview
import com.student.Compass_Abroad.adaptor.AdaptorWebinarsRecyclerview
import com.student.Compass_Abroad.adaptor.AreaOfInterestAdaptor
import com.student.Compass_Abroad.adaptor.ProgramTagAdapter
import com.student.Compass_Abroad.adaptor.dashBoardAdapter.HomeInterestsAdapter
import com.student.Compass_Abroad.adaptor.dashBoardAdapter.HomeLatestUpdateAdapter
import com.student.Compass_Abroad.adaptor.dashBoardAdapter.HomeUniversitiesAdapter
import com.student.Compass_Abroad.adaptor.dashBoardAdapter.TopPreferCountryAdapter
import com.student.Compass_Abroad.databinding.FragmentHomeBinding

import com.student.Compass_Abroad.databinding.ProgramTagsDialogBinding
import com.student.Compass_Abroad.databinding.SliderDataLayoutBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.modal.AllProgramModel.AllProgramModel
import com.student.Compass_Abroad.modal.clientEventModel.ClientEventResponse
import com.student.Compass_Abroad.modal.clientEventModel.Record
import com.student.Compass_Abroad.modal.getDestinationCountryList.Data
import com.student.Compass_Abroad.modal.getOffersUpdatesModel.GetOffersandUpdates
import com.student.Compass_Abroad.modal.getScholarships.GetScholarships
import com.student.Compass_Abroad.modal.getVoucherModel.getVouchers
import com.student.Compass_Abroad.modal.getVoucherPaymentMode.getVoucherPaymentMode
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.modal.staffProfile.StaffProfileModal
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.student.Compass_Abroad.BuildConfig
import com.student.Compass_Abroad.InDemandCoursesAdapter
import com.student.Compass_Abroad.LatestUpdateAdapter
import com.student.Compass_Abroad.QuickActionsAdapter
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.StudentTestimonialsAdapter
import com.student.Compass_Abroad.TopDestinationAdapter
import com.student.Compass_Abroad.TopDestinationModel
import com.student.Compass_Abroad.TopInDemandIntuitionsAdapter
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.activities.MainActivity

import com.student.Compass_Abroad.adaptor.AdapterProgramsAllProg
import com.student.Compass_Abroad.adaptor.AdaptorWebinarRecyclerview
import com.student.Compass_Abroad.adaptor.bannerSlider.SliderAdapter
import com.student.Compass_Abroad.databinding.EducationloanBinding
import com.student.Compass_Abroad.modal.getBannerModel.getBannerModel
import com.student.Compass_Abroad.modal.getWebinars.getWebinarsResponse
import com.student.Compass_Abroad.modal.refreshToken.RefreshTokenResonse
import com.student.Compass_Abroad.retrofit.ApiInterface
import com.student.Compass_Abroad.retrofit.LoginViewModal
import com.student.Compass_Abroad.retrofit.RetrofitClient12
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.math.abs
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.NestedScrollView
import com.student.Compass_Abroad.StaticLatestUpdate
import com.student.Compass_Abroad.StaticLatestUpdateAdapter
import com.student.Compass_Abroad.StaticTestimonial
import com.student.Compass_Abroad.StudentStaticTestimonialsAdapter
import com.student.Compass_Abroad.databinding.DialogBuyCouponBinding

@Suppress("DEPRECATION")
class HomeFragment : Fragment(), AdapterProgramsAllProg.select,
    AdapterOffersandUpdate.OnClickData, AdapterScholarships.Scholarships,
    AdaptorWebinarRecyclerview.select {
    var binding: FragmentHomeBinding? = null
    var interestsAdapter: HomeInterestsAdapter? = null
    var fetchLatestAdapter: HomeLatestUpdateAdapter? = null
    var fetchUniversitiesAdapter: HomeUniversitiesAdapter? = null
    var arrayList1 = ArrayList<Record>()
    var webinarsList1 = ArrayList<com.student.Compass_Abroad.modal.getWebinars.Record>()
    var arrayListVouchers = ArrayList<com.student.Compass_Abroad.modal.getVoucherModel.Record>()
    var arrayListTopDestinations =
        ArrayList<com.student.Compass_Abroad.modal.top_destinations.Data>()
    var arrayListInDemandInstitution =
        ArrayList<com.student.Compass_Abroad.modal.in_demandInstitution.Data>()
    var arrayListInStudentTestimonials =
        ArrayList<com.student.Compass_Abroad.modal.getTestimonials.Row>()

    var arrayListInLatestUpdate =
        ArrayList<com.student.Compass_Abroad.modal.getTestimonials.Row>()
    var arrayListInDemand = ArrayList<com.student.Compass_Abroad.modal.inDemandCourse.Data>()
    private val modeOfPaymentList: MutableList<com.student.Compass_Abroad.modal.getVoucherPaymentMode.RecordsInfo> =
        mutableListOf()
    var dataPerPage = 20
    var presentPage: Int = 1
    var apiInterface = RetrofitClient12.retrofitCallerObject11!!.create(ApiInterface::class.java)
    private var adaptorClientEvents: AdaptorWebinarsRecyclerview? = null
    private var adaptorwebinars: AdaptorWebinarRecyclerview? = null
    private var adaptorVouchers: AdaptorVouchersRecyclerview? = null
    var nextPage: Int = 0
    var isScrolling = false
    var currentVisibleItems = 0
    var totalItemsInAdapter: Int = 0
    var scrolledOutItems: Int = 0
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterProgramsAllProg: AdapterProgramsAllProg
    var topPreferCountryProgram: TopPreferCountryAdapter? = null
    private val recordInfoList: ArrayList<Data> = ArrayList()
    private lateinit var viewModel: ViewModalClass
    var secretKey: String = ""
    private lateinit var paymentSheet: PaymentSheet
    private lateinit var dialog: Dialog
    private var disciplineAdapter: AreaOfInterestAdaptor? = null
    private val allDisciplineList =
        mutableListOf<com.student.Compass_Abroad.modal.preferCountryList.Data>()
    private var isLoading = false

    private var scrollYPosition = 0
    private lateinit var nestedScrollView: NestedScrollView
    private var contentKey = ""
    private var dataPerPage1 = 6
    private var presentPage1 = 1
    private var dataPerPage2 = 6
    private var presentPage2 = 1
    private var nextPage1 = 0

    var token: String? = null
    private var payment_gateway_identifier = ""
    private var arrayList = ArrayList<com.student.Compass_Abroad.modal.AllProgramModel.Record>()
    private var arrayListBanner =
        ArrayList<com.student.Compass_Abroad.modal.getBannerModel.Record>()
    private var identityInfo: com.student.Compass_Abroad.modal.staffProfile.Data? = null

    private val sliderHandler: Handler = Handler()
    private val sliderItems = mutableListOf<String>()
    private val sliderRunnable: Runnable =
        Runnable { binding!!.viewPagerImageSlider.currentItem += 1 }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE //

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }



        setupRecyclerViewTopDestination()
        setupRecyclerViewInDemand()
        setupRecyclerViewInDemandIntuitions()
        setupRecyclerViewStudentTestimonials()
        setupRecyclerLatestUpdates()
        setQuickActionsAdapter()

        return binding!!.root
    }


    private fun setQuickActionsAdapter() {
        val actions = listOf(
            TopDestinationModel("Education Loan", R.drawable.loan),
            TopDestinationModel("Visa Application", R.drawable.visa),
            TopDestinationModel("Find Accommodation", R.drawable.accommodation),
            TopDestinationModel("IELTS Booking", R.drawable.ielts),
            TopDestinationModel("SOP Guidance", R.drawable.soap),
            TopDestinationModel("My Applications", R.drawable.applica)
        )

        val adapter =
            QuickActionsAdapter(actions, object : QuickActionsAdapter.QuickActionClickListener {
                override fun onQuickActionClick(item: TopDestinationModel) {
                    when (item.name) {
                        "Education Loan" -> findNavController().navigate(R.id.educationLoanFragment)
                        "Visa Application" -> showInfoBottomSheet()
                        "Find Accommodation" -> findNavController().navigate(R.id.webViewButton)
                        "IELTS Booking" -> showInfoBottomSheet()
                        "SOP Guidance" -> showInfoBottomSheet()
                        "My Applications" -> findNavController().navigate(R.id.createApplicationFragment)
                    }
                }
            })

        binding!!.rvQuciAction.layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
        binding!!.rvQuciAction.adapter = adapter
    }
    private fun setupRecyclerViewInDemandIntuitions() {
        val deviceId = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""
        val token = "Bearer ${CommonUtils.accessToken}"
        LoginViewModal().get_in_demandInstitution(
            requireActivity(),
            AppConstants.fiClientNumber,
            deviceId, token
        ).observe(viewLifecycleOwner) { response ->
            response?.let { topDestinations ->
                if (topDestinations.statusCode == 200) {
                    val destinations = topDestinations.data
                    if (!destinations.isNullOrEmpty()) {
                        arrayListInDemandInstitution.clear()
                        arrayListInDemandInstitution.addAll(destinations)
                        binding?.rvIndemandIntuitions?.apply {
                            layoutManager = LinearLayoutManager(
                                requireContext(), LinearLayoutManager.HORIZONTAL, false
                            )
                            adapter =
                                TopInDemandIntuitionsAdapter(arrayListInDemandInstitution) { selectedItem ->


                                }
                        }
                    }

                } else {
                    val errorMsg = topDestinations.message ?: "Failed"
                    if (!errorMsg.contains("Access token expired", ignoreCase = true)) {
                        CommonUtils.toast(requireActivity(), errorMsg)
                    }
                }
            }
        }
    }
    private fun setupRecyclerViewInDemand() {
        val deviceId = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""
        val token = "Bearer ${CommonUtils.accessToken}"
        LoginViewModal().get_in_demandCourses(
            requireActivity(),
            AppConstants.fiClientNumber,
            deviceId,
            token
        ).observe(viewLifecycleOwner) { response ->
            response?.let { topDestinations ->
                if (topDestinations.statusCode == 200) {
                    val destinations = topDestinations.data
                    if (!destinations.isNullOrEmpty()) {
                        arrayListInDemand.clear()
                        arrayListInDemand.addAll(destinations)
                        binding?.rvIndemand?.apply {
                            layoutManager = LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                            adapter = InDemandCoursesAdapter(
                                arrayListInDemand,
                                object : InDemandCoursesAdapter.OnCourseClickListener {
                                    override fun onItemClick(
                                        data: com.student.Compass_Abroad.modal.inDemandCourse.Data,
                                        position: Int
                                    ) {

                                    }

                                    override fun onLikeClick(
                                        data: com.student.Compass_Abroad.modal.inDemandCourse.Data,
                                        position: Int
                                    ) {

                                        val hexString = generateRandomHexString(16)
                                        val publicKey = hexString
                                        val privateKey = AppConstants.privateKey

                                        val formData = JSONObject().apply {
                                            put(
                                                "program_campus_identifier",
                                                data.program_campus_identifier
                                            )
                                        }
                                        val dataToEncrypt = formData.toString()
                                        val appSecret = AppConstants.appSecret
                                        val ivHexString = "$privateKey$publicKey"
                                        val encryptedString =
                                            encryptData(dataToEncrypt, appSecret, ivHexString)

                                        if (encryptedString != null) {
                                            contentKey = "$publicKey^#^$encryptedString"
                                            Log.d("shortlisted", contentKey)
                                            addToShortlist(requireActivity(), contentKey)
                                        } else {
                                            Log.d("shortlisted", "Encryption failed.")
                                        }

                                    }

                                    override fun onDislikeClick(
                                        data: com.student.Compass_Abroad.modal.inDemandCourse.Data,
                                        position: Int
                                    ) {

                                        val hexString = generateRandomHexString(16)
                                        val publicKey = hexString
                                        val privateKey = AppConstants.privateKey

                                        val formData = JSONObject().apply {
                                            put(
                                                "program_campus_identifier",
                                                data.program_campus_identifier
                                            )
                                        }
                                        val dataToEncrypt = formData.toString()
                                        val appSecret = AppConstants.appSecret
                                        val ivHexString = "$privateKey$publicKey"
                                        val encryptedString =
                                            encryptData(dataToEncrypt, appSecret, ivHexString)

                                        if (encryptedString != null) {
                                            contentKey = "$publicKey^#^$encryptedString"
                                            Log.d("shortlisted", contentKey)
                                            // removeFromShortlist(data)
                                            addToShortlist(requireActivity(), contentKey)
                                        } else {
                                            Log.d("shortlisted", "Encryption failed.")
                                        }

                                    }
                                }
                            )
                        }
                    }

                } else {
                    val errorMsg = topDestinations.message ?: "Failed"
                    if (!errorMsg.contains("Access token expired", ignoreCase = true)) {
                        CommonUtils.toast(requireActivity(), errorMsg)
                    }
                }
            }
        }
    }
    private fun setupRecyclerViewTopDestination() {
        val deviceId = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""
        val token = "Bearer ${CommonUtils.accessToken}"

        LoginViewModal().get_topdestination(
            requireActivity(),
            AppConstants.fiClientNumber,
            deviceId,
            token
        ).observe(viewLifecycleOwner) { response ->

            response?.let { topDestinations ->
                if (topDestinations.statusCode == 200) {

                    val destinations = topDestinations.data

                    if (!destinations.isNullOrEmpty()) {
                        arrayListTopDestinations.clear()
                        arrayListTopDestinations.addAll(destinations)
                        binding?.rvTopDestination?.apply {
                            layoutManager =
                                LinearLayoutManager(
                                    requireContext(),
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                            adapter =
                                TopDestinationAdapter(arrayListTopDestinations) { selectedItem ->

                                }
                        }
                    }

                } else {
                    val errorMsg = topDestinations.message ?: "Failed"
                    if (!errorMsg.contains("Access token expired", ignoreCase = true)) {
                        CommonUtils.toast(requireActivity(), errorMsg)
                    }
                }
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Your logic
        arrayList1.clear()
        identityInfo = null
        arrayListVouchers.clear()



        onGetClientEvent(requireActivity(), dataPerPage, presentPage)
        onGetVouchers(requireActivity(), dataPerPage1, presentPage1)
        getOffersandUpdatesIn(requireActivity())
        getScholarships(requireActivity())
        GetWebinars(requireActivity(), dataPerPage2, presentPage2)

        arrayList1.clear()

        initPayment()
        onGetRecommendedAllPrograms(dataPerPage, presentPage)
        setupRecyclerViewRecommended()

        Log.d(
            "SelectedCountry_sharepref",
            sharedPre!!.getString(AppConstants.USER_DISCIPLINES, "").toString()
        )

        MainActivity.bottomNav
        sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")
            ?.let { Log.d("onCreateViewLogin", it) }
        CommonUtils.accessToken?.let { Log.d("onCreateViewLogin", it) }
        CommonUtils.refreshToken?.let { Log.d("onCreateViewLogin", it) }
        AppConstants.fiClientNumber.let { Log.d("onCreateViewLogin", it) }
        sharedPre?.getString(AppConstants.User_IDENTIFIER, "")
            ?.let { Log.d("onCreateViewLogin", it) }

        sharedPre?.getString(AppConstants.USER_IDENTIFIER, "")
            ?.let { Log.d("onCreateViewLogin", it) }

        onBackPressed()
        interestProgram()
        fetchLatestAdapter()

        setupViewModel()
        setRecyclerView()


        if (BuildConfig.FLAVOR == "MavenConsulting") {
            binding?.fabCreateApplication?.visibility = View.GONE
        } else {
            binding?.fabCreateApplication?.visibility = View.GONE
        }

        val currentFlavor = BuildConfig.FLAVOR.lowercase()



        if (currentFlavor == "eduways") {

            binding!!.fabFdStuCoordinatorCall.visibility = View.INVISIBLE
        }


        if (getString(R.string.app_name).trim().equals("Admissiony.com", ignoreCase = true)) {

            recordInfoList.clear()
            saveSelectedToSharedPreferences(AppConstants.CountryList, "230", "United Kingdom")

            val json = loadJSONFromAsset(requireContext(), "countries_admissiony.json")
            val gson = Gson()
            val type = object : TypeToken<List<Data>>() {}.type
            val countryList: List<Data> = gson.fromJson(json, type)

            recordInfoList.addAll(countryList)
            topPreferCountryProgram?.notifyDataSetChanged()

        } else {
            fetchDataFromApi()

        }


        onClicks()


        getDisciplineList()


        getBanner()
        fetchUniversitiesAdapter()


        FirebaseMessaging.getInstance().token.addOnCompleteListener { task: Task<String> ->
            if (!task.isSuccessful) {
                Log.w(
                    ContentValues.TAG,
                    "Fetching FCM registration token failed",
                    task.exception
                )
                return@addOnCompleteListener
            }
            token = task.result

            Log.d("onCreateViewLoginToken", token.toString())
        }

        sharedPre?.saveString(AppConstants.SCOUtLOGIN, "false")
        binding!!.switchStu.isChecked = false
        var isCurrentRoleScout = false
        binding!!.switchStu.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked == isCurrentRoleScout) {
                binding!!.switchStu.isChecked = isCurrentRoleScout
                return@setOnCheckedChangeListener
            }
            clearFilter()

            val message =
                if (isChecked) "Switching to Scout dashboard..." else "Switching to Student dashboard..."
            CommonUtils.progressDialog(requireActivity(), null, message, 1000)

            val identityInfoList = identityInfo?.userInfo?.identityInfo
            val activeIdentifier = identityInfo?.activeUserIdentityInfo?.identifier

            if (identityInfoList != null && activeIdentifier != null) {
                val otherIdentities =
                    identityInfoList.filter { it.identifier != activeIdentifier }.take(2)

                Handler(Looper.getMainLooper()).postDelayed({
                    CommonUtils.progressDialogueDismiss()

                    if (otherIdentities.isNotEmpty()) {
                        val identity = otherIdentities.first()

                        CoroutineScope(Dispatchers.IO).launch {
                            val refreshResponse = refreshTokenApi(identity.identifier, context)
                            withContext(Dispatchers.Main) {
                                if (refreshResponse?.statusCode == 200) {
                                    val newToken = refreshResponse.data?.tokensInfo?.accessToken
                                    sharedPre?.saveString(AppConstants.ACCESS_TOKEN, newToken)

                                    val isStudent =
                                        identity.name.contains("Student", ignoreCase = true)
                                    val intent = if (isStudent) {
                                        FragProgramAllProg.selectedTab = "recommended"
                                        Intent(requireActivity(), MainActivity::class.java)
                                    } else {
                                        FragProgramAllProg.selectedTab = "all"
                                        Intent(requireActivity(), ScoutMainActivity::class.java)
                                    }

                                    startActivity(intent)
                                    requireActivity().finish()
                                    hitApiUserDetails()
                                } else {
                                    handleRefreshTokenError(
                                        refreshResponse?.statusCode,
                                        refreshResponse?.message
                                    )
                                }
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Other role identifier not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, 2000)
            } else {
                Toast.makeText(requireContext(), "Identity info not found", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }
    private fun clearFilter() {

        sharedPre!!.clearKeyLabelValue(AppConstants.PGWP_KEY)
        sharedPre!!.clearKeyLabelValue(AppConstants.ATTENDANCE_KEY)
        sharedPre!!.clearKeyLabelValue(AppConstants.Accomodation)
        sharedPre!!.clearKeyLabelValue(AppConstants.PROGRAM_TYPE_KEY)
        sharedPre!!.clearKey(AppConstants.MIN_TUTION_KEY)
        sharedPre!!.clearKey(AppConstants.MAX_TUTION_KEY)
        sharedPre!!.clearKey(AppConstants.MIN_APPLICATION_KEY)
        sharedPre!!.clearKey(AppConstants.MAX_APPLICATION_KEY)
        sharedPre!!.clearKey(AppConstants.AgeList)
        sharedPre!!.clearKey(AppConstants.EnglishLevelList)

        clearAllSelectedValues()

    }
    private fun clearAllSelectedValues() {
        // Call this method for each key prefix you use
        clearSelectedValuesFromSharedPreferences(AppConstants.CountryList)
        clearSelectedValuesFromSharedPreferences(AppConstants.StateList)
        clearSelectedValuesFromSharedPreferences(AppConstants.CityList)
        clearSelectedValuesFromSharedPreferences(AppConstants.institutionList)
        clearSelectedValuesFromSharedPreferences(AppConstants.studyLevelList)
        clearSelectedValuesFromSharedPreferences(AppConstants.disciplineList)
        clearSelectedValuesFromSharedPreferences(AppConstants.IntakeList)
    }
    fun clearSelectedValuesFromSharedPreferences(keyPrefix: String) {
        val sharedPrefs = SharedPrefs(requireContext())
        sharedPrefs.clearStringList("${keyPrefix}Id")
        sharedPrefs.clearStringList("${keyPrefix}Label")
    }
    private fun getDisciplineList() {
        ViewModalClass().getDisciplineDataList(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { response ->
            if (response?.success == true) {
                response.data?.let {
                    allDisciplineList.clear()
                    binding!!.noAreaOfInterestFound.visibility = View.GONE
                    allDisciplineList.addAll(it)

                    disciplineAdapter = AreaOfInterestAdaptor(
                        requireContext(),
                        allDisciplineList,
                        object : AreaOfInterestAdaptor.Select {
                            override fun selectItemFilter(data: com.student.Compass_Abroad.modal.preferCountryList.Data) {
                                AppConstants.PROGRAM_STATUS = "1"
                                FragProgramAllProg.selectedTab = "all"
                                binding!!.root.findNavController().navigate(R.id.fragProgramAllProg)

                            }
                        }, 9
                    )

                    binding!!.rvAreaOfInterest.apply {
                        layoutManager = GridLayoutManager(requireContext(), 3)
                        adapter = disciplineAdapter

                    }
                }
            } else {

                binding!!.noAreaOfInterestFound.visibility = View.VISIBLE

            }
        }
    }
    private fun setUserData(staffData: StaffProfileModal) {
        sharedPre!!.saveString(AppConstants.FIRST_NAME, staffData.data?.userInfo?.first_name)
        sharedPre!!.saveString(AppConstants.LAST_NAME, staffData.data!!.userInfo.last_name)
        sharedPre!!.saveString(AppConstants.DOB, staffData.data!!.userInfo.birthday)

        val firstName = staffData.data?.userInfo?.first_name ?: ""
        val lastName = staffData.data?.userInfo?.last_name ?: ""

        //binding!!.name.text = "Hi, " + "$firstName $lastName"

        val profilePictureUrl = staffData.data?.userInfo?.profile_picture_url

//        if (!profilePictureUrl.isNullOrEmpty()) {
//            Glide.with(this)
//                .load(profilePictureUrl)
//                .into(binding!!.civProfileImageFd2)
//        } else {
//            binding?.civProfileImageFd2?.setImageResource(R.drawable.test_image)
//        }

        sharedPre!!.saveModel(AppConstants.USER_IMAGE, profilePictureUrl)

    }
    private fun onClicks() {


        val currentFlavor = BuildConfig.FLAVOR.lowercase()

        Log.d("currentFlavor", currentFlavor)

        when (currentFlavor) {
            "firmli", "admisiony", "Compass Abroad" -> {

                binding?.quickActions?.visibility = View.VISIBLE
                binding?.tvWebinars?.visibility = View.VISIBLE
                binding?.btnWebinars?.visibility = View.VISIBLE
                binding?.rvWebinars?.visibility = View.VISIBLE

            }

            else -> {

                binding?.tvReferEarn?.visibility = View.GONE
                binding?.cdReferEarn?.visibility = View.GONE
                binding?.cdBecomeaScout?.visibility = View.GONE
                binding?.quickActions?.visibility = View.GONE
                binding?.quickActions?.visibility = View.GONE
                binding?.tvWebinars?.visibility = View.GONE
                binding?.btnWebinars?.visibility = View.GONE
                binding?.rvWebinars?.visibility = View.GONE

            }
        }

        binding!!.btnWebinars.setOnClickListener {

            binding!!.root.findNavController().navigate(R.id.fragmentWebinars)
        }


        binding!!.civProfileImageFd2.setOnClickListener {

            drawer!!.open()

        }


        binding!!.fabFpHeart.setOnClickListener {

            sharedPre!!.saveString("category", "higher_education")
            binding!!.root.findNavController().navigate(R.id.shortListedFragment)

        }


        binding!!.btnFdStudentViewWeb.setOnClickListener {

            binding!!.root.findNavController().navigate(R.id.fragmentClientEvents)

        }
        binding!!.btnFdStudentDes.setOnClickListener {

            binding!!.root.findNavController().navigate(R.id.areaOfInterestFragment)
        }



        binding!!.btnVoucher.setOnClickListener {

            binding!!.root.findNavController().navigate(R.id.fragmentVouchers)

        }

        binding!!.fabFpNotificationStu.setOnClickListener {

            binding!!.root.findNavController().navigate(R.id.fragmentNotification)

        }

        binding!!.btnFdStudentViewAllRecomPro.setOnClickListener {
            AppConstants.PROGRAM_STATUS = "1"

            binding!!.root.findNavController().navigate(R.id.fragProgramAllProg)
        }
        binding!!.btnLogin.setOnClickListener {
            AppConstants.PROGRAM_STATUS = "1"
            binding!!.root.findNavController().navigate(R.id.fragProgramAllProg)

        }

        binding!!.fabCreateApplication.setOnClickListener {

            binding!!.root.findNavController().navigate(R.id.createApplicationFragment)

        }

        binding!!.viewAlldestination.setOnClickListener {
            binding!!.root.findNavController().navigate(R.id.topDestinationFragment)
        }

        binding!!.viewAllIndemand.setOnClickListener {
            binding!!.root.findNavController().navigate(R.id.inDemandCoursesFragment)
        }

        binding!!.viewAllIndemandIntuitions.setOnClickListener {
            binding!!.root.findNavController().navigate(R.id.inDemandInstitution)
        }

        binding!!.btnTestimonials.setOnClickListener {
            binding!!.root.findNavController().navigate(R.id.studentTestimonials)
        }

        binding!!.btnLatestUpdates.setOnClickListener {
            binding!!.root.findNavController().navigate(R.id.latestUpdateFragment)
        }


    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ViewModalClass::class.java]
    }
    private fun initPayment() {

        paymentSheet = PaymentSheet(this) {

                paymentSheetResult: PaymentSheetResult? ->

            paymentSheetResult?.let {
                onPaymentResult(
                    it, dialog
                )
            }
        }
    }
    private fun setRecyclerView() {
        topPreferCountryProgram = TopPreferCountryAdapter(
            requireActivity(),
            recordInfoList,
            object : TopPreferCountryAdapter.select {
                override fun onClick() {
                    AppConstants.PROGRAM_STATUS = "1"
                    FragProgramAllProg.selectedTab = "all"
                    binding!!.root.findNavController().navigate(R.id.fragProgramAllProg)
                }

            })
        binding?.rvPreferredCountriesStu?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvPreferredCountriesStu?.adapter = topPreferCountryProgram
    }
    private fun fetchDataFromApi() {
        viewModel.getDestinationCountryLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}"
        ).observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.statusCode == 200 && it.success) {
                    val newRecords = it.data ?: emptyList()

                    recordInfoList.clear()
                    recordInfoList.addAll(newRecords)
                    topPreferCountryProgram?.notifyDataSetChanged()

                    Log.d("API_Response", "Record list size after update: ${recordInfoList.size}")
                }
            }
        }
    }
    private fun onGetClientEvent(
        requireActivity: FragmentActivity,
        presentPage: Int,
        dataPerPage: Int,
    ) {
        ViewModalClass().clientEventsModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, dataPerPage, presentPage
        ).observe(requireActivity()) { clientEventModal: ClientEventResponse? ->
            clientEventModal?.let { nonNullForgetModal ->
                if (clientEventModal.statusCode == 200) {
                    if (clientEventModal.data?.records.isNullOrEmpty()) {
                        binding!!.rvCE.visibility = View.GONE
                        binding!!.noWbinarFound.visibility = View.GONE
                    } else {
                        binding!!.rvCE.visibility = View.VISIBLE
                        binding!!.noWbinarFound.visibility = View.GONE
                        arrayList1.addAll(clientEventModal.data!!.records)
                        setClientEventsRecyclerview(arrayList1)
                    }
                } else {
                    val errorMessage = nonNullForgetModal.message ?: "Failed"

                    if (!errorMessage.contains("Access token expired", ignoreCase = true)) {
                        CommonUtils.toast(requireActivity(), errorMessage)
                    }
                }
            }
        }
    }

    private fun onGetVouchers(
        activity: FragmentActivity,   // avoid naming it `requireActivity` to prevent confusion
        presentPage: Int,
        dataPerPage: Int,
    ) {
        // show loading state if you have one (optional)
        // binding?.progressVoucher?.visibility = View.VISIBLE

        ViewModalClass().getVouchersModalLiveData(
            activity,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken,
            dataPerPage,
            presentPage
        ).observe(viewLifecycleOwner) { getVouchersModal: getVouchers? ->

            // hide loading
            // binding?.progressVoucher?.visibility = View.GONE

            if (getVouchersModal == null) {
                // Null response -> hide voucher area
                binding?.apply {
                    rvVoucher.visibility = View.GONE
                    view122.visibility = View.GONE
                    relativeVoucher?.visibility = View.GONE    // if you have this view
                }
                return@observe
            }

            if (getVouchersModal.statusCode == 200) {
                val records = getVouchersModal.data?.records
                if (records.isNullOrEmpty()) {
                    // No vouchers -> hide recycler and header/divider, show 'no vouchers'
                    binding?.apply {
                        rvVoucher.visibility = View.GONE
                        view122.visibility = View.GONE
                        relativeVoucher?.visibility = View.GONE
                    }
                } else {
                    // We have data -> show recycler and header/divider, hide 'no vouchers'
                    binding?.apply {
                        rvVoucher.visibility = View.VISIBLE
                        view122.visibility = View.VISIBLE
                        relativeVoucher?.visibility = View.VISIBLE
                    }

                    // update list safely
                    arrayListVouchers.clear()
                    arrayListVouchers.addAll(records)

                    setVouchersRecyclerview(arrayListVouchers)
                }
            } else {
                // API returned error -> hide voucher area and show toast
                binding?.apply {
                    rvVoucher.visibility = View.GONE
                    view122.visibility = View.GONE
                    relativeVoucher?.visibility = View.GONE
                }

                val errorMessage = getVouchersModal.message ?: "Failed"
                if (!errorMessage.contains("Access token expired", ignoreCase = true)) {
                    CommonUtils.toast(activity, errorMessage)
                }
            }
        }
    }



    private fun setVouchersRecyclerview(arrayList1: ArrayList<com.student.Compass_Abroad.modal.getVoucherModel.Record>) {
        adaptorVouchers =
            AdaptorVouchersRecyclerview(
                this.requireActivity(),
                arrayList1,
                arrayList1.size,
                object : AdaptorVouchersRecyclerview.Select {
                    @RequiresApi(Build.VERSION_CODES.P)
                    override fun select(
                        data: com.student.Compass_Abroad.modal.getVoucherModel.Record,
                        position1: Int,
                    ) {
                        BuyVoucherDialog(requireActivity(), data)
                    }

                })
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding!!.rvVoucher.layoutManager = layoutManager
        binding!!.rvVoucher.adapter = adaptorVouchers

        binding!!.rvVoucher.addOnScrollListener(
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
                        if (presentPage1 < nextPage1) {
                            presentPage1 += 1

                            Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                                override fun run() {
                                    Handler().removeCallbacks(this, null)
                                    // onGetVouchers(requireActivity(), dataPerPage, presentPage)
                                }
                            }, 2000)
                        }
                    }
                }
            })

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun BuyVoucherDialog(
        requireActivity: FragmentActivity,
        data1: com.student.Compass_Abroad.modal.getVoucherModel.Record
    ) {
        dialog = Dialog(requireContext())
        val binding = DialogBuyCouponBinding.inflate(LayoutInflater.from(requireContext()))
        dialog.setContentView(binding.root)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        payment_gateway_identifier = ""

        val currency = data1.display_currency.uppercase() ?: "USD"
        val unitPrice = data1.display_amount?.toDouble() ?: 200.0

        with(binding) {
            tvCourseName.text = data1.name
            tvCourseDesc.text = data1.description
            tvUnitPrice.text = "$currency ${String.format("%,.2f", unitPrice)}"
            tvUnitPriceStatic.text = "$currency ${String.format("%,.2f", unitPrice)}"
            tvAvailableVoucher.text = "Available Vouchers: ${data1.available_voucher ?: 0}"
            etQuantity.setText("1")

            // Set initial total
            val initialQty = etQuantity.text.toString().toIntOrNull() ?: 0
            tvTotal.text = "$currency ${String.format("%,.2f", initialQty * unitPrice)}"

            // Load image using Glide
            Glide.with(requireActivity)
                .load(data1.file_url)
                .placeholder(R.drawable.logo)
                .into(imgCourse)

            // Payment mode dropdown
            getModeOfPaymentDropdown(requireActivity, spinnerGateway)

            // Quantity change listener
            etQuantity.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val qty = s?.toString()?.toIntOrNull() ?: 0
                    val total = qty * unitPrice
                    tvTotal.text = "$currency ${String.format("%,.2f", total)}"
                }

                override fun afterTextChanged(s: Editable?) {}
            })

            btnBuyNow.setOnClickListener {
                val quantity = etQuantity.text.toString().toIntOrNull()
                val selectedGateway = payment_gateway_identifier.toString()

                if (quantity == null || quantity <= 0) {
                    Toast.makeText(requireActivity, "Enter a valid quantity", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                if (selectedGateway.isEmpty() || selectedGateway == "Select") {
                    Toast.makeText(
                        requireActivity,
                        "Please select a payment gateway",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                // API Call
                generatingPaymentLinkVoucher(
                    requireActivity,
                    data1.base_currency,
                    data1.base_amount.toString(),
                    quantity.toString(),
                    data1.identifier,
                    "FTY1734081664533I24VMFUF52",
                    selectedGateway,
                    dialog
                )
            }
        }

        dialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun PaymentModeSpinner(paymentSpinner: TextView) {
        var fragment: Fragment? = null

        paymentSpinner?.setOnClickListener { v: View ->

            val popupWindow = PopupWindow(requireActivity())
            val layout: View =
                LayoutInflater.from(requireContext()).inflate(R.layout.custom_popup2, null)
            popupWindow.contentView = layout

            layout.requireViewById<TextView>(R.id.etSelect).setHint("Search Payment Mode")

            popupWindow.setBackgroundDrawable(Color.WHITE.toDrawable())
            popupWindow.isOutsideTouchable = true
            popupWindow.isFocusable = true
            popupWindow.elevation = 5f
            popupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
            popupWindow.width = paymentSpinner.width

            val locationOnScreen = IntArray(2)
            if (fragment != null) {
                paymentSpinner.getLocationOnScreen(locationOnScreen)

            } else {
                view?.getLocationOnScreen(locationOnScreen)

            }

            val recyclerView = layout.findViewById<RecyclerView>(R.id.rvSelect)
            recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            val adapter =
                AdapterModeOfPaymentVoucherSelector(requireActivity(), modeOfPaymentList, layout)
            recyclerView.adapter = adapter

            adapter.onItemClickListener = { selectedModeOfPayment ->
                paymentSpinner?.text = selectedModeOfPayment.label
                payment_gateway_identifier = selectedModeOfPayment.value
                popupWindow.dismiss()
                // You can perform additional actions based on the selected country here
            }
// Show the popup window
            popupWindow.showAsDropDown(paymentSpinner)


            //search edit text
            layout.requireViewById<EditText>(R.id.etSelect)
                .addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int,
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int,
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        val enteredText = s.toString()
                        adapter.getFilter().filter(enteredText)
                    }
                })
        }
    }

    private fun setClientEventsRecyclerview(arrayList1: ArrayList<Record>) {
        adaptorClientEvents =
            AdaptorWebinarsRecyclerview(this.requireActivity(), arrayList1, arrayList1.size)
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding!!.rvCE.layoutManager = layoutManager
        binding!!.rvCE.adapter = adaptorClientEvents

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

    private fun hitApiUserDetails() {


        Log.e("Client Number", AppConstants.fiClientNumber)   // Log client number
        Log.e(
            "Device Identifier",
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").toString()
        )  // Log device identifier
        Log.e("Access Token", "Bearer ${CommonUtils.accessToken}")


        ViewModalClass().getStaffProfileData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity()) { staffData: StaffProfileModal? ->
            staffData?.let { nonNullForgetModal ->
                if (staffData.statusCode == 200) {

                    val size = staffData?.data!!.userInfo.identityInfo.size


                    val currentFlavor = BuildConfig.FLAVOR.lowercase()

                    Log.d("hitApiUserDetails", currentFlavor)

                    when (currentFlavor) {
                        "admisiony", "firmli", "eeriveurope", "compassabroad", "studiepoint", "unitedglobalservices" -> {

                            if (size > 1) {
                                binding?.switchStu?.visibility = View.VISIBLE

                            } else {

                                binding?.switchStu?.visibility = View.GONE
                            }
                        }

                        else -> {
                            binding?.switchStu?.visibility = View.GONE

                            binding?.switchStu?.visibility = View.GONE

                            binding?.layoutSlider?.visibility = View.GONE
                        }
                    }

                    Log.d("onCreateViewDataInfo", size.toString())

                    setUserData(staffData)
                    val firstName =
                        staffData.data?.studentProfileInfo?.assignedStaffInfo?.first_name ?: ""
                    val lastName =
                        staffData.data?.studentProfileInfo?.assignedStaffInfo?.last_name ?: ""

                    val fullName = when {
                        firstName.isNotEmpty() && lastName.isNotEmpty() -> "$firstName $lastName"
                        firstName.isNotEmpty() -> firstName
                        lastName.isNotEmpty() -> lastName
                        else -> "N/A"
                    }


                    sharedPre!!.saveString(
                        AppConstants.USER_ID,
                        staffData.data.studentProfileInfo?.student_id.toString()
                    )

                    identityInfo = staffData.data
                    binding?.tvFdStuCoordinatorName?.text = fullName  ?: ""

                    binding!!.emailId.text =
                        staffData.data?.studentProfileInfo?.assignedStaffInfo?.email ?: ""

                    binding!!.fabFdStuCoordinatorCall.setOnClickListener {
                        val phoneNumber =
                            staffData.data?.studentProfileInfo?.assignedStaffInfo?.mobile?.toString()
                        val countryCode =
                            staffData.data?.studentProfileInfo?.assignedStaffInfo?.country_code?.toString()

                        if (!phoneNumber.isNullOrEmpty() && !countryCode.isNullOrEmpty()) {
                            // Ensure the country code starts with '+'
                            val formattedCountryCode =
                                if (countryCode.startsWith("+")) countryCode else "+$countryCode"
                            val fullNumber = formattedCountryCode + phoneNumber

                            val phoneIntent =
                                Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", fullNumber, null))
                            if (phoneIntent.resolveActivity(requireActivity().packageManager) != null) {
                                startActivity(phoneIntent)
                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "No app available to place a call",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Phone number is invalid",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                    binding!!.fabFdStuCoordinatorChat.setOnClickListener { v: View ->
                        App.singleton?.applicationIdentifierChat =
                            staffData.data!!.studentProfileInfo.identifier
                        App.singleton?.chatStatus = "1"
                        App.singleton?.idetity = "leads"
                        Navigation.findNavController(v).navigate(R.id.fragmentAgentChat)
                    }

                    App.singleton?.studentIdentifier =
                        staffData.data?.studentProfileInfo?.identifier

                    sharedPre!!.saveString(
                        AppConstants.USER_IDENTIFIER,
                        staffData.data?.studentProfileInfo?.identifier
                    )


                    if (currentFlavor == "admisiony" || currentFlavor == "eeriveurope" || currentFlavor == "firmli" || currentFlavor == "compassabroad" || currentFlavor == "studiepoint" || currentFlavor == "unitedglobalservices") {
                        val user =
                            staffData.data?.userInfo?.identityInfo?.filter { it.identifier == "RO1743976880086Y25NOHVF85" }

                        if (user?.size == 1) {
                            binding?.cdReferEarn?.visibility = View.VISIBLE
                            binding?.tvReferEarn?.visibility = View.GONE
                            binding?.tvBecomeaScout?.visibility = View.GONE
                            binding?.cdBecomeaScout?.visibility = View.GONE
                        } else {
                            binding?.cdReferEarn?.visibility = View.GONE
                            binding?.tvReferEarn?.visibility = View.GONE
                            binding?.tvBecomeaScout?.visibility = View.VISIBLE
                            binding?.cdBecomeaScout?.visibility = View.VISIBLE
                        }
                    } else {
                        // Hide both sections for other flavors
                        binding?.cdReferEarn?.visibility = View.GONE
                        binding?.tvReferEarn?.visibility = View.GONE
                        binding?.tvBecomeaScout?.visibility = View.GONE
                        binding?.cdBecomeaScout?.visibility = View.GONE
                    }


                    binding!!.cdBecomeaScout.setOnClickListener {
                        val identifier = staffData.data?.studentProfileInfo?.identifier
                        if (!identifier.isNullOrEmpty()) {
                            BecomeaScout(requireActivity(), identifier)
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Identifier not found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                    binding!!.cdReferEarn.setOnClickListener {
                        createReferandShare(requireActivity())
                    }


                } else {
                    val errorMessage = nonNullForgetModal.message ?: "Failed"

                    if (!errorMessage.contains("Access token expired", ignoreCase = true)) {
                        CommonUtils.toast(requireActivity(), errorMessage)
                    }
                }
            }
        }
    }

    private fun fetchUniversitiesAdapter() {

        fetchUniversitiesAdapter = HomeUniversitiesAdapter()
        binding?.rvUniversitiesStu?.adapter = fetchUniversitiesAdapter
    }

    private fun fetchLatestAdapter() {

        fetchLatestAdapter = HomeLatestUpdateAdapter()
        binding?.rvLatestStudyStu?.adapter = fetchLatestAdapter
    }

    private fun interestProgram() {

        interestsAdapter = HomeInterestsAdapter()
        binding?.rvInterestsStu?.adapter = interestsAdapter
    }

    private fun onBackPressed() {
        binding?.getRoot()?.setFocusableInTouchMode(true)
        binding?.getRoot()?.requestFocus()
        binding?.getRoot()?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode === KEYCODE_BACK) {
                requireActivity().finishAffinity()
                return@setOnKeyListener true
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()

        requireActivity().window.statusBarColor =
            requireActivity().getColor(R.color.theme_color_light)

        requireActivity().window.navigationBarColor =
            requireActivity().getColor(R.color.bottom_gradient_one)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController
            controller?.setSystemBarsAppearance(
                0, // clear light status bar flag
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }



        hitApiUserDetails()
        AppConstants.PROGRAM_STATUS = "0"
        FragProgramAllProg.selectedTab = "recommended"
        MainActivity.bottomNav!!.isVisible = true

    }

    override fun onCLick(record: com.student.Compass_Abroad.modal.AllProgramModel.Record) {
        ProgramDetails.details = record
        binding!!.root.findNavController().navigate(R.id.programDetails)

    }

    override fun likeClick(
        record: com.student.Compass_Abroad.modal.AllProgramModel.Record,
        pos: Int
    ) {
        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey

        val formData = JSONObject().apply {
            put("program_campus_identifier", record.identifier)
        }
        val dataToEncrypt = formData.toString()
        val appSecret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"
        val encryptedString = encryptData(dataToEncrypt, appSecret, ivHexString)

        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            Log.d("shortlisted", contentKey)
            addToShortlist(requireActivity(), contentKey)
        } else {
            Log.d("shortlisted", "Encryption failed.")
        }
    }

    override fun disLikeCLick(
        record: com.student.Compass_Abroad.modal.AllProgramModel.Record,
        pos: Int
    ) {
        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey

        val formData = JSONObject().apply {
            put("program_campus_identifier", record.identifier)
        }
        val dataToEncrypt = formData.toString()
        val appSecret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"
        val encryptedString = encryptData(dataToEncrypt, appSecret, ivHexString)

        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            Log.d("shortlisted", contentKey)
            removeFromShortlist(record)
            addToShortlist(requireActivity(), contentKey)
        } else {
            Log.d("shortlisted", "Encryption failed.")
        }
    }

    override fun openDialogCLick(
        record: com.student.Compass_Abroad.modal.AllProgramModel.Record,
        position: Int
    ) {
        val itemBinding = ProgramTagsDialogBinding.inflate(requireActivity().layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(itemBinding.root)
        dialog.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = 200.dpToPx()
        val margin = resources.getDimensionPixelSize(R.dimen.space5)
        layoutParams.horizontalMargin = 0f
        dialog.window!!.decorView.setPadding(margin, 0, margin, 0)
        dialog.window!!.attributes = layoutParams


        itemBinding.backBtn.setOnClickListener {
            dialog.dismiss()
        }
        val layoutManager =
            LinearLayoutManager(binding!!.root.context, LinearLayoutManager.HORIZONTAL, false)
        itemBinding.recyclerTags.layoutManager = layoutManager


        if (!record.program.tags.isNullOrEmpty()) {

            itemBinding.recyclerLay.visibility = View.VISIBLE
            val tagsAdapter = ProgramTagAdapter(record.program.tags)
            itemBinding.recyclerTags.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            itemBinding.recyclerTags.adapter = tagsAdapter
        } else {
            itemBinding.recyclerLay.visibility = View.GONE
        }

        dialog.show()
    }

    private fun Int.dpToPx(): Int {

        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    /* private fun fetchDataFromApi(binding: RecyclerView) {
         ViewModalClass().getPrograTagsList(
             requireActivity(),
             AppConstants.fiClientNumber,
             App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
             "Bearer ${CommonUtils.accessToken}",
             "program"
         ).observe(viewLifecycleOwner) { response ->
             response?.let {
                 if (it.statusCode == 200 && it.success) {
                     val programResponse = it.data?.recordsInfo ?: emptyList()

                     Log.d("fetchDataFromApi", "programResponse size: ${programResponse.size}")

                     applicationList.clear()
                     applicationList.addAll(programResponse)
                     programTagAdapter = ProgramTagAdapter(applicationList)
                     binding.adapter = programTagAdapter
                     programTagAdapter?.notifyDataSetChanged()

                     Log.d(
                         "AdapterCheck",
                         "Data size: ${applicationList.size}, First Item: ${
                             applicationList.getOrNull(0)
                         }"
                     )
                 } else {
                     Log.e("fetchDataFromApi", "Error fetching data: ${response.message}")
                 }
             }
         }
     }*/

    private fun addToShortlist(requireActivity: FragmentActivity, content: String) {
        ViewModalClass().getshorListModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken, content
        ).observe(viewLifecycleOwner) { shortListResponse: ShortListResponse? ->
            shortListResponse?.let { nonNullForgetModal ->
                if (shortListResponse.statusCode == 200) {

                    // Successfully added to shortlist
                } else {
                    CommonUtils.toast(requireActivity, shortListResponse.message ?: " Failed")
                }
            }
        }
    }

    private fun removeFromShortlist(record: com.student.Compass_Abroad.modal.AllProgramModel.Record) {
        arrayList.remove(record)
        adapterProgramsAllProg.notifyDataSetChanged()
    }

    private fun generateRandomHexString(length: Int): String {
        val hexChars = "0123456789abcdef"
        return (1..length).map { hexChars[Random.nextInt(hexChars.length)] }.joinToString("")
    }

    private fun getOffersandUpdatesIn(requireActivity: FragmentActivity) {
        ViewModalClass().getOffersUpdatesTabsLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { response: GetOffersandUpdates? ->
            response?.let { nonNullResponse ->
                if (nonNullResponse.statusCode == 200) {
                    nonNullResponse.data?.let { data ->
                        if (data.records.isNullOrEmpty()) {
                            binding!!.viewPager.visibility = View.GONE
                        } else {
                            binding!!.viewPager.visibility = View.GONE

                            val items1 = data.records

                            // Create or update your adapter with the list
                            val adapter = AdapterOffersandUpdate(
                                requireActivity,
                                items1,
                                binding!!.viewPager,
                                this
                            )
                            binding!!.viewPager.adapter = adapter
                            binding!!.indicatorOb.setViewPager(binding!!.viewPager)

                            // Start auto-scroll
                            adapter.startAutoScroll()

                            // Notify the adapter of the data change
                            adapter.notifyDataSetChanged()

                            Log.d("OffersUpdates", "Data loaded: ${items1.size} items")
                        }
                    } ?: run {
                        // Handle the case where data is null
                        val errorMessage = nonNullResponse.message ?: "Failed to retrieve data."

                        if (!errorMessage.contains("Access token expired", ignoreCase = true)) {
                            CommonUtils.toast(requireActivity, errorMessage)
                        }
                    }
                } else {
                    val errorMessage = nonNullResponse.message ?: "Failed to retrieve data."

                    if (!errorMessage.contains("Access token expired", ignoreCase = true)) {
                        CommonUtils.toast(requireActivity, errorMessage)
                    }
                }
            }
        }
    }

    private fun getScholarships(requireActivity: FragmentActivity) {
        ViewModalClass().getScholarshipsTabsLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { response: GetScholarships? ->
            response?.let { nonNullResponse ->
                if (nonNullResponse.statusCode == 200) {
                    nonNullResponse.data?.let { data ->
                        if (data.records.isNullOrEmpty()) {
                            binding!!.tvNoScolarshipDataFound.visibility = View.GONE
                            binding!!.viewPager2.visibility = View.GONE
                        } else {
                            binding!!.tvNoScolarshipDataFound.visibility = View.GONE
                            binding!!.viewPager2.visibility = View.VISIBLE

                            val items1 = data.records

                            val adapter = AdapterScholarships(items1, binding!!.viewPager2, this)
                            binding!!.viewPager2.adapter = adapter
                            binding!!.indicatorOb1.setViewPager(binding!!.viewPager2)

                            // Start auto-scroll
                            adapter.startAutoScroll()

                            adapter.notifyDataSetChanged()

                            Log.d("scholarships", "Data loaded: ${items1.size} items")
                        }
                    } ?: run {
                        val errorMessage = nonNullResponse.message ?: "Failed to retrieve data."

                        if (!errorMessage.contains("Access token expired", ignoreCase = true)) {
                            CommonUtils.toast(requireActivity, errorMessage)
                        }
                    }
                } else {
                    val errorMessage = nonNullResponse.message ?: "Failed to retrieve data."

                    if (!errorMessage.contains("Access token expired", ignoreCase = true)) {
                        CommonUtils.toast(requireActivity, errorMessage)
                    }
                }
            }
        }
    }

    override fun onClickData(
        position: Int,
        record: com.student.Compass_Abroad.modal.getOffersUpdatesModel.Record,
    ) {

        val itemBinding = SliderDataLayoutBinding.inflate(requireActivity().layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(itemBinding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        val margin = resources.getDimensionPixelSize(R.dimen.space5)
        layoutParams.horizontalMargin = 0f // Ensure margins are cleared in LayoutParams
        dialog.window!!.decorView.setPadding(margin, 0, margin, 0)
        dialog.window!!.attributes = layoutParams

        dialog.show()


        itemBinding.backBtn.setOnClickListener {
            dialog.dismiss()
        }

        itemBinding.tvDialogTitle.text = "Recent Updates"
        itemBinding.tvTitle.text = record.title
        itemBinding.content.text = record.content

        val inputFormatWithTime = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        ) // For published_at
        val inputFormatWithoutTime =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // For till_date
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) // Output format
        val tillDate = inputFormatWithoutTime.parse(record.till_date.toString())
        val formattedTillDate = outputFormat.format(tillDate!!)
        itemBinding.tvDateTime.text = "To: $formattedTillDate"
        val date = inputFormatWithTime.parse(record.published_at.toString())
        val formattedDate = outputFormat.format(date!!)
        itemBinding.from.text = "From: $formattedDate"

        dialog.show()

    }

    override fun listener(record: com.student.Compass_Abroad.modal.getScholarships.Record) {

        val itemBinding = SliderDataLayoutBinding.inflate(requireActivity().layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(itemBinding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        val margin = resources.getDimensionPixelSize(R.dimen.space5)
        layoutParams.horizontalMargin = 0f
        dialog.window!!.decorView.setPadding(margin, 0, margin, 0)
        dialog.window!!.attributes = layoutParams

        dialog.show()


        itemBinding.backBtn.setOnClickListener {
            dialog.dismiss()
        }

        itemBinding.tvDialogTitle.text = "Scholarships"
        itemBinding.tvTitle.text = record.title
        itemBinding.content.text = record.content
        val inputFormatWithTime = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        )
        val inputFormatWithoutTime =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // For till_date
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) // Output format
        val tillDate = inputFormatWithoutTime.parse(record.till_date.toString())
        val formattedTillDate = outputFormat.format(tillDate!!)
        itemBinding.tvDateTime.text = "To: $formattedTillDate"
        val date = inputFormatWithTime.parse(record.published_at.toString())
        val formattedDate = outputFormat.format(date!!)
        itemBinding.from.text = "From: $formattedDate"

        dialog.show()
    }


    private fun getModeOfPaymentDropdown(
        requireActivity: FragmentActivity,
        payment_mode: TextView,
    ) {

        ViewModalClass().getModeOFPaymentDropDownVoucherLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken
        ).observe(requireActivity) { getVoucherPaymentMode: getVoucherPaymentMode? ->
            getVoucherPaymentMode?.let { nonNullcategoriesResponse ->
                if (getVoucherPaymentMode.statusCode == 200) {
                    modeOfPaymentList.clear()
                    if (getVoucherPaymentMode.data != null) {
                        for (i in 0 until getVoucherPaymentMode?.data?.recordsInfo!!.size) {
                            modeOfPaymentList?.add(getVoucherPaymentMode.data!!.recordsInfo[i])
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                            PaymentModeSpinner(payment_mode)

                        }
                    } else {

                        CommonUtils.toast(requireActivity, "Failed to retrieve testScore. Please try again.")

                    }


                } else {

                    CommonUtils.toast(requireActivity, getVoucherPaymentMode.message ?: " Failed")
                }
            }
        }

    }

    private fun generatingPaymentLinkVoucher(
        requireActivity: FragmentActivity,
        currency: String,
        price: String,
        quantity: String,
        moduleIdentifier: String,
        paymentTypeIdentifier: String,
        paymentGatewayIdentifier: String,
        dialog: Dialog
    ) {
        val accessToken = CommonUtils.accessToken
        val deviceIdentifier = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""

        if (accessToken.isNullOrEmpty()) {
            CommonUtils.toast(requireActivity, "Access token is missing")
            return
        }

        ViewModalClass().genratingPaymentLinkVoucherLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            deviceIdentifier,
            "Bearer $accessToken",
            "voucher",  // Ensure module = "voucher"
            moduleIdentifier,
            price,
            currency,
            quantity,
            paymentTypeIdentifier,
            paymentGatewayIdentifier
        ).observe(requireActivity) { response ->
            response?.let {
                when (it.statusCode) {
                    201 -> {
                        //CommonUtils.toast(requireActivity, it.message ?: "Link created successfully")
                        calltheApiPay(
                            requireActivity,
                            it.data!!.feePaymentInfo.identifier,
                            it.data!!.feePaymentInfo.payment_gateway_info.name
                        )


                    }

                    409 -> {
                        CommonUtils.toast(requireActivity, it.message ?: "Already exists")

                        dialog.dismiss()
                    }

                    else -> {
                        dialog.dismiss()
                        CommonUtils.toast(requireActivity, it.message ?: "Link creation failed")
                    }
                }
            }
        }
    }

    private fun calltheApiPay(
        context: FragmentActivity,
        identifier1: String,
        value: String

    ) {
        context.let { fragmentActivity ->
            CommonUtils.accessToken?.let { accessToken ->
                ViewModalClass().getApplicationPayLiveData(
                    fragmentActivity,
                    AppConstants.fiClientNumber,
                    sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                    "Bearer $accessToken",
                    identifier1
                ).observe(fragmentActivity) { response ->
                    if (response != null && response.success && response.data != null) {
                        val data = response.data

                        if (value.equals("Razorpay")) {
                            Checkout.preload(context)
                            val co = Checkout()

                            co.setKeyID(data!!.gateway_info.content.public_key)
                            initPayment(context, data)

                        }
                        if (value.equals("Stripe")) {
                            startPayment(
                                fragmentActivity,
                                data!!.gateway_info.content.public_key,
                                data!!.gateway_info.order_info.id
                            )
                        }

                    } else {
                        val errorMessage = response?.message ?: "Failed to fetch data"
                        CommonUtils.toast(fragmentActivity, errorMessage)
                    }
                }
            } ?: run {
                CommonUtils.toast(fragmentActivity, "Access token is missing")
            }
        }
    }

    private fun startPayment(fragmentActivity: FragmentActivity, pK: String, sK: String) {
        PaymentConfiguration.init(fragmentActivity, pK)
        secretKey = sK

        if (secretKey != null) {
            paymentSheet.presentWithPaymentIntent(secretKey)
            CommonUtils.dismissProgress()
        } else {
            CommonUtils.dismissProgress()
            CommonUtils.toast(fragmentActivity, "Payment key not available")
        }
    }


    private fun initPayment(
        context: FragmentActivity?,
        data: com.student.Compass_Abroad.modal.getPaymentApplicationPay.Data?
    ) {
        val activity: FragmentActivity = context!!
        val co = Checkout()
        try {
            val options = JSONObject()
            options.put("name", context.getString(R.string.app_name))

            options.put("image", context.getString(R.string.payment_icon))
            options.put("theme.color", data!!.gateway_info.theme)
            options.put("currency", data.gateway_info.payment_info.currency)
            options.put("order_id", data.gateway_info.order_info.id)
            options.put("amount", data.gateway_info.payment_info.amount)
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)
            val prefill = JSONObject()
            prefill.put("email", data.user_info.email)
            prefill.put("contact", "${data.user_info.country_code}${data.user_info.mobile}")

            options.put("prefill", prefill)
            co!!.open(activity, options)
            dialog.dismiss()

        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

    private fun onPaymentResult(
        paymentSheetResult: PaymentSheetResult,
        dialog: Dialog
    ) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Completed -> {
                dialog.dismiss()
                CommonUtils.toast(context, "Payment Success")
            }

            is PaymentSheetResult.Canceled -> {
                dialog.dismiss()
                CommonUtils.toast(context, "Payment Cancelled")
            }

            is PaymentSheetResult.Failed -> {
                val errorMessage =
                    (paymentSheetResult as? PaymentSheetResult.Failed)?.error?.message
                        ?: "Unknown error occurred"
                CommonUtils.toast(context, "Payment Failed: $errorMessage")

            }
        }
    }

    private fun onGetRecommendedAllPrograms(
        dataPerPage: Int,
        presentPage: Int,
    ) {
        ViewModalClass().getAllRecommendedProgramsModalLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken,
            presentPage,
            dataPerPage
        ).observe(viewLifecycleOwner) { allProgramModal: AllProgramModel? ->
            allProgramModal?.let { nonNullForgetModal ->
                if (view != null) {
                    if (allProgramModal.statusCode == 200) {
                        nextPage = allProgramModal.data?.metaInfo?.nextPage ?: 0
                        // Only clear list when loading the first page
                        if (presentPage == 1) {
                            arrayList.clear()
                        }
                        arrayList.addAll(allProgramModal.data?.records ?: emptyList())
                    } else {
                        CommonUtils.toast(requireActivity(), nonNullForgetModal.message ?: "Failed")
                    }
                    setRecyclerViewVisibilityRecommended()
                }
            }
        }
    }

    private fun setRecyclerViewVisibilityRecommended() {
        binding!!.llFpApNoData.isVisible = arrayList.isEmpty()
        adapterProgramsAllProg.notifyDataSetChanged()
        isLoading = false


    }

    private fun setupRecyclerViewRecommended() {
        layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding!!.rvFpApp.layoutManager = layoutManager


        adapterProgramsAllProg = AdapterProgramsAllProg(requireActivity(), arrayList, this)

        binding!!.rvFpApp.adapter = adapterProgramsAllProg

        binding!!.rvFpApp.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentVisibleItems = layoutManager.childCount
                totalItemsInAdapter = layoutManager.itemCount
                scrolledOutItems = layoutManager.findFirstVisibleItemPosition()

                if (isScrolling && (scrolledOutItems + currentVisibleItems == totalItemsInAdapter)) {
                    isScrolling = false
                    onGetRecommendedAllPrograms(dataPerPage, presentPage)
                }
            }
        })
    }

    private fun sliderImage(arrayListBanner: ArrayList<com.student.Compass_Abroad.modal.getBannerModel.Record>) {
        sliderItems.clear()
        binding!!.indicatorLayout.removeAllViews()

        // Populate sliderItems with data from API
        for (item in arrayListBanner) {
            sliderItems.add(item.fileInfo.view_page) // Assuming `image` is the URL string
        }

        // Set adapter
        binding!!.viewPagerImageSlider.adapter =
            SliderAdapter(sliderItems, binding!!.viewPagerImageSlider)

        // Set ViewPager2 properties
        binding!!.viewPagerImageSlider.clipToPadding = false
        binding!!.viewPagerImageSlider.clipChildren = false
        binding!!.viewPagerImageSlider.offscreenPageLimit = 3
        binding!!.viewPagerImageSlider.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        binding!!.viewPagerImageSlider.setPageTransformer(compositePageTransformer)

        binding!!.viewPagerImageSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                if (position == sliderItems.size - 1) {
                    binding!!.viewPagerImageSlider.postDelayed({
                        binding!!.viewPagerImageSlider.setCurrentItem(0, true)
                    }, 1000)
                } else {
                    sliderHandler.postDelayed(sliderRunnable, 3000)
                }
            }
        })

        // Dot indicator
        val dotCount = sliderItems.size
        val dots = arrayOfNulls<ImageView>(dotCount)
        for (i in 0 until dotCount) {
            dots[i] = ImageView(requireActivity())
            dots[i]?.setImageResource(R.drawable.dot_selector)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding!!.indicatorLayout.addView(dots[i], params)
        }

        binding!!.viewPagerImageSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in 0 until dotCount) {
                    dots[i]?.isSelected = (i == position)
                }
            }
        })
    }


    private fun getBanner() {
        ViewModalClass().getBannerModalLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken
        ).observe(viewLifecycleOwner) { getBannerModel: getBannerModel? ->
            getBannerModel?.let { nonNullForgetModal ->
                if (view != null) {
                    if (getBannerModel.statusCode == 200) {
                        arrayListBanner.clear()
                        arrayListBanner.addAll(getBannerModel.data?.records ?: emptyList())
                    } else {
                        CommonUtils.toast(requireActivity(), nonNullForgetModal.message ?: "Failed")
                    }
                    sliderImage(arrayListBanner)
                }
            }
        }
    }

    fun refreshTokenApi(list: String, context: Context?): RefreshTokenResonse? {
        return try {
            val response = apiInterface.getRefreshToken(
                fiClientNumber = AppConstants.fiClientNumber,
                device_number = sharedPre?.getString(AppConstants.Device_IDENTIFIER, ""),
                authorization = "Bearer ${
                    sharedPre?.getString(
                        AppConstants.REFRESH_TOKEN,
                        ""
                    )
                }",
                identity = list
            )!!.execute()
            if (response.isSuccessful) {

                response.body()

            } else {
                Log.e(
                    "ProfileActivity",
                    "Error refreshing token: ${response.code()} ${response.errorBody()?.string()}"
                )
                null
            }
        } catch (e: IOException) {
            Log.e("ProfileActivity", "Token refresh failed due to network error: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e("ProfileActivity", "Unexpected error during token refresh: ${e.message}", e)
            null
        }
    }


    private fun handleRefreshTokenError(code: Int?, errorBody: String?) {
        if (code == 422) {
            Log.e("AuthInterceptor", "Unprocessable Entity: $errorBody")
        } else {
            Log.e("AuthInterceptor", "Token refresh failed with unexpected error: $code")
        }
    }

    fun loadJSONFromAsset(context: Context, filename: String): String? {
        return try {
            val inputStream = context.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    private fun saveSelectedToSharedPreferences(
        keyPrefix: String,
        ids: String,
        labels: String
    ) {
        val sharedPrefs = SharedPrefs(requireActivity())
        sharedPrefs.putString11("${keyPrefix}Id", ids)
        sharedPrefs.putString11("${keyPrefix}Label", labels)
    }


    private fun showInfoBottomSheet() {
        val inflater = LayoutInflater.from(requireActivity())
        val bottomSheetBinding = EducationloanBinding.inflate(inflater, null, false)


        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheet2)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()


    }

    private fun GetWebinars(
        activity: FragmentActivity,
        presentPage: Int,
        dataPerPage: Int,
    ) {
        ViewModalClass().getWebinarsModalLiveData(
            activity,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken,
            dataPerPage,
            presentPage,
            null
        ).observe(viewLifecycleOwner) { response: getWebinarsResponse? ->

            if (response == null) {
                // null response → hide everything
                binding?.apply {
                    rvWebinars.visibility = View.GONE
                    relativeWebinars?.visibility = View.GONE
                    viewWeb?.visibility = View.GONE
                    noWbinarFound.visibility = View.VISIBLE
                }
                return@observe
            }

            if (response.statusCode == 200) {
                val records = response.data?.records
                if (records.isNullOrEmpty()) {
                    binding?.apply {
                        rvWebinars.visibility = View.GONE
                        relativeWebinars?.visibility = View.GONE
                        viewWeb?.visibility = View.GONE
                    }
                } else {
                    // webinars found → show section & recycler
                    binding?.apply {
                        rvWebinars.visibility = View.VISIBLE
                        relativeWebinars?.visibility = View.VISIBLE
                        viewWeb?.visibility = View.VISIBLE
                    }

                    webinarsList1.clear()
                    webinarsList1.addAll(records)
                    setWebinarsRecyclerview(webinarsList1)
                }
            } else {
                // API error → hide everything
                binding?.apply {
                    rvWebinars.visibility = View.GONE
                    relativeWebinars?.visibility = View.GONE
                    viewWeb?.visibility = View.GONE
                }

                val errorMessage = response.message ?: "Failed"
                if (!errorMessage.contains("Access token expired", ignoreCase = true)) {
                    CommonUtils.toast(activity, errorMessage)
                }
            }
        }
    }


    private fun setWebinarsRecyclerview(arrayList: ArrayList<com.student.Compass_Abroad.modal.getWebinars.Record>) {
        adaptorwebinars =
            AdaptorWebinarRecyclerview(this.requireActivity(), arrayList, this)
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding!!.rvWebinars.layoutManager = layoutManager
        binding!!.rvWebinars.adapter = adaptorwebinars

        binding!!.rvWebinars.addOnScrollListener(
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
                        if (presentPage2 < nextPage) {
                            presentPage2 += 1

                            Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                                override fun run() {
                                    Handler().removeCallbacks(this, null)
                                    //GetWebinars(requireActivity(), dataPerPage2, presentPage2)
                                }
                            }, 2000)
                        }
                    }
                }
            })

    }

    override fun onclick(currentItem: com.student.Compass_Abroad.modal.getWebinars.Record) {
        postAteende(requireActivity(), currentItem)
    }

    private fun postAteende(
        activity1: Activity,
        currentItem: com.student.Compass_Abroad.modal.getWebinars.Record
    ) {
        val deviceIdentifier = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty()
        val token = "Bearer ${CommonUtils.accessToken}"
        val firstName =
            sharedPre?.getString(AppConstants.FIRST_NAME, "")?.takeIf { it.isNotBlank() }
        val lastName = sharedPre?.getString(AppConstants.LAST_NAME, "")?.takeIf { it.isNotBlank() }
        val email = sharedPre?.getString(AppConstants.USER_EMAIL, "")?.takeIf { it.isNotBlank() }
        val phone = sharedPre?.getString(AppConstants.PHONE, "")?.takeIf { it.isNotBlank() }


// Validation

        ViewModalClass().postAttendeLiveData(
            activity1,
            AppConstants.fiClientNumber,
            deviceIdentifier,
            token,
            currentItem.identifier,
            firstName,
            lastName,
            email,
            phone,
            "internal"
        ).observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Toast.makeText(activity1, "Error: Response is null", Toast.LENGTH_SHORT).show()
                Log.e("SaveReviewResponse", "Response is null")
                return@observe
            }

            if (response.statusCode == 200) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(currentItem.event_detail)
                requireActivity().startActivity(intent)
            } else {
                Toast.makeText(
                    activity1,
                    response.message ?: "Failed to submit review",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun BecomeaScout(activity1: Activity, identifier: String) {
        val deviceIdentifier =
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty()
        val token = "Bearer ${CommonUtils.accessToken}"
        ViewModalClass().postBecomeaScoutData(
            activity1,
            AppConstants.fiClientNumber,
            deviceIdentifier,
            token,
            identifier
        ).observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Toast.makeText(activity1, "Error: Response is null", Toast.LENGTH_SHORT).show()
                Log.e("SaveReviewResponse", "Response is null")
                return@observe
            }

            if (response.statusCode == 200) {
                hitApiUserDetails()

            } else {
                Toast.makeText(
                    activity1,
                    response.message ?: "Failed to submit review",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createReferandShare(activity1: Activity) {
        val deviceIdentifier = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "").orEmpty()
        val token = "Bearer ${CommonUtils.accessToken}"
        ViewModalClass().postReferLinkLiveData(
            activity1,
            AppConstants.fiClientNumber,
            deviceIdentifier,
            token,
            "user"
        ).observe(viewLifecycleOwner) { response ->
            if (response == null) {
                Toast.makeText(activity1, "Error: Response is null", Toast.LENGTH_SHORT).show()
                Log.e("SaveReviewResponse", "Response is null")
                return@observe
            }
            Log.d(
                "SaveReviewResponse",
                "Status Code: ${response.statusCode}, Message: ${response.message}"
            )

            // Check if status code is 201 (Created) meaning the referral link was generated successfully
            if (response.statusCode == 201) {
                val shortUrl = response.data?.shortUrl

                if (!shortUrl.isNullOrEmpty()) {
                    // Show success message
                    // Toast.makeText(activity1, response.message ?: "Referral link generated successfully!", Toast.LENGTH_SHORT).show()

                    // Log the short URL for debugging
                    Log.e("ReferralLink", shortUrl)

                    // Share the referral link
                    shareReferralLink(activity1, shortUrl)
                } else {
                    // Handle case when the short URL is null or empty
                    Toast.makeText(
                        activity1,
                        "Failed to generate referral link",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    activity1,
                    response.message ?: "Failed to submit review",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun shareReferralLink(activity1: Activity, shortUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "Check out this awesome app! Use my referral link: $shortUrl"
            )
        }

        activity1.startActivity(Intent.createChooser(shareIntent, "Share via"))

    }

    private fun setupRecyclerViewStudentTestimonials() {

        val staticList = listOf(
            StaticTestimonial(
                name = "Aarav Sharma",
                description = "Compass Abroad helped me get admission to my dream university in Canada!",
                date = "2025-11-01",
                imageResId = R.drawable.test_banner
            ),
            StaticTestimonial(
                name = "Priya Mehta",
                description = "Amazing experience! The counselors were super supportive.",
                date = "2025-10-25",
                imageResId = R.drawable.test_banner
            ),
            StaticTestimonial(
                name = "Rohan Verma",
                description = "Very professional service. Highly recommend Compass Abroad!",
                date = "2025-10-15",
                imageResId = R.drawable.test_banner
            )
        )

        binding?.rvTestimonials?.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = StudentStaticTestimonialsAdapter(staticList)
        }


//        val deviceId = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""
//        val token = "Bearer ${CommonUtils.accessToken}"
//        LoginViewModal().getTestimonials(
//            requireActivity(),
//            AppConstants.fiClientNumber,
//            deviceId,
//            token,
//            true,
//            "webinar"
//        ).observe(viewLifecycleOwner) { response ->
//            response?.let { resp ->
//                if (resp.statusCode == 200) {
//                    val records = resp.data?.records?.rows
//                    if (!records.isNullOrEmpty()) {
//                        // ✅ Show section
//                        binding?.relativeTestimonials?.visibility = View.VISIBLE
//                        binding?.viewTest?.visibility = View.VISIBLE
//
//                        arrayListInStudentTestimonials.clear()
//                        arrayListInStudentTestimonials.addAll(records)
//
//                        binding?.rvTestimonials?.apply {
//                            layoutManager = LinearLayoutManager(
//                                requireContext(),
//                                LinearLayoutManager.HORIZONTAL,
//                                false
//                            )
//                            adapter = StudentTestimonialsAdapter(arrayListInStudentTestimonials) { selectedItem ->
//                                // Handle click here
//                            }
//                        }
//                    } else {
//                        // ✅ Hide section if list is empty
//                        binding?.relativeTestimonials?.visibility = View.GONE
//                        binding?.viewTest?.visibility = View.GONE
//                    }
//                } else {
//                    // ✅ Hide section on API failure
//                    binding?.relativeTestimonials?.visibility = View.GONE
//                    binding?.viewTest?.visibility = View.GONE
//
//
//                    val errorMsg = resp.message ?: "Failed"
//                    if (!errorMsg.contains("Access token expired", ignoreCase = true)) {
//                        CommonUtils.toast(requireActivity(), errorMsg)
//                    }
//                }
//            } ?: run {
//                // ✅ Hide section if response is null
//                binding?.relativeTestimonials?.visibility = View.GONE
//                binding?.viewTest?.visibility = View.GONE
//
//            }
//        }
    }

    private fun setupRecyclerLatestUpdates() {
        val deviceId = sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: ""
        val token = "Bearer ${CommonUtils.accessToken}"

        val staticUpdates = listOf(
            StaticLatestUpdate(
                title = "Study Visa Updates 2025",
                description = "Canada announces new study visa rules effective January 2025.",
                date = "2025-11-08",
                imageResId = R.drawable.latest
            ),
            StaticLatestUpdate(
                title = "UK Scholarship Alert",
                description = "New full scholarships available for Indian students in 2025 intake.",
                date = "2025-10-30",
                imageResId = R.drawable.latest
            ),
            StaticLatestUpdate(
                title = "Australia Intake 2026",
                description = "Applications for the February 2026 intake now open for top universities.",
                date = "2025-10-20",
                imageResId = R.drawable.latest
            )
        )

        // ✅ Show static list in RecyclerView
        binding?.rvLatestUpdates?.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = StaticLatestUpdateAdapter(staticUpdates)
        }



//        LoginViewModal().getTestimonials(
//            requireActivity(),
//            AppConstants.fiClientNumber,
//            deviceId,
//            token,
//            true,
//            "recent_update"
//        ).observe(viewLifecycleOwner) { response ->
//            response?.let { resp ->
//                if (resp.statusCode == 200) {
//                    val records = resp.data?.records?.rows
//
//                    if (!records.isNullOrEmpty()) {
//                        // ✅ Show section when data available
//                        binding?.relativeLatestUpdates?.visibility = View.VISIBLE
//
//                        arrayListInLatestUpdate.clear()
//                        arrayListInLatestUpdate.addAll(records)
//
//                        binding?.rvLatestUpdates?.apply {
//                            layoutManager = LinearLayoutManager(
//                                requireContext(),
//                                LinearLayoutManager.HORIZONTAL,
//                                false
//                            )
//                            adapter = LatestUpdateAdapter(arrayListInLatestUpdate) { selectedItem ->
//                                // Handle click here
//                            }
//                        }
//                    } else {
//                        // ✅ Hide section when list is empty
//                        binding?.relativeLatestUpdates?.visibility = View.GONE
//                    }
//                } else {
//                    // ✅ Hide section on API failure
//                    binding?.relativeLatestUpdates?.visibility = View.GONE
//
//                    val errorMsg = resp.message ?: "Failed"
//                    if (!errorMsg.contains("Access token expired", ignoreCase = true)) {
//                        CommonUtils.toast(requireActivity(), errorMsg)
//                    }
//                }
//            } ?: run {
//                // ✅ Hide section if response is null
//                binding?.relativeLatestUpdates?.visibility = View.GONE
//            }
//        }
    }

}


