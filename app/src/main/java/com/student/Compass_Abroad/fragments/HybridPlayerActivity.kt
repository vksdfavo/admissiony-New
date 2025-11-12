package com.student.Compass_Abroad.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.annotation.OptIn
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
class HybridPlayerActivity : Fragment() {  // ✅ renamed for clarity

    private lateinit var binding: FragmentHybridPlayerActivityBinding

    private var exoPlayer: ExoPlayer? = null
    private var youTubePlayer: YouTubePlayer? = null

    private var isYouTube = false
    private var videoUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHybridPlayerActivityBinding.inflate(inflater, container, false)

        // ✅ Example URLs (switch as needed)
        //videoUrl = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3"
         //videoUrl = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"
        videoUrl = "https://www.youtube.com/watch?v=ScMzIvxBSi4"

        // ✅ Setup lifecycle for YouTubeView
        lifecycle.addObserver(binding.youTubePlayerView)

        // ✅ Initialize proper player based on link type
        setupPlayer(videoUrl)



        return binding.root
    }

    // ------------------------------------------------------------
    // Initialize correct player type (YouTube or ExoPlayer)
    // ------------------------------------------------------------
    private fun setupPlayer(url: String) {
        if (url.contains("youtube.com") || url.contains("youtu.be")) {
            // 🔹 YouTube Player mode
            isYouTube = true
            binding.exoPlayerView.visibility = View.GONE
            binding.youTubePlayerView.visibility = View.VISIBLE

            binding.youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(player: YouTubePlayer) {
                    youTubePlayer = player
                    val videoId = extractYouTubeId(url)
                    player.cueVideo(videoId, 0f)
                }
            })
        } else {
            // 🔹 ExoPlayer mode
            isYouTube = false
            binding.youTubePlayerView.visibility = View.GONE
            binding.exoPlayerView.visibility = View.VISIBLE

            val dataSourceFactory = DefaultHttpDataSource.Factory()
                .setAllowCrossProtocolRedirects(true)
                .setDefaultRequestProperties(mapOf("User-Agent" to "HybridPlayer/1.0"))

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
    // Playback controls
    // ------------------------------------------------------------
    private fun playVideo() {
        if (isYouTube) youTubePlayer?.play()
        else exoPlayer?.playWhenReady = true
    }

    private fun pauseVideo() {
        if (isYouTube) youTubePlayer?.pause()
        else exoPlayer?.playWhenReady = false
    }

    private fun stopVideo() {
        if (isYouTube) youTubePlayer?.pause()
        else exoPlayer?.stop()
    }

    // ------------------------------------------------------------
    // Extract YouTube Video ID from URL
    // ------------------------------------------------------------
    private fun extractYouTubeId(url: String): String {
        return when {
            url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
            url.contains("youtu.be/") -> url.substringAfter("youtu.be/")
            else -> ""
        }
    }

    // ------------------------------------------------------------
    // Lifecycle handling
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
        MainActivity.bottomNav?.visibility=View.GONE

        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        window.navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bottom_gradient_one)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            // Below Android 11
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

}
