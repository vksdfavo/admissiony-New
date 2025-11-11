package com.student.Compass_Abroad.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _refreshTrigger = MutableLiveData<Unit>()
    val refreshTrigger: LiveData<Unit> = _refreshTrigger
    val _currentTabPosition = MutableLiveData<Int>()
    val currentTabPosition: LiveData<Int> = _currentTabPosition


    fun triggerRefresh() {

        _refreshTrigger.value = Unit
    }

    private val _selectedTab = MutableLiveData<String>("all")
    val selectedTab: LiveData<String> = _selectedTab

    private val _selectedSubTab = MutableLiveData<String>("today")
    val selectedSubTab: LiveData<String> = _selectedSubTab

    fun updateSelectedTab(tab: String) {
        _selectedTab.value = tab
    }

    private val _profilePictureUrl = MutableLiveData<String>()
    val profilePictureUrl: LiveData<String> get() = _profilePictureUrl

    fun updateSelectedSubTab(subTab: String) {
        _selectedSubTab.value = subTab
    }

    private val _refreshDataEvent = MutableLiveData<Boolean>()
    val refreshDataEvent: LiveData<Boolean> get() = _refreshDataEvent

    fun triggerRefreshData() {
        _refreshDataEvent.value = true
    }

}