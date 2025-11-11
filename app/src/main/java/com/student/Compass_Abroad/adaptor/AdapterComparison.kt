package com.student.Compass_Abroad.adaptor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.Utils.AppConstants
import com.student.Compass_Abroad.databinding.ItemComparisonBinding
import java.util.ArrayList

class AdapterComparison(
    var requireActivity: FragmentActivity,
    var arrayList1: ArrayList<com.student.Compass_Abroad.modal.AllProgramModel.Record>,


    ) : RecyclerView.Adapter<AdapterComparison.MyViewHolder>() {


    override fun onCreateViewHolder(

        parent: ViewGroup, viewType: Int,
    ): MyViewHolder {
        val binding =
            ItemComparisonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = arrayList1[position]
        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return arrayList1.size
    }

    class MyViewHolder(
        var binding: ItemComparisonBinding,

        ) : RecyclerView.ViewHolder(

        binding.getRoot()
    ) {
        @SuppressLint("SetTextI18n")
        fun bind(record: com.student.Compass_Abroad.modal.AllProgramModel.Record) {
            binding.apply {
                tvProgramName.text = record.program.name

                val isLanguageProgram = record.program?.additional_items?.duration_range != null
                val isHigherEducation = record.program?.duration != null
                val durationType = record.program.duration_type ?: ""
                val symbol = record?.program?.institution?.country?.currency_symbol
                val start_date = record?.program?.additional_items?.start_date
                val age = record?.program?.additional_items?.age
                binding.startDate.text = start_date
                binding.age.text = age

                // Tuition Fee Based on Program Type
                if (isLanguageProgram) {
                    val tuitionFee = record.program?.additional_items?.tuition_fee ?: ""
                    tvTutionFee.text = "$tuitionFee "
                } else {
                    val tuitionFee = record.tuition_fee?.toString() ?: ""
                    val currencyCode = record.program?.institution?.country?.currency_code ?: ""
                    tvTutionFee.text = "$currencyCode $symbol $tuitionFee "
                }


                // Duration Based on Program Type
                tvDuration.text = "Duration"
                val duration = if (isHigherEducation) {
                    "${record.program?.duration ?: ""} $durationType"
                } else {
                    record.program?.additional_items?.duration_range ?: ""
                }
                tvDuration.text = duration



                if (isLanguageProgram) {
                    val applicationFee = record?.program?.additional_items?.application_fee ?: ""
                    binding.tvApplicationFee.text = "$applicationFee "
                } else {
                    val applicationFee = record?.application_fee?.toString() ?: ""
                    val currencyCode = record.program?.institution?.country?.currency_code ?: ""
                    binding.tvApplicationFee.text = "$currencyCode$symbol $applicationFee "
                }



                if (isLanguageProgram) {
                    labelIntakes.visibility = View.GONE
                    tvIntake.visibility = View.GONE
                } else {
                    tvIntake.text = if (record.program.intakes.isNotEmpty()) {
                        record.program.intakes.joinToString(", ") { it.intake_name ?: "Unknown" }
                    } else {
                        "No Intake available"
                    }
                    labelIntakes.visibility = View.VISIBLE
                    tvIntake.visibility = View.VISIBLE

                }

                if (isLanguageProgram) {
                    binding.tvHours.setText(record.program.additional_items?.weekly_hours)
                    labelWeeklyHours.visibility = View.VISIBLE
                    tvHours.visibility = View.VISIBLE
                } else {
                    labelWeeklyHours.visibility = View.GONE
                    tvHours.visibility = View.GONE

                }


                if (isLanguageProgram) {
                    binding.tvEnglishLevel.setText(record.program.additional_items?.english_level)
                    binding.labelEnglishLevel.visibility = View.VISIBLE
                    binding.tvEnglishLevel.visibility = View.VISIBLE
                } else {
                    binding.labelEnglishLevel.visibility = View.GONE
                    binding.tvEnglishLevel.visibility = View.GONE

                }


                if (App.sharedPre?.getString(AppConstants.CATEGORY, "") == "higher_education") {
                    binding!!.startDate.visibility = View.GONE
                    binding!!.startDateText.visibility = View.GONE
                    binding!!.age.visibility = View.GONE
                    binding!!.ageText.visibility = View.GONE
                } else if (App.sharedPre?.getString(
                        AppConstants.CATEGORY,
                        ""
                    ) == "language_program"
                ) {

                    binding!!.startDate.visibility = View.GONE
                    binding!!.startDateText.visibility = View.GONE
                    binding!!.age.visibility = View.VISIBLE
                    binding!!.ageText.visibility = View.VISIBLE

                } else if (App.sharedPre?.getString(AppConstants.CATEGORY, "") == "summer_school") {

                    binding!!.startDate.visibility = View.VISIBLE
                    binding!!.startDateText.visibility = View.VISIBLE
                    binding!!.age.visibility = View.VISIBLE
                    binding.labelEnglishLevel.visibility = View.GONE
                    binding!!.ageText.visibility = View.VISIBLE
                }


                val testScoreStringBuilder = StringBuilder()

                if (isHigherEducation) {
                    if (record.program.testscores.isNotEmpty()) {
                        record.program.testscores.forEachIndexed { index, testScore ->
                            if (index > 0) {
                                testScoreStringBuilder.append(", ")
                            }

                            val scoreName = testScore.score_name ?: ""
                            val score = testScore.program_testscore.score ?: ""
                            val minScore =
                                testScore.program_testscore.min_score?.takeIf { it.isNotEmpty() }
                                    ?: ""

                            testScoreStringBuilder.append("$scoreName-$score($minScore)")

                        }

                    } else {
                        testScoreStringBuilder.append("No test scores available")

                    }
                    tvScore.text = testScoreStringBuilder.toString()

                    binding.labelTestScore.visibility = View.VISIBLE
                    binding.tvScore.visibility = View.VISIBLE
                } else {
                    binding.labelTestScore.visibility = View.GONE
                    binding.tvScore.visibility = View.GONE
                }
            }

            val itemCount = position + 1
            binding.courseCount.text = "Course: $itemCount"


        }

    }

}