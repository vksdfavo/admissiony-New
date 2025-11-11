package com.student.Compass_Abroad.fragments

import androidx.fragment.app.Fragment


class FragmentToday : BaseFragment() {
   /* private lateinit var leadLayoutManager: LinearLayoutManager


    private var currentPage = 1
    private var isLoading = false
    private var isScrolling = false
    private var dataPerPage = 10
    private lateinit var todayAdapter: TodayAdaptor
    private var todayRecords = mutableListOf<com.learn.overseas.modal.getSubWorkliiTabs.Record>()
    private var hasMorePages = true

      private lateinit var binding:FragmentTodayBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTodayBinding.inflate(inflater,container,false)
       // Toast.makeText(requireActivity(),data,Toast.LENGTH_SHORT).show()
        // Inflate the layout for this fragment
        setupRecyclerView()


        getDataFromApi(currentPage,dataPerPage, subselect, select)
        return binding.root
    }

    private fun setupRecyclerView() {
        todayAdapter=TodayAdaptor(requireActivity(),todayRecords )
        leadLayoutManager = LinearLayoutManager(requireActivity())
        binding.rvLd.layoutManager = leadLayoutManager
        binding.rvLd.adapter = todayAdapter

        binding.rvLd.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = leadLayoutManager.childCount
                val totalItemCount = leadLayoutManager.itemCount
                val firstVisibleItemPosition = leadLayoutManager.findFirstVisibleItemPosition()

                if (isScrolling && !isLoading && hasMorePages &&
                    (visibleItemCount + firstVisibleItemPosition >= totalItemCount)) {
                    isScrolling = false
                    binding.pbFpApPagination.visibility = View.VISIBLE
                    getDataFromApi(currentPage + 1, dataPerPage, subselect,select)
                }
            }
        })
    }

    private fun getDataFromApi(pageNo: Int, dataPerPage: Int, datavalue: String, data: String) {
    if (isLoading || !hasMorePages) return
    isLoading = true

    ViewModalClass().getSubWorkliTabsLiveData(
    requireActivity(),
    AppConstants.fiClientNumber,
    App.sharedPre?.getString(AppConstants.Device_IDENTIFIER, "") ?: "",
    "Bearer ${CommonUtils.accessToken}",
        datavalue,
        data,"assigned,working",currentPage,dataPerPage,"desc"
    ).observe(viewLifecycleOwner) { response ->
        response?.let {
            if (it.statusCode == 200 && it.success) {
                val todayResponse = it.data?.records
                val metaInfo = it.data?.metaInfo

                if (todayResponse.isNullOrEmpty()) {
                    if (todayRecords.isEmpty()) {
                        binding.llSaaNoData.visibility = View.VISIBLE
                        binding.rvLd.visibility = View.GONE
                    }
                    hasMorePages = false // No more pages to load
                } else {
                    binding.llSaaNoData.visibility = View.GONE
                    binding.rvLd.visibility = View.VISIBLE

                    todayResponse?.let { records ->
                        todayRecords.addAll(records)
                    }

                    currentPage = pageNo
                    hasMorePages = metaInfo?.hasNextPage == true

                    if (!hasMorePages) {
                        // If no more pages to load, show a toast
                        //CommonUtils.toast(requireActivity(), "No more leads to load")
                    }
                }
            } else {
                CommonUtils.toast(requireActivity(), it.message ?: "Failed")
                if (todayRecords.isEmpty()) {
                    binding.llSaaNoData.visibility = View.VISIBLE
                    binding.rvLd.visibility = View.GONE
                }
            }
        } ?: run {
            if (todayRecords.isEmpty()) {
                binding.llSaaNoData.visibility = View.VISIBLE
                binding.rvLd.visibility = View.GONE
            }
        }
        binding.pbFpApPagination.visibility = View.GONE // Hide progress bar
        isLoading = false // Reset loading state after API call completes
    }
    }

*/



}