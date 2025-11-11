package com.student.Compass_Abroad.modal.getProgramFilters

import com.student.Compass_Abroad.modal.getProgramFilters.AttendanceOn
import com.student.Compass_Abroad.modal.getProgramFilters.City
import com.student.Compass_Abroad.modal.getProgramFilters.Country
import com.student.Compass_Abroad.modal.getProgramFilters.Discipline
import com.student.Compass_Abroad.modal.getProgramFilters.Fees
import com.student.Compass_Abroad.modal.getProgramFilters.Institution
import com.student.Compass_Abroad.modal.getProgramFilters.Intake
import com.student.Compass_Abroad.modal.getProgramFilters.IsPGWP
import com.student.Compass_Abroad.modal.getProgramFilters.ProgramType
import com.student.Compass_Abroad.modal.getProgramFilters.State
import com.student.Compass_Abroad.modal.getProgramFilters.Studylevel

data class Data(
    val attendanceOn: List<AttendanceOn>,
    val cities: List<City>,
    val countries: List<Country>,
    val disciplines: List<Discipline>,
    val institutions: List<Institution>,
    val intakes: List<Intake>,
    val isPGWP: List<IsPGWP>,
    val programType: List<ProgramType>,
    val states: List<State>,
    val studylevels: List<Studylevel>,
    val fees: List<Fees>,
    val hasAccommodation: List<Accomodation>,
    val englishLevel: List<EnglishLevel>,
    val age: List<Age>
)