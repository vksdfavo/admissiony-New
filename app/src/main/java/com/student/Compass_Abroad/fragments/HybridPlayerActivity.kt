package com.student.Compass_Abroad.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.student.Compass_Abroad.R
import com.student.Compass_Abroad.activities.MainActivity
import com.student.Compass_Abroad.databinding.FragmentHybridPlayerActivityBinding

@UnstableApi
class HybridPlayerActivity : BaseFragment() {
    private lateinit var binding: FragmentHybridPlayerActivityBinding

    private var exoPlayer: ExoPlayer? = null
    private var youTubePlayer: YouTubePlayer? = null

    private var isYouTube = false
    private var videoUrl: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHybridPlayerActivityBinding.inflate(inflater, container, false)

        binding.fabAcBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        videoUrl = arguments?.getString("media_url") ?: ""

        lifecycle.addObserver(binding.youTubePlayerView)

        Log.d("onCreateViewonCreateView", videoUrl)

        // Initialize based on URL type
        setupPlayer(videoUrl)

        return binding.root
    }

    // ------------------------------------------------------------
    // Setup the correct player
    // ------------------------------------------------------------
    private fun setupPlayer(url: String) {
        if (url.contains("youtube.com") || url.contains("youtu.be")) {

            isYouTube = true
            binding.exoPlayerView.visibility = View.GONE
            binding.youTubePlayerView.visibility = View.VISIBLE

            binding.youTubePlayerView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {

                override fun onReady(player: YouTubePlayer) {
                    youTubePlayer = player
                    val videoId = extractYouTubeId(url)
                    player.cueVideo(videoId, 0f)
                }
            })

        } else {
            isYouTube = false
            binding.youTubePlayerView.visibility = View.GONE
            binding.exoPlayerView.visibility = View.VISIBLE

            val dataSourceFactory = DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)
                .setDefaultRequestProperties(
                    mapOf("User-Agent" to "HybridPlayer/1.0")
                )

            exoPlayer = ExoPlayer.Builder(requireContext())
                .setMediaSourceFactory(DefaultMediaSourceFactory(dataSourceFactory))
                .build()

            binding.exoPlayerView.player = exoPlayer

            val mediaItem = MediaItem.fromUri(url)
            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()
        }
    }

    // ------------------------------------------------------------
    // Video control helpers
    // ------------------------------------------------------------
    private fun pauseVideo() {
        if (isYouTube) youTubePlayer?.pause()
        else exoPlayer?.playWhenReady = false
    }

    // ------------------------------------------------------------
    // Extract YouTube video ID
    // ------------------------------------------------------------
    private fun extractYouTubeId(url: String): String {
        return when {
            url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
            url.contains("youtu.be/") -> url.substringAfter("youtu.be/")
            else -> ""
        }
    }

    // ------------------------------------------------------------
    // Lifecycles
    // ------------------------------------------------------------
    override fun onStop() {
        super.onStop()
        pauseVideo()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (isYouTube) {
            binding.youTubePlayerView.release()
        } else {
            exoPlayer?.release()
            exoPlayer = null
        }
    }

    override fun onResume() {
        super.onResume()

        MainActivity.bottomNav?.visibility = View.GONE

        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}
