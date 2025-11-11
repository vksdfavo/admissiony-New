package com.student.Compass_Abroad.onBoardingScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.fragments.BaseFragment


class Page2 : BaseFragment() {
    var relativeLayout: RelativeLayout? = null
    var ivVector: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_ob_page2, container, false)

        //find id
        relativeLayout = view.findViewById(R.id.rl1)
        ivVector = view.findViewById(R.id.ivObP2_vector)


        YoYo.with(Techniques.BounceInUp)
            .duration(700)
            .repeat(0)
            .playOn(view.findViewById<View>(R.id.ivObP2_vector));

        view.findViewById<View>(R.id.tvSkip2).setOnClickListener {

            (requireActivity() as OnBoardingActivity).getViewPager2().currentItem = 3

        }

        return view
    }
}
