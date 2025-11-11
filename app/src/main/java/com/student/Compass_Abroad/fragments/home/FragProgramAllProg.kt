package com.student.Compass_Abroad.fragments.home

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.student.Compass_Abroad.BuildConfig

import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Scout.activities.ScoutMainActivity
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.SharedPrefs
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.activities.MainActivity.Companion.drawer
import com.student.Compass_Abroad.adaptor.AdapterProgramsAllProg
import com.student.Compass_Abroad.adaptor.CategoryProgramAdapter
import com.student.Compass_Abroad.adaptor.ProgramTagAdapter
import com.student.Compass_Abroad.databinding.FragmentFragProgramAllProgBinding
import com.student.Compass_Abroad.databinding.ProgramTagsDialogBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.AllProgramModel.AllProgramModel
import com.student.Compass_Abroad.modal.AllProgramModel.Record
import com.student.Compass_Abroad.modal.ProgramTags.RecordsInfo
import com.student.Compass_Abroad.modal.getCategoryProgramModel.getCategoryProgramModel
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass
import org.json.JSONObject
import kotlin.random.Random
import androidx.core.graphics.drawable.toDrawable
import androidx.navigation.findNavController

class FragProgramAllProg : BaseFragment(), AdapterProgramsAllProg.select {
    private lateinit var binding: FragmentFragProgramAllProgBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapterProgramsAllProg: AdapterProgramsAllProg
    private lateinit var categoryLayoutManager: LinearLayoutManager
    private lateinit var categoryProgramAdapter: CategoryProgramAdapter
    private var arrayList1 = ArrayList<Record>()
    private var isScrolling = false
    private var isLoading = false
    private var currentVisibleItems = 0
    private var totalItemsInAdapter = 0
    private var scrolledOutItems = 0
    private var dataPerPage = 25
    private var presentPage = 1
    private var nextPage = 0
    private var contentKey = ""
    private var countryNamee: List<String> = ArrayList()
    private var stateNamee: List<String> = ArrayList()
    private var cityNamee: List<String> = ArrayList()
    private var instituteName: List<String> = ArrayList()
    private var tvFStudyLevel: List<String> = ArrayList()
    private var tvLookingFor: List<String> = ArrayList()
    private var tvIntakeSelector: List<String> = ArrayList()
    private var tvFEnglishLevel: String? = null
    private var tvFAge: String? = null
    private var tvPGMP: String? = null
    private var tvAttendance: String? = null
    private var tvProgramType: String? = null
    private var tvAccomodation: String? = null
    private var tvminTutionFee: String? = null
    private var tvMaxTutionFee: String? = null
    private var status: Int? = null
    private var tvMinApplicationFee: String? = null
    private var tvMaxApplicationFee: String? = null
    private var search: String? = null
    private var category = "higher_education"
    private val arrayListCategory: MutableList<com.student.Compass_Abroad.modal.getCategoryProgramModel.Data> =
        mutableListOf()
    private val applicationList: MutableList<RecordsInfo> = mutableListOf()
    private var recyclerViewPosition: Int = 0
    private var isBottomNavVisible = true
    private var isRecommended = "true"

    companion object {

        var selectedTab: String? = "recommended"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFragProgramAllProgBinding.inflate(inflater, container, false)

        setupRecyclerView()
        onClicks()
        searchData()
        App.singleton?.assignStaffFav = "0"

        sharedPre!!.saveString(AppConstants.CATEGORY, "higher_education")

        binding.civProfileImageFd2.setOnClickListener {
            if (sharedPre!!.getString(AppConstants.SCOUtLOGIN, "") == "true") {
                ScoutMainActivity.drawer!!.open()
            } else {
                drawer!!.open()
            }
        }

        if (sharedPre!!.getString(AppConstants.SCOUtLOGIN, "") == "true") {
            binding.fabFpHeart.visibility = View.GONE
            binding.fabFpNotificationStu.visibility = View.GONE
            binding.tlFc.visibility = View.GONE
        } else {
            binding.fabFpHeart.visibility = View.VISIBLE
            binding.fabFpNotificationStu.visibility = View.VISIBLE
            binding.tlFc.visibility = View.VISIBLE
        }

        getProgramCategoryStat(requireActivity())

        tvPGMP = sharedPre!!.getString11(AppConstants.PGWP_KEY)
        tvAttendance = sharedPre!!.getString11(AppConstants.ATTENDANCE_KEY)
        tvProgramType = sharedPre!!.getString11(AppConstants.PROGRAM_TYPE_KEY)
        tvAccomodation = sharedPre!!.getString11(AppConstants.Accomodation)


        val englishLevel =
            sharedPre!!.getLabelValue(AppConstants.EnglishLevelList) // This returns a Pair<String, String> or null if no data found

        if (englishLevel != null) {
            tvFEnglishLevel = englishLevel.second  // Second element is the value

        } else {
            tvFEnglishLevel = null
        }

        val age =
            sharedPre!!.getLabelValue(AppConstants.AgeList) // This returns a Pair<String, String> or null if no data found

        if (age != null) {
            tvFAge = age.second  // Second element is the value

        } else {
            tvFAge = null
        }


        tvminTutionFee = sharedPre!!.getString11(AppConstants.MIN_TUTION_KEY)
        tvMaxTutionFee = sharedPre!!.getString11(AppConstants.MAX_TUTION_KEY)
        tvMinApplicationFee = sharedPre!!.getString11(AppConstants.MIN_APPLICATION_KEY)
        tvMaxApplicationFee = sharedPre!!.getString11(AppConstants.MAX_APPLICATION_KEY)



        tvPGMP = if (tvPGMP.isNullOrEmpty()) null else tvPGMP
        tvAttendance = if (tvAttendance.isNullOrEmpty()) null else tvAttendance
        tvProgramType = if (tvProgramType.isNullOrEmpty()) null else tvProgramType
        tvAccomodation = if (tvAccomodation.isNullOrEmpty()) null else tvAccomodation
        tvFEnglishLevel = if (tvFEnglishLevel.isNullOrEmpty()) null else tvFEnglishLevel
        tvFAge = if (tvFAge.isNullOrEmpty()) null else tvFAge

        tvminTutionFee = if (tvminTutionFee.isNullOrEmpty()) null else tvminTutionFee
        tvMaxTutionFee = if (tvMaxTutionFee.isNullOrEmpty()) null else tvMaxTutionFee
        tvMinApplicationFee = if (tvMinApplicationFee.isNullOrEmpty()) null else tvMinApplicationFee
        tvMaxApplicationFee = if (tvMaxApplicationFee.isNullOrEmpty()) null else tvMaxApplicationFee


        if (category == "higher_education") {
            ProgramFilterFragment.data = category
            category = "higher_education"
            status = 0
            ProgramFilterFragment.clearData = 1
            sharedPre!!.saveString("status", status.toString())

        } else if (category == "summer_school") {
            ProgramFilterFragment.data = category
            ProgramFilterFragment.clearData = 1
            category = "summer_school"
            status = 2
            sharedPre!!.saveString("status", status.toString())

        } else if (category == "career_program") {
            ProgramFilterFragment.data = category
            ProgramFilterFragment.clearData = 1
            category = "career_program"
            status = 3
            sharedPre!!.saveString("status", status.toString())

        } else {
            status = 1
            ProgramFilterFragment.data = category
            category = "language_program"
            ProgramFilterFragment.clearData = 1
            sharedPre!!.saveString("status", status.toString())


        }

        if (selectedTab == "recommended") {
            arrayList1.clear()
            isRecommended = "true"
            if (!isLoading) {
                isLoading = true
                binding.pbFpAp.visibility = View.VISIBLE

                onGetAllPrograms(
                    presentPage,
                    dataPerPage,
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    null,
                    emptyList(),
                    emptyList(),
                    null,
                    null,
                    emptyList(),
                    null,
                    null,
                    null,
                    null,
                    search,
                    "higher_education",
                    null,
                    null,
                    null,
                    isRecommended
                )
            }

            binding.rvCategory.visibility = View.GONE
            binding.rll.visibility = View.GONE
            binding!!.view1.visibility = View.VISIBLE
            binding!!.view2.visibility = View.GONE
            selectedTab = "recommended"
            updateTabUI(isRecommended = true)
        } else {
            arrayList1.clear()
            isRecommended = "false"

            selectedTab = "all"
            updateTabUI(isRecommended = false)
            binding!!.swipeRefreshLayout.setOnRefreshListener {
                applicationList.clear()
                presentPage = 1
                nextPage = 0
                onGetAllPrograms(
                    presentPage,
                    dataPerPage,
                    countryNamee,
                    stateNamee,
                    cityNamee,
                    instituteName,
                    tvPGMP,
                    tvFStudyLevel,
                    tvLookingFor,
                    tvAttendance,
                    tvProgramType,
                    tvIntakeSelector,
                    tvminTutionFee,
                    tvMaxTutionFee,
                    tvMinApplicationFee,
                    tvMaxApplicationFee,
                    search,
                    category,
                    tvAccomodation,
                    tvFEnglishLevel,
                    tvFAge,
                    isRecommended
                )
            }

            countryNamee = getSavedSelectedItems(AppConstants.CountryList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            stateNamee = getSavedSelectedItems(AppConstants.StateList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            cityNamee = getSavedSelectedItems(AppConstants.CityList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            instituteName = getSavedSelectedItems(AppConstants.institutionList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            tvLookingFor = getSavedSelectedItems(AppConstants.disciplineList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            tvFStudyLevel = getSavedSelectedItems(AppConstants.studyLevelList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            tvIntakeSelector = getSavedSelectedItems(AppConstants.IntakeList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }


            if (countryNamee.isNotEmpty() ||
                stateNamee.isNotEmpty() ||
                cityNamee.isNotEmpty() ||
                instituteName.isNotEmpty() ||
                tvFStudyLevel.isNotEmpty() ||
                tvLookingFor.isNotEmpty() ||
                tvIntakeSelector.isNotEmpty() ||
                tvPGMP != null ||
                tvAttendance != null ||
                tvProgramType != null ||
                tvminTutionFee != null ||
                tvMaxTutionFee != null ||
                tvMinApplicationFee != null ||
                tvMaxApplicationFee != null ||
                tvAccomodation != null ||
                tvFEnglishLevel != null ||
                tvFAge != null
            ) {

                binding.filterDot.visibility = View.VISIBLE
                ProgramFilterFragment.clearData = 0
                loadFilteredData()

            } else {
                binding.filterDot.visibility = View.GONE
                ProgramFilterFragment.clearData = 1
                loadInitialData(search, category, isRecommended)
            }
            binding.rvCategory.visibility = View.GONE
            binding.rll.visibility = View.VISIBLE
            binding!!.view1.visibility = View.GONE
            binding!!.view2.visibility = View.VISIBLE

        }




        binding!!.tabRecommended.setOnClickListener {

            arrayList1.clear()
            isRecommended = "true"
            if (!isLoading) {
                isLoading = true
                binding.pbFpAp.visibility = View.VISIBLE

                onGetAllPrograms(
                    presentPage,
                    dataPerPage,
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    emptyList(),
                    null,
                    emptyList(),
                    emptyList(),
                    null,
                    null,
                    emptyList(),
                    null,
                    null,
                    null,
                    null,
                    search,
                    "higher_education",
                    null,
                    null,
                    null,
                    isRecommended
                )
            }

            binding.rvCategory.visibility = View.GONE
            binding.rll.visibility = View.GONE
            binding!!.view1.visibility = View.VISIBLE
            binding!!.view2.visibility = View.GONE
            selectedTab = "recommended"
            updateTabUI(isRecommended = true)


        }

        binding!!.tabAllProgram.setOnClickListener {
            arrayList1.clear()
            isRecommended = "false"

            selectedTab = "all"
            updateTabUI(isRecommended = false)
            binding!!.swipeRefreshLayout.setOnRefreshListener {
                applicationList.clear()
                presentPage = 1
                nextPage = 0
                onGetAllPrograms(
                    presentPage,
                    dataPerPage,
                    countryNamee,
                    stateNamee,
                    cityNamee,
                    instituteName,
                    tvPGMP,
                    tvFStudyLevel,
                    tvLookingFor,
                    tvAttendance,
                    tvProgramType,
                    tvIntakeSelector,
                    tvminTutionFee,
                    tvMaxTutionFee,
                    tvMinApplicationFee,
                    tvMaxApplicationFee,
                    search,
                    category,
                    tvAccomodation,
                    tvFEnglishLevel,
                    tvFAge,
                    isRecommended
                )
            }

            countryNamee = getSavedSelectedItems(AppConstants.CountryList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            stateNamee = getSavedSelectedItems(AppConstants.StateList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            cityNamee = getSavedSelectedItems(AppConstants.CityList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            instituteName = getSavedSelectedItems(AppConstants.institutionList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            tvLookingFor = getSavedSelectedItems(AppConstants.disciplineList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            tvFStudyLevel = getSavedSelectedItems(AppConstants.studyLevelList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }
            tvIntakeSelector = getSavedSelectedItems(AppConstants.IntakeList)
                .mapNotNull { it.toIntOrNull() }.map { it.toString() }


            if (countryNamee.isNotEmpty() ||
                stateNamee.isNotEmpty() ||
                cityNamee.isNotEmpty() ||
                instituteName.isNotEmpty() ||
                tvFStudyLevel.isNotEmpty() ||
                tvLookingFor.isNotEmpty() ||
                tvIntakeSelector.isNotEmpty() ||
                tvPGMP != null ||
                tvAttendance != null ||
                tvProgramType != null ||
                tvminTutionFee != null ||
                tvMaxTutionFee != null ||
                tvMinApplicationFee != null ||
                tvMaxApplicationFee != null ||
                tvAccomodation != null ||
                tvFEnglishLevel != null ||
                tvFAge != null
            ) {

                binding.filterDot.visibility = View.VISIBLE
                ProgramFilterFragment.clearData = 0
                loadFilteredData()

            } else {
                binding.filterDot.visibility = View.GONE
                ProgramFilterFragment.clearData = 1
                loadInitialData(search, category, isRecommended)
            }
            binding.rvCategory.visibility = View.GONE
            binding.rll.visibility = View.VISIBLE
            binding!!.view1.visibility = View.GONE
            binding!!.view2.visibility = View.VISIBLE
        }


//
//        binding!!.rvFpAp.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                if (dy > 50 && isBottomNavVisible) { // Scrolling down
//                    hideBottomNav()
//                    isBottomNavVisible = false
//                } else if (dy < -50 && !isBottomNavVisible) { // Scrolling up
//                    showBottomNav()
//                    isBottomNavVisible = true
//                }
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isBottomNavVisible) {
//                    showBottomNav()
//                    isBottomNavVisible = true
//                }
//            }
//        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun hideBottomNav() {
        MainActivity.bottomNav?.animate()?.translationY(MainActivity.bottomNav!!.height.toFloat())
            ?.setDuration(300)?.start()
    }

    private fun showBottomNav() {
        MainActivity.bottomNav?.animate()?.translationY(0f)?.setDuration(300)?.start()
    }


    private fun searchData() {

        binding.ibFpSearch.setOnClickListener {

            search = binding.etFpSearch.text.toString()


            if (search.isNullOrEmpty()) {
                CommonUtils.toast(requireActivity(), "Please enter program name")
            } else {
                arrayList1.clear()
                adapterProgramsAllProg.notifyDataSetChanged()
                resetPagination()
                loadInitialData(search, category, isRecommended)
            }
            binding.etFpSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // No action needed before text is changed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No action needed during text change
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrEmpty()) {
                        arrayList1.clear()
                        adapterProgramsAllProg.notifyDataSetChanged()
                        resetPagination()
                        search = null
                        // Reload initial data when the search field is cleared
                        loadInitialData(search, category, isRecommended)
                    }
                }
            })


        }
    }


    private fun onClicks() {


        binding.fabFpFilterStart.setOnClickListener {
            if (sharedPre!!.getString(AppConstants.SCOUtLOGIN, "") == "true") {
                Navigation.findNavController(binding.root).navigate(R.id.programFilterFragment2)
            } else {
                Navigation.findNavController(binding.root).navigate(R.id.programFilterFragment)
            }


        }
        binding.fabFpHeart.setOnClickListener {

            Navigation.findNavController(binding.root).navigate(R.id.shortListedFragment)

        }

        binding.fabFpNotificationStu.setOnClickListener {

            Navigation.findNavController(binding.root).navigate(R.id.fragmentNotification)

        }
    }


    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFpAp.layoutManager = layoutManager


        adapterProgramsAllProg = AdapterProgramsAllProg(requireActivity(), arrayList1, this)
        binding.rvFpAp.adapter = adapterProgramsAllProg

        binding.rvFpAp.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                    loadMoreData(category)
                }
            }
        })
    }

    private fun loadFilteredData() {
        // Clear previous data
        arrayList1.clear()
        adapterProgramsAllProg.notifyDataSetChanged()
        resetPagination()
        loadInitialData(search, category, isRecommended)
    }


    private fun loadInitialData(search: String?, category: String?, isRecommended: String) {
        if (!isLoading) {
            isLoading = true
            binding.pbFpAp.visibility = View.VISIBLE


            onGetAllPrograms(
                presentPage,
                dataPerPage,
                countryNamee,
                stateNamee,
                cityNamee,
                instituteName,
                tvPGMP,
                tvFStudyLevel,
                tvLookingFor,
                tvAttendance,
                tvProgramType,
                tvIntakeSelector,
                tvminTutionFee,
                tvMaxTutionFee,
                tvMinApplicationFee,
                tvMaxApplicationFee,
                search,
                category,
                tvAccomodation,
                tvFEnglishLevel,
                tvFAge,
                isRecommended
            )


        }
    }

    private fun loadMoreData(category: String?) {
        if (!isLoading && nextPage != 0 && presentPage < nextPage) {
            isLoading = true
            binding.pbFpApPagination.visibility = View.VISIBLE
            presentPage++
            onGetAllPrograms(
                presentPage,
                dataPerPage,
                countryNamee,
                stateNamee,
                cityNamee,
                instituteName,
                tvPGMP,
                tvFStudyLevel,
                tvLookingFor,
                tvAttendance,
                tvProgramType,
                tvIntakeSelector,
                tvminTutionFee,
                tvMaxTutionFee,
                tvMinApplicationFee,
                tvMaxApplicationFee,
                search,
                category,
                tvAccomodation,
                tvFEnglishLevel,
                tvFAge,
                isRecommended
            )
        }
    }

    private fun setRecyclerViewVisibility() {
        binding.llFpApNoData.isVisible = arrayList1.isEmpty()
        adapterProgramsAllProg.notifyDataSetChanged()
        isLoading = false
        binding.pbFpApPagination.visibility = View.GONE
        binding.pbFpAp.visibility = View.GONE
        binding!!.swipeRefreshLayout.isRefreshing = false

    }

    private fun onGetAllPrograms(
        presentPage: Int,
        dataPerPage: Int,
        countryName: List<String>,
        stateName: List<String>,
        cityName: List<String>,
        instituteName: List<String>,
        tvPGMP: String?,
        tvFStudyLevel: List<String>,
        tvLookingFor: List<String>,
        tvAttendance: String?,
        tvProgramType: String?,
        tvIntakeSelector: List<String>,
        tvminTutionFee: String?,
        tvMaxTutionFee: String?,
        tvMinApplicationFee: String?,
        tvMaxApplicationFee: String?,
        search: String?,
        category: String?,
        tvAccomodation: String?,
        tvFEnglishLevel: String?,
        tvFAge: String?,
        isRecommended: String
    ) {
        ViewModalClass().getAllProgramsModalLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken,
            presentPage,
            dataPerPage,
            countryName,
            stateName,
            cityName,
            instituteName,
            tvPGMP,
            tvFStudyLevel,
            tvLookingFor,
            tvAttendance,
            tvProgramType,
            tvIntakeSelector,
            tvminTutionFee,
            tvMaxTutionFee,
            tvMinApplicationFee,
            tvMaxApplicationFee,
            search,
            category,
            tvAccomodation,
            tvFEnglishLevel,
            tvFAge,
            isRecommended
        ).observe(viewLifecycleOwner) { allProgramModal: AllProgramModel? ->
            allProgramModal?.let { nonNullForgetModal ->
                if (view != null) {
                    if (allProgramModal.statusCode == 200) {
                        nextPage = allProgramModal.data?.metaInfo?.nextPage ?: 0
                        // Only clear list when loading the first page
                        if (presentPage == 1) {
                            arrayList1.clear()
                        }
                        arrayList1.addAll(allProgramModal.data?.records ?: emptyList())
                    } else {
                        CommonUtils.toast(requireActivity(), nonNullForgetModal.message ?: "Failed")
                    }
                    setRecyclerViewVisibility()
                }
            }
        }
    }


    override fun onCLick(record: Record) {
        FragProgramDetailDetails.details = record
        if (sharedPre!!.getString(AppConstants.SCOUtLOGIN, "") == "true") {
            binding!!.root.findNavController().navigate(R.id.fragProgramDetailDetails2)
        } else {
            binding!!.root.findNavController().navigate(R.id.fragProgramDetailDetails)
        }


    }

    override fun likeClick(record: Record, pos: Int) {
        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey
        Log.d("shortlisted", record.identifier)

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

    override fun disLikeCLick(record: Record, pos: Int) {
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

    override fun openDialogCLick(record: Record, position: Int) {
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
        val layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
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

    private fun removeFromShortlist(record: Record) {
        arrayList1.remove(record)
        adapterProgramsAllProg.notifyDataSetChanged()
        setRecyclerViewVisibility()
    }

    private fun generateRandomHexString(length: Int): String {
        val hexChars = "0123456789abcdef"
        return (1..length).map { hexChars[Random.nextInt(hexChars.length)] }.joinToString("")
    }

    override fun onResume() {
        super.onResume()

        sharedPre!!.getString(AppConstants.USER_IMAGE, "")!!.trim('"')
//
//        Glide.with(requireActivity())
//            .load(imageUrl).error(R.drawable.test_image)
//            .into(binding!!.civProfileImageFd2)

        binding.etFpSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text is changed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed during text change
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    arrayList1.clear()
                    adapterProgramsAllProg.notifyDataSetChanged()
                    resetPagination()
                    search = null
                    // Reload initial data when the search field is cleared
                    loadInitialData(search, category, isRecommended)
                }
            }
        })

        binding?.rvFpAp?.post {
            val layoutManager = binding?.rvFpAp?.layoutManager as? LinearLayoutManager
            layoutManager?.scrollToPositionWithOffset(recyclerViewPosition, 0)
        }
        if (sharedPre!!.getString(AppConstants.SCOUtLOGIN, "") == "true") {
            ScoutMainActivity.bottomNav!!.visibility = View.VISIBLE
        } else {
            MainActivity.bottomNav!!.visibility = View.VISIBLE
        }

        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            // Below Android 11
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }

    private fun resetPagination() {
        presentPage = 1
        nextPage = 0
        isLoading = false
        binding!!.swipeRefreshLayout.isRefreshing = false

    }


    override fun onDestroyView() {
        super.onDestroyView()
        arrayList1.clear()
        adapterProgramsAllProg.notifyDataSetChanged()
    }


    private fun getSavedSelectedItems(keyPrefix: String): List<String> {
        val sharedPrefs = SharedPrefs(requireContext())
        val ids = sharedPrefs.getStringList("${keyPrefix}Id") ?: emptyList()

        return ids
    }

    private fun getProgramCategoryStat(requireActivity: FragmentActivity) {
        // Check if data is already available, if not, make the API call
        if (arrayListCategory.isNullOrEmpty()) {
            ViewModalClass().getCategoryProgramList(
                requireActivity,
                AppConstants.fiClientNumber,
                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer " + CommonUtils.accessToken
            ).observe(viewLifecycleOwner) { getCategoryProgramModal: getCategoryProgramModel? ->
                getCategoryProgramModal?.let { modal ->
                    if (view != null) {
                        if (modal.statusCode == 200) {
                            modal.data?.let { dataList ->
                                if (dataList.isNotEmpty()) {
                                    arrayListCategory.clear()

                                    arrayListCategory.addAll(dataList)
                                } else {
                                    CommonUtils.toast(requireActivity, "No categories found")
                                }
                            } ?: CommonUtils.toast(requireActivity, "No data available")
                        } else {
                            CommonUtils.toast(requireActivity, modal.message ?: "Failed")
                        }


                        setupRecyclerViewCategory(arrayListCategory)
                    }
                }
            }
        } else {
            setupRecyclerViewCategory(arrayListCategory)
        }


    }

    private fun setupRecyclerViewCategory(items: List<com.student.Compass_Abroad.modal.getCategoryProgramModel.Data>) {
        if (isAdded) {  // Check if the fragment is added to an activity
            categoryProgramAdapter =
                CategoryProgramAdapter(
                    requireActivity(),
                    items,
                    object : CategoryProgramAdapter.Select {
                        override fun onCLick(
                            record: com.student.Compass_Abroad.modal.getCategoryProgramModel.Data,
                            position: Int
                        ) {

                            val newStatus = when (record.name) {
                                "higher_education" -> 0
                                "summer_school" -> 2
                                "career_program" -> 3
                                else -> 1
                            }

                            // Only proceed if the status has changed
                            if (status != newStatus) {
                                status = newStatus
                                category = record.name
                                presentPage = 1
                                nextPage = 0
                                isLoading = false
                                isScrolling = false
                                arrayList1.clear() // Clear the list
                                adapterProgramsAllProg.notifyDataSetChanged()

                                // Show progress bar
                                binding.pbFpAp.visibility = View.VISIBLE

                                if (BuildConfig.FLAVOR == "Admisiony") {


                                } else {
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



                                if (category == "higher_education") {
                                    ProgramFilterFragment.data = category
                                    ProgramFilterFragment.clearData = 1
                                    category = "higher_education"
                                    status = 0
                                    sharedPre!!.saveString("status", status.toString())
                                } else if (category == "summer_school") {
                                    ProgramFilterFragment.data = category
                                    ProgramFilterFragment.clearData = 1
                                    category = "summer_school"
                                    status = 2
                                    sharedPre!!.saveString("status", status.toString())
                                } else if (category == "career_program") {
                                    ProgramFilterFragment.data = category
                                    ProgramFilterFragment.clearData = 1
                                    category = "career_program"
                                    status = 3
                                    sharedPre!!.saveString("status", status.toString())
                                } else {
                                    ProgramFilterFragment.data = category
                                    ProgramFilterFragment.clearData = 1
                                    category = "language_program"
                                    status = 1
                                    sharedPre!!.saveString("status", status.toString())
                                }

                                val PGMP = sharedPre!!.getLabelValue(AppConstants.PGWP_KEY)

                                if (PGMP != null) {
                                    tvPGMP = PGMP.second

                                } else {
                                    tvPGMP = null
                                }

                                val Attendence =
                                    sharedPre!!.getLabelValue(AppConstants.ATTENDANCE_KEY)

                                if (Attendence != null) {
                                    tvAttendance = Attendence.second

                                } else {
                                    tvAttendance = null
                                }

                                val ProgramTypeKey =
                                    sharedPre!!.getLabelValue(AppConstants.PROGRAM_TYPE_KEY)

                                if (ProgramTypeKey != null) {
                                    tvProgramType = ProgramTypeKey.second

                                } else {
                                    tvProgramType = null
                                }

                                val Accomodation =
                                    sharedPre!!.getLabelValue(AppConstants.Accomodation)

                                if (Accomodation != null) {
                                    tvAccomodation =
                                        Accomodation.second  // Second element is the value

                                } else {
                                    tvAccomodation = null
                                }

                                val englishLevel =
                                    sharedPre!!.getLabelValue(AppConstants.EnglishLevelList) // This returns a Pair<String, String> or null if no data found

                                if (englishLevel != null) {
                                    tvFEnglishLevel =
                                        englishLevel.second  // Second element is the value

                                } else {
                                    tvFEnglishLevel = null
                                }

                                val age =
                                    sharedPre!!.getLabelValue(AppConstants.AgeList) // This returns a Pair<String, String> or null if no data found

                                if (age != null) {

                                    tvFAge = age.second

                                } else {
                                    tvFAge = null
                                }


                                tvminTutionFee =
                                    sharedPre!!.getString11(AppConstants.MIN_TUTION_KEY)
                                tvMaxTutionFee =
                                    sharedPre!!.getString11(AppConstants.MAX_TUTION_KEY)
                                tvMinApplicationFee =
                                    sharedPre!!.getString11(AppConstants.MIN_APPLICATION_KEY)
                                tvMaxApplicationFee =
                                    sharedPre!!.getString11(AppConstants.MAX_APPLICATION_KEY)

                                tvPGMP = if (tvPGMP.isNullOrEmpty()) null else tvPGMP
                                tvAttendance =
                                    if (tvAttendance.isNullOrEmpty()) null else tvAttendance
                                tvProgramType =
                                    if (tvProgramType.isNullOrEmpty()) null else tvProgramType
                                tvAccomodation =
                                    if (tvAccomodation.isNullOrEmpty()) null else tvAccomodation
                                tvFEnglishLevel =
                                    if (tvFEnglishLevel.isNullOrEmpty()) null else tvFEnglishLevel
                                tvFAge = if (tvFAge.isNullOrEmpty()) null else tvFAge

                                tvminTutionFee =
                                    if (tvminTutionFee.isNullOrEmpty()) null else tvminTutionFee
                                tvMaxTutionFee =
                                    if (tvMaxTutionFee.isNullOrEmpty()) null else tvMaxTutionFee
                                tvMinApplicationFee =
                                    if (tvMinApplicationFee.isNullOrEmpty()) null else tvMinApplicationFee
                                tvMaxApplicationFee =
                                    if (tvMaxApplicationFee.isNullOrEmpty()) null else tvMaxApplicationFee

                                binding.filterDot.visibility = View.GONE
                                countryNamee = getSavedSelectedItems(AppConstants.CountryList)
                                    .mapNotNull { it.toIntOrNull() }.map { it.toString() }
                                stateNamee = getSavedSelectedItems(AppConstants.StateList)
                                    .mapNotNull { it.toIntOrNull() }.map { it.toString() }
                                cityNamee = getSavedSelectedItems(AppConstants.CityList)
                                    .mapNotNull { it.toIntOrNull() }.map { it.toString() }
                                instituteName = getSavedSelectedItems(AppConstants.institutionList)
                                    .mapNotNull { it.toIntOrNull() }.map { it.toString() }
                                tvLookingFor = getSavedSelectedItems(AppConstants.disciplineList)
                                    .mapNotNull { it.toIntOrNull() }.map { it.toString() }
                                tvFStudyLevel = getSavedSelectedItems(AppConstants.studyLevelList)
                                    .mapNotNull { it.toIntOrNull() }.map { it.toString() }
                                tvIntakeSelector = getSavedSelectedItems(AppConstants.IntakeList)
                                    .mapNotNull { it.toIntOrNull() }.map { it.toString() }

                                if (countryNamee.isNotEmpty() ||
                                    stateNamee.isNotEmpty() ||
                                    cityNamee.isNotEmpty() ||
                                    instituteName.isNotEmpty() ||
                                    tvFStudyLevel.isNotEmpty() ||
                                    tvLookingFor.isNotEmpty() ||
                                    tvIntakeSelector.isNotEmpty() ||
                                    tvPGMP != null ||
                                    tvAttendance != null ||
                                    tvProgramType != null ||
                                    tvminTutionFee != null ||
                                    tvMaxTutionFee != null ||
                                    tvMinApplicationFee != null ||
                                    tvMaxApplicationFee != null ||
                                    tvAccomodation != null ||
                                    tvFEnglishLevel != null ||
                                    tvFAge != null
                                ) {

                                    binding.filterDot.visibility = View.VISIBLE
                                    ProgramFilterFragment.clearData = 0
                                    loadFilteredData()

                                } else {
                                    binding.filterDot.visibility = View.GONE
                                    ProgramFilterFragment.clearData = 1
                                    loadInitialData(search, category, isRecommended)
                                }


                            }
                        }

                    })
            categoryLayoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            binding.rvCategory.layoutManager = categoryLayoutManager
            binding.rvCategory.adapter = categoryProgramAdapter
            status?.let { categoryProgramAdapter.setSelectedItem(it) }

        }
    }

    private fun clearAllSelectedValues() {
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

    private fun updateTabUI(isRecommended: Boolean) {
        if (isRecommended) {
            binding!!.view1.visibility = View.VISIBLE
            binding!!.view2.visibility = View.GONE
            binding.rvCategory.visibility = View.GONE
            binding.rll.visibility = View.GONE
        } else {
            binding!!.view1.visibility = View.GONE
            binding!!.view2.visibility = View.VISIBLE
            binding.rvCategory.visibility = View.GONE
            binding.rll.visibility = View.VISIBLE
        }
    }

}
