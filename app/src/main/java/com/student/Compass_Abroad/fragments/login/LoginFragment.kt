package com.student.Compass_Abroad.fragments.login

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.student.Compass_Abroad.R

import com.student.Compass_Abroad.databinding.FragmentLoginBinding

import kotlin.random.Random

class LoginFragment : Fragment() {
    var binding: FragmentLoginBinding? = null
    private var num_password = 0
    var contentKey=""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        onClicks()

        return binding!!.getRoot()
    }


    private fun onClicks() {
        binding!!.ibShowPasscode.setOnClickListener {
            val cursorPosition = binding!!.etPassword.selectionEnd
            if (num_password % 2 == 0) {
                binding!!.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding!!.ibShowPasscode.setImageResource(R.drawable.ic_show_password)
            } else {
                binding!!.etPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding!!.ibShowPasscode.setImageResource(R.drawable.ic_hide_password)
            }
            num_password++
            binding!!.etPassword.setSelection(cursorPosition)
        }
        binding!!.ibHidePasscode.setOnClickListener {
            binding!!.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding!!.etPassword.setSelection(binding!!.etPassword.getText().length)
            binding!!.ibHidePasscode.setVisibility(View.GONE)
            binding!!.ibShowPasscode.setVisibility(View.VISIBLE)
        }
        binding!!.tvSp2Save.setOnClickListener {
            //loginUser()
        }
    }



    fun generateRandomHexString(length: Int): String {
        val hexChars = "0123456789abcdef"
        return (1..length)
            .map { hexChars[Random.nextInt(hexChars.length)] }
            .joinToString("")
    }

}