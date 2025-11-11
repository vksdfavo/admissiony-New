package com.student.Compass_Abroad.viewmodel

import androidx.lifecycle.ViewModel
import com.student.Compass_Abroad.modal.preferCountryList.Data

class SetPreferencesViewModel : ViewModel() {
    val selectedDisciplines = mutableListOf<Data>()

}
