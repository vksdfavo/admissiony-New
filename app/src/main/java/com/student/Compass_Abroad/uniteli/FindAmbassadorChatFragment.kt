package com.student.Compass_Abroad.uniteli

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.databinding.AmbassadorBioLayoutBinding
import com.student.Compass_Abroad.uniteli.adapter.FindAmbassadorChatAdapter
import com.student.Compass_Abroad.databinding.FragmentNewAmbassadorChatBinding
import com.student.Compass_Abroad.databinding.SliderDataLayoutBinding
import com.student.Compass_Abroad.fragments.BaseFragment
import com.student.Compass_Abroad.modal.findAmbassadorModal.RecordsInfo
import com.student.Compass_Abroad.retrofit.ViewModalClass
import androidx.core.graphics.drawable.toDrawable

class FindAmbassadorChatFragment : Fragment(), FindAmbassadorChatAdapter.OnChatClick {
    private lateinit var binding: FragmentNewAmbassadorChatBinding
    private var ambassadorAdapter: FindAmbassadorChatAdapter? = null
    private val ambassadorList: MutableList<RecordsInfo> = mutableListOf()
    private val viewModel: ViewModalClass by lazy { ViewModalClass() }
    private var currentPage = 1
    private var perPage = 25
    private var isLoading = false
    private var hasNextPage = true
    var userID: String = ""
    var searchUser: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewAmbassadorChatBinding.inflate(inflater, container, false)

        userID = App.sharedPre!!.getString(AppConstants.USER_ID, "").toString()

        setApplicationActiveRecyclerview()
        fetchDataFromApi(searchUser)
        searchList()

        return binding.root
    }

    private fun searchList() {
        binding.ibFpSearch.setOnClickListener {
             searchUser = binding.etFpSearch.text.toString().trim()

            // Reset pagination
            currentPage = 1
            hasNextPage = true
            isLoading = false

            ambassadorList.clear()
            ambassadorAdapter?.notifyDataSetChanged()

            fetchDataFromApi(searchUser)
        }

        // Optional: Trigger search when user clears the EditText
        binding.etFpSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrBlank()) {
                    currentPage = 1
                    hasNextPage = true
                    isLoading = false

                    ambassadorList.clear()
                    ambassadorAdapter?.notifyDataSetChanged()

                    fetchDataFromApi("") // Load all data again
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    private fun setApplicationActiveRecyclerview() {
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvFaActive.layoutManager = layoutManager

        ambassadorAdapter = FindAmbassadorChatAdapter(requireActivity(), ambassadorList, this)
        binding.rvFaActive.adapter = ambassadorAdapter
        binding.rvFaActive.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && hasNextPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                        firstVisibleItemPosition >= 0
                    ) {

                        fetchDataFromApi(searchUser)
                    }
                }
            }
        })
    }

    private fun fetchDataFromApi(searchQuery: String = "") {
        if (!hasNextPage || isLoading) return

        isLoading = true
        if (currentPage == 1) {
            binding.pbFaActive.visibility = View.VISIBLE
        } else {
            binding.pbFaActivePagination.visibility = View.VISIBLE
        }

        viewModel.getAmbassadorLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            currentPage,
            perPage,
            0,
            "DESC",
            "ID",
            searchQuery  // Pass the search query here
        ).observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.statusCode == 201 || it.statusCode == 200 && it.success) {
                    val programResponse = it.data?.recordsInfo ?: emptyList()

                    if (currentPage == 1) ambassadorList.clear()
                    ambassadorList.addAll(programResponse)
                    ambassadorAdapter?.notifyDataSetChanged()

                    hasNextPage = it.data?.metaInfo?.hasNextPage ?: false
                    if (hasNextPage) currentPage++

                    if (ambassadorList.isEmpty()) {
                        binding.llFaActiveNoApplications.visibility = View.VISIBLE
                        binding.rvFaActive.visibility = View.GONE
                    } else {
                        binding.llFaActiveNoApplications.visibility = View.GONE
                        binding.rvFaActive.visibility = View.VISIBLE
                    }
                } else {
                    CommonUtils.toast(requireContext(), it.message ?: "Failed")
                    binding.llFaActiveNoApplications.visibility = View.VISIBLE
                    binding.rvFaActive.visibility = View.GONE
                }
            } ?: run {
                binding.llFaActiveNoApplications.visibility = View.VISIBLE
                binding.rvFaActive.visibility = View.GONE
            }

            isLoading = false
            binding.pbFaActive.visibility = View.GONE
            binding.pbFaActivePagination.visibility = View.GONE
        }
    }

    override fun onClick(recordInfo: RecordsInfo) {
        viewModel.joinAmbassadroChatLiveData(
            requireActivity(),
            AppConstants.fiClientNumber,
            App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
            "Bearer ${CommonUtils.accessToken}",
            recordInfo.user_id.toString()

        ).observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.statusCode == 201 && it.success) {

                    val bundle = Bundle().apply {

                        putString("relation_identifier", response.data?.relation_identifier)
                    }

                    App.sharedPre!!.saveString(
                        AppConstants.RELATION_IDENTIFIER,
                        response.data?.relation_identifier
                    )

                    App.singleton?.relationIdentifier = response.data?.relation_identifier
                    findNavController().navigate(R.id.fragmentAmbassadorGetChat, bundle)

                } else {

                    CommonUtils.toast(requireContext(), it.message ?: "Failed")

                }

            }
        }
    }

    override fun onClickBio(recordInfo: RecordsInfo) {
        val itemBinding = AmbassadorBioLayoutBinding.inflate(requireActivity().layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(itemBinding.root)
        dialog.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

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


        itemBinding.tvTitle.text = recordInfo.bio ?: "N/A"
    }
}