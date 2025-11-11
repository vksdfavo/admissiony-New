package com.student.Compass_Abroad.fragments.login

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.Navigation
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.Utils.App
import com.student.Compass_Abroad.databinding.FragmentLoginSignUpBinding
import androidx.navigation.findNavController


class LoginSignUpFragment : Fragment() {
    var binding: FragmentLoginSignUpBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginSignUpBinding.inflate(inflater, container, false)
        onClick()
        onBackPressed()
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE //

        ViewCompat.setOnApplyWindowInsetsListener(binding!!.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        return binding!!.getRoot()


    }

    override fun onResume() {
        super.onResume()


        App.singleton?.statusValidation = 0

        requireActivity().window.statusBarColor =
            requireActivity().getColor(R.color.white)

        requireActivity().window.navigationBarColor =
            requireActivity().getColor(R.color.bottom_gradient_one)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = requireActivity().window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            requireActivity().window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    private fun onClick() {
        binding!!.btnLogin.setOnClickListener {

            binding!!.getRoot().findNavController().navigate(R.id.signInEmailFragment)
            App.singleton?.SHOW_PASSCODE_SECTION = false

        }
        binding?.btnSignUp?.setOnClickListener {
            if (isAdded) {
                try {

                    App.singleton?.statusValidation = 1

                    binding!!.root.findNavController()
                        .navigate(R.id.signUpFragment2)

                } catch (e: Exception) {
                    Log.e("SignUpFragment", "Navigation error: ${e.message}", e)
                }
            } else {
                Log.e("SignUpFragment", "Fragment is not attached to an activity.")
            }
        }
    }

    private fun onBackPressed() {
        binding?.getRoot()?.setFocusableInTouchMode(true)
        binding?.getRoot()?.requestFocus()
        binding?.getRoot()?.setOnKeyListener { _, keyCode, _ ->
            if (keyCode === KeyEvent.KEYCODE_BACK) {
                requireActivity().finishAffinity()
                return@setOnKeyListener true
            }
            false
        }
    }


}