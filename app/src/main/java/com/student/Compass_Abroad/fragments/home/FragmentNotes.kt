package com.student.Compass_Abroad.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.Utils.CommonUtils
import com.student.Compass_Abroad.adaptor.AdapterFragmentNotes
import com.student.Compass_Abroad.adaptor.generateRandomHexString
import com.student.Compass_Abroad.databinding.FragmentNotesBinding
import com.student.Compass_Abroad.encrytion.encryptData
import com.student.Compass_Abroad.retrofit.ViewModalClass
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.student.Compass_Abroad.fragments.BaseFragment
import org.json.JSONObject


class FragmentNotes : BaseFragment() {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var adapterFragmentNotes: AdapterFragmentNotes
    private val applicationNotesList: MutableList<com.student.Compass_Abroad.modal.getApplicationNotes.Record> = mutableListOf()
    private var currentPage = 1
    private var isLastPage = false
    private val perPage = 10
    var contentKey=""
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private val viewModel: ViewModalClass by lazy {
        ViewModalClass()
    }

    companion object {
        var data: com.student.Compass_Abroad.modal.getApplicationResponse.Record? = null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding=FragmentNotesBinding.inflate(inflater,container,false)
        setRecyclerView()
        setData()
        fetchDataFromApi(currentPage)
        return binding.root
    }

    private fun setData() {
      binding.fabVdAdd.setOnClickListener { v:View->
          showBottomSheetDialog()
      }
    }




@SuppressLint("MissingInflatedId")
private fun showBottomSheetDialog() {
    val bottomSheetView = layoutInflater.inflate(R.layout.bottomsheet_application_notes, null)
    bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheet2)
    bottomSheetDialog.setContentView(bottomSheetView)
    bottomSheetDialog.show()

    val imageView: ImageView = bottomSheetView.findViewById(R.id.back_btn)
    val titleEditText: EditText = bottomSheetView.findViewById(R.id.et_title)
    val etNotes: EditText = bottomSheetView.findViewById(R.id.et_notes)

    imageView.setOnClickListener {
        bottomSheetDialog.dismiss()
    }


    bottomSheetView.findViewById<View>(R.id.tvSp2_save).setOnClickListener {

        val title = titleEditText.text.toString()
        val notes = etNotes.text.toString()


        if (validateInputs(title,notes)) {

            postNotes(title, notes)
        }
    }
}

private fun validateInputs(title: String, notes: String): Boolean {
    return when {
        title.isEmpty() -> {
            Toast.makeText(requireContext(), "Please enter a title", Toast.LENGTH_SHORT).show()
            false
        }

        notes.isEmpty() -> {
            Toast.makeText(requireContext(), "Please enter notes", Toast.LENGTH_SHORT).show()
            false
        }

        else -> true
    }
}

private fun postNotes( title: String, notes: String) {

    val hexString = generateRandomHexString(16)
    val publicKey = hexString
    val privateKey = AppConstants.privateKey
    val appSecret = AppConstants.appSecret
    val ivHexString = "$privateKey$publicKey"

    val formData = JSONObject().apply {
        put("title", title)
        put("note", notes)
    }

    val formDataToBeEncrypted = formData.toString()
    val encryptedString = encryptData(formDataToBeEncrypted, appSecret, ivHexString)

    if (encryptedString != null) {
        contentKey = "$publicKey^#^$encryptedString"
        println("Encrypted data: $encryptedString")

        CommonUtils.accessToken?.let {
            data?.identifier?.let { it1 ->
                ViewModalClass().postApplicationNotesLiveData(
                    requireActivity(),
                    AppConstants.fiClientNumber,
                    App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                    "Bearer " + it,
                    it1,
                    contentKey,
                ).observe(requireActivity()) { postReminderResponse ->
                    postReminderResponse?.let { response ->
                        if (response.statusCode == 201) {
                            CommonUtils.toast(
                                requireActivity(),
                                response.message ?: "Notes Created Successfully"
                            )
                            currentPage=1
                            applicationNotesList.clear()
                            fetchDataFromApi(currentPage)
                            adapterFragmentNotes.notifyDataSetChanged()
                            bottomSheetDialog.dismiss()
                        } else if (response.statusCode == 409) {
                            CommonUtils.toast(requireActivity(), response.message ?: "Exists")
                            bottomSheetDialog.dismiss()
                        } else {
                            CommonUtils.toast(requireActivity(), response.message ?: "Notes Failed")
                        }
                    }

                }
            }
        }

    }
}

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.rvFa.layoutManager = layoutManager

        adapterFragmentNotes = AdapterFragmentNotes(requireActivity(), applicationNotesList)
        binding.rvFa.adapter = adapterFragmentNotes

        binding.rvFa.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!binding.pbPagination.isShown && !isLastPage &&
                    firstVisibleItemPosition + visibleItemCount >= totalItemCount && dy > 0) {
                    currentPage++
                    fetchDataFromApi(currentPage)
                }
            }
        })
    }

    private fun fetchDataFromApi(page: Int) {
        if (page == 1) {
            binding.pbFadAs.visibility = View.VISIBLE

        } else {
            binding.pbPagination.visibility = View.VISIBLE

        }

        data?.let {
            viewModel.getApplicationNotesResponseLiveData(
                requireActivity(),
                AppConstants.fiClientNumber,
                App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
                "Bearer ${CommonUtils.accessToken}",
                it.identifier,
                page,
                perPage,
                "desc",
                "id"
            ).observe(viewLifecycleOwner, Observer { response ->
                response?.let {
                    if (it.statusCode == 200 && it.success) {
                        val newApplicationNotes = it.data?.records ?: emptyList()
                        val metaInfo = it.data?.metaInfo

                        if (page == 1) {
                            applicationNotesList.clear()
                        }

                        applicationNotesList.addAll(newApplicationNotes)
                        adapterFragmentNotes.notifyDataSetChanged()

                        isLastPage = metaInfo?.isLastPage ?: true

                        if (applicationNotesList.isEmpty()) {
                            binding.llSaaNoData.visibility = View.VISIBLE
                            binding.rvFa.visibility = View.GONE
                        } else {
                            binding.llSaaNoData.visibility = View.GONE
                            binding.rvFa.visibility = View.VISIBLE
                        }
                    } else {
                        CommonUtils.toast(requireContext(), it.message ?: "Failed")
                        if (page == 1) {
                            binding.llSaaNoData.visibility = View.VISIBLE
                            binding.rvFa.visibility = View.GONE
                        }
                    }
                } ?: run {
                    if (page == 1) {
                        binding.llSaaNoData.visibility = View.VISIBLE
                        binding.rvFa.visibility = View.GONE
                    }
                }

                if (page == 1) {
                    binding.pbFadAs.visibility = View.GONE
                } else {
                    binding.pbPagination.visibility = View.GONE
                }
            })
        }
    }

}