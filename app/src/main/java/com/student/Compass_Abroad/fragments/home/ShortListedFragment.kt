package com.student.Compass_Abroad.fragments.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.App.Companion.sharedPre
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.adaptor.AdapterProgramsShortListedProgram
import com.student.Compass_Abroad.adaptor.CategoryProgramAdapter
import com.student.Compass_Abroad.adaptor.ProgramTagAdapter
import com.student.Compass_Abroad.databinding.FragmentShortListedBinding
import com.student.Compass_Abroad.databinding.ProgramTagsDialogBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.AllProgramModel.AllProgramModel
import com.student.Compass_Abroad.modal.AllProgramModel.Record
import com.student.Compass_Abroad.modal.getCategoryProgramModel.getCategoryProgramModel
import com.student.Compass_Abroad.modal.shortListModel.ShortListResponse
import com.student.Compass_Abroad.retrofit.ViewModalClass
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.random.Random

class ShortListedFragment : BaseFragment(), AdapterProgramsShortListedProgram.select {
    private var binding: FragmentShortListedBinding? = null
    private var arrayList1 = ArrayList<Record>()
    private lateinit var adapterShortListedProgram: AdapterProgramsShortListedProgram
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var categoryLayoutManager: LinearLayoutManager
    private lateinit var categoryProgramAdapter: CategoryProgramAdapter
    private var contentKey = ""
    private var dataPerPage = 25
    private var presentPage = 1
    private var nextPage = 0
    var category = "higher_education"
    var status1 = "self"

    companion object{
         var status: Int? = null
    }


    private val arrayListCategory: MutableList<com.student.Compass_Abroad.modal.getCategoryProgramModel.Data> =
        mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShortListedBinding.inflate(inflater, container, false)

        setupRecyclerView()

        binding!!.backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding!!.lll.setOnClickListener {
            if (arrayList1.isNotEmpty()) {
                CompareProgram.detail = arrayList1
                if (isAdded && isResumed) {
                    Navigation.findNavController(binding!!.root).navigate(R.id.compareProgram)

                }
            } else {
                Toast.makeText(requireActivity(), "No items to compare", Toast.LENGTH_LONG).show()
            }
        }

        binding!!.view1.visibility = View.VISIBLE
        binding!!.view2.visibility = View.GONE

        getProgramCategoryStat(requireActivity(), binding!!)

        binding!!.tabSelfShortlisted.setOnClickListener {
            binding?.pbFpAp?.visibility = View.VISIBLE

            presentPage=1
            nextPage=0
            arrayList1.clear()
            if (isAdded && isResumed) {
                onGetAllShorlistedPrograms(
                    requireActivity(),
                    dataPerPage,
                    presentPage,
                    category
                )

            }

            status1 = "self"
            App.singleton?.assignStaffFav = "0"

            binding!!.view1.visibility = View.VISIBLE
            binding!!.view2.visibility = View.GONE


        }

        binding!!.tabAssigedStaff.setOnClickListener {

            presentPage=1
            nextPage=0
            arrayList1.clear()
            adapterShortListedProgram.notifyDataSetChanged()
            status1 = "staff"
            if (isAdded && isResumed) {
                onGetAllShorlistedProgramsss(
                    requireActivity(),
                    dataPerPage,
                    presentPage,
                    category
                )

            }
            App.singleton?.assignStaffFav = "1"
            binding!!.view1.visibility = View.GONE
            binding!!.view2.visibility = View.VISIBLE
        }




        // Ensure the TabLayout is fully initialized


        return binding!!.root
    }


    private fun onGetAllShorlistedProgramsss(
        requireActivity: FragmentActivity,
        dataPerPage: Int,
        presentPage: Int,
        category:String
    ) {
        ViewModalClass().getshortlistedModalLiveDataStaff(
            requireActivity,
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer " + CommonUtils.accessToken,
            presentPage,
            dataPerPage,category
        ).observe(viewLifecycleOwner) { shorlistedProgramModal: AllProgramModel? ->
            shorlistedProgramModal?.let { nonNullForgetModal ->
                if (shorlistedProgramModal.statusCode == 200) {
                    nextPage = shorlistedProgramModal.data?.metaInfo?.nextPage ?: 0
                    val newRecords = shorlistedProgramModal.data?.records ?: emptyList()
                    newRecords.forEach { record ->
                        if (!arrayList1.contains(record)) {
                            arrayList1.add(record)
                        }
                    }
                    setRecyclerViewVisibility()
                } else {
                    CommonUtils.toast(requireActivity, nonNullForgetModal.message ?: "Failed")
                }
                binding?.pbFpAp?.visibility = View.GONE
                binding?.pbFpApPagination?.visibility = View.GONE
            }
        }
    }

    private fun getProgramCategoryStat(
        requireActivity: FragmentActivity,
        binding: FragmentShortListedBinding
    ) {
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

                                    arrayList1.clear()
                                    loadInitialData(category)
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


    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupRecyclerView() {
        linearLayoutManager = LinearLayoutManager(requireActivity())
        binding?.rvFpAp?.layoutManager = linearLayoutManager
        /*val dividerItemDecoration = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(requireActivity().resources.getDrawable(R.drawable.horizontal_line))
        binding?.rvFpAp?.addItemDecoration(dividerItemDecoration)*/
        adapterShortListedProgram = AdapterProgramsShortListedProgram(requireActivity(), arrayList1, this,viewLifecycleOwner)
        binding?.rvFpAp?.adapter = adapterShortListedProgram

        binding?.rvFpAp?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                // Trigger pagination when reaching the end
                if (!recyclerView.canScrollVertically(1) && nextPage > 0) {
                    if (presentPage < nextPage) {
                        presentPage++
                        binding?.pbFpApPagination?.visibility = View.VISIBLE

                        if (isAdded && isResumed)
                        {
                            Handler(Looper.getMainLooper()).postDelayed({
                                if (status1 == "self") {
                                    onGetAllShorlistedPrograms(requireActivity(), dataPerPage, presentPage, category)
                                } else {
                                    onGetAllShorlistedProgramsss(requireActivity(), dataPerPage, presentPage, category)
                                }
                            }, 1500)
                        }

                    }
                }
            }
        })

    }

    private fun loadInitialData(category: String) {
        binding?.pbFpAp?.visibility = View.VISIBLE
        arrayList1.clear()
        if (isAdded && isResumed)
        {
            onGetAllShorlistedPrograms(requireActivity(), dataPerPage, presentPage,category)

        }
    }

    private fun onGetAllShorlistedPrograms(
        requireActivity: FragmentActivity,
        dataPerPage: Int,
        presentPage: Int,
        category: String
    ) {
        // ✅ Show loading for 1 second before API call
        binding?.pbFpAp?.visibility = View.VISIBLE
        binding?.pbFpApPagination?.visibility = View.GONE
        viewLifecycleOwner.lifecycleScope.launch {
            ViewModalClass().getshortlistedModalLiveData(
                requireActivity,
                AppConstants.fiClientNumber,
                sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer " + CommonUtils.accessToken,
                presentPage,
                dataPerPage,
                category
            ).observe(viewLifecycleOwner) { shorlistedProgramModal: AllProgramModel? ->
                shorlistedProgramModal?.let { nonNullForgetModal ->
                    if (shorlistedProgramModal.statusCode == 200) {
                        nextPage = shorlistedProgramModal.data?.metaInfo?.nextPage ?: 0
                        val newRecords = shorlistedProgramModal.data?.records ?: emptyList()

                        adapterShortListedProgram.notifyDataSetChanged()
                        newRecords.forEach { record ->
                            if (!arrayList1.contains(record)) {
                                arrayList1.add(record)
                            }
                        }

                        setRecyclerViewVisibility()

                    } else {
                        if (isAdded && binding != null) {
                            CommonUtils.toast(
                                requireActivity,
                                nonNullForgetModal.message ?: "Failed"
                            )
                        }
                    }

                    if (isAdded && binding != null) {
                        binding?.pbFpAp?.visibility = View.GONE
                        binding?.pbFpApPagination?.visibility = View.GONE
                    }
                }
            }
        }

    }



    private fun setRecyclerViewVisibility() {
        if (arrayList1.isEmpty()) {
            binding?.llFpApNoData?.visibility = View.VISIBLE
        } else {
            binding?.llFpApNoData?.visibility = View.GONE
        }
        adapterShortListedProgram.notifyDataSetChanged()
    }

    override fun onCLick(record: Record) {
        ProgramDetails.details = record
        Navigation.findNavController(binding!!.root).navigate(R.id.programDetails)

    }

    override fun likeClick(
        record: Record,
        pos: Int
    ) {
        handleLikeOrDislike(record, pos)
    }

    override fun disLikeCLick(
        record: Record,
        pos: Int
    ) {
        handleLikeOrDislike(record, pos)
    }

    override fun openTagCLick(record: Record, position: Int) {
        val itemBinding = ProgramTagsDialogBinding.inflate(requireActivity().layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(itemBinding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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


        if (record.program.tags.isNotEmpty()) {

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

    private fun handleLikeOrDislike(
        record: Record,
        pos: Int
    ) {
        val hexString = generateRandomHexString(16)
        val publicKey = hexString
        val privateKey = AppConstants.privateKey
        val formData = JSONObject()
        formData.put("program_campus_identifier", record.identifier)


        val data = formData.toString()
        val dataToEncrypt = data
        val app_secret = AppConstants.appSecret
        val ivHexString = "$privateKey$publicKey"
        val encryptedString = encryptData(dataToEncrypt, app_secret, ivHexString)
        if (encryptedString != null) {
            contentKey = "$publicKey^#^$encryptedString"
            Log.d("shorlisted", contentKey)
            removeFromShortlist(pos)
        } else {
            Log.d("shorlisted", "Encryption failed.")
        }
    }

    private fun removeFromShortlist(pos: Int) {
        addToShortlist(requireActivity(), contentKey) {
            arrayList1.removeAt(pos)
            adapterShortListedProgram.notifyItemRemoved(pos)
            setRecyclerViewVisibility()
        }
    }

    private fun addToShortlist(
        requireActivity: FragmentActivity,
        content: String,
        onSuccess: () -> Unit
    ) {
        ViewModalClass().getshorListModalLiveData(
            requireActivity,
            AppConstants.fiClientNumber,
            sharedPre?.getString(AppConstants.Device_IDENTIFIER, "")!!,
            "Bearer " + CommonUtils.accessToken, content
        ).observe(requireActivity) { allShorListModal: ShortListResponse? ->
            allShorListModal?.let { nonNullForgetModal ->
                if (allShorListModal.statusCode == 200) {
                    onSuccess()
                } else {
                    CommonUtils.toast(requireActivity, allShorListModal.message ?: "Failed")
                }
            }
        }
    }

    fun generateRandomHexString(length: Int): String {
        val hexChars = "0123456789abcdef"
        return (1..length)
            .map { hexChars[Random.nextInt(hexChars.length)] }
            .joinToString("")
    }

    override fun onResume() {
        super.onResume()
        if (binding != null) {
            if (status1 == "self") {
                binding?.view1?.visibility = View.VISIBLE
                binding?.view2?.visibility = View.GONE
                arrayList1.clear()

                adapterShortListedProgram.notifyDataSetChanged()

                if (isAdded && isResumed) {
                    if (category != null) {
                        onGetAllShorlistedPrograms(
                            requireActivity(),
                            dataPerPage,
                            presentPage,
                            category
                        )
                    }
                }
                status1 = "self"
                App.singleton?.assignStaffFav = "0"
            } else {
                binding?.view1?.visibility = View.GONE
                binding?.view2?.visibility = View.VISIBLE
                arrayList1.clear()
                adapterShortListedProgram.notifyDataSetChanged()
                status1 = "staff"
                App.singleton?.assignStaffFav = "1"
                if (isAdded && isResumed) {
                    if (category != null) {
                        onGetAllShorlistedProgramsss(
                            requireActivity(),
                            dataPerPage,
                            presentPage,
                            category
                        )
                    }
                }
            }
            MainActivity.bottomNav?.isVisible = false
        }
    }



    private fun setupRecyclerViewCategory(items: List<com.student.Compass_Abroad.modal.getCategoryProgramModel.Data>) {
        if (!isAdded) return // Ensure Fragment is attached

        categoryProgramAdapter = CategoryProgramAdapter(
            requireActivity(),
            items,
            object : CategoryProgramAdapter.Select {
                override fun onCLick(record: com.student.Compass_Abroad.modal.getCategoryProgramModel.Data, position: Int) {
                    category = record.name

                    if (status1 == "staff") {
                        arrayList1.clear()
                        adapterShortListedProgram.notifyDataSetChanged()

                        if (isResumed) {
                            onGetAllShorlistedProgramsss(requireActivity(), dataPerPage, presentPage, category)
                        }
                    } else {
                        presentPage = 1 // Reset page
                        loadInitialData(category)
                    }

                    // Save category
                    sharedPre?.saveString("category", category)
                    val value = sharedPre?.getString("category", "").orEmpty()


                    // Update status based on category
                    status = when (value) {
                        "higher_education" -> 0
                        "language_program" -> 1
                        "career_program" -> 3
                        else -> 2
                    }

                    sharedPre?.saveString("status", status.toString())
                }
            }
        )

        categoryLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding?.rvCategory?.apply {
            layoutManager = categoryLayoutManager
            adapter = categoryProgramAdapter
        }

        status?.let { categoryProgramAdapter.setSelectedItem(it) }
    }


}