package com.example.project2_5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import com.example.project2_5.databinding.FragmentTopSegmentBinding
import android.media.MediaPlayer

class TopSegmentFragment : Fragment() {

    private lateinit var binding: FragmentTopSegmentBinding

    private val musicFiles = listOf(
        R.raw.spring_song,
        R.raw.summer_song,
        R.raw.autumn_song,
        R.raw.winter_song
    )

    private var currentStep = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopSegmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startButton.setOnClickListener {
            currentStep = 0
            startSeasonAnimation()
        }

        binding.stopButton.setOnClickListener {
            resetToInitialState()
        }

        // Start the animation for sun and birds initially
        animateSunAndBirds()
    }

    private fun resetToInitialState() {
        stopMusic()
    }

    private fun startSeasonAnimation() {
        stopMusic()

        if (currentStep >= musicFiles.size) {
            currentStep = 0
        }

        playMusic(musicFiles[currentStep])

        currentStep++
    }

    private var mediaPlayer: MediaPlayer? = null

    private fun playMusic(musicFile: Int) {
        stopMusic()

        mediaPlayer = MediaPlayer.create(requireContext(), musicFile)
        mediaPlayer?.start()

        mediaPlayer?.setOnCompletionListener {
            // Restart the music for loop playback
            mediaPlayer?.start()
        }
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun animateSunAndBirds() {
        // Sun animation: Move from left to right and continue from right to left
        val sunAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1.0f,
            Animation.RELATIVE_TO_PARENT, 1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f
        )
        sunAnimation.repeatMode = Animation.RESTART
        sunAnimation.repeatCount = Animation.INFINITE
        sunAnimation.duration = 8000 // Adjust the duration as needed
        binding.sunImageView.startAnimation(sunAnimation)

        // Birds animation: Move from left to right and continue from right to left
        val birdAnimation1 = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1.0f,
            Animation.RELATIVE_TO_PARENT, 1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f
        )
        birdAnimation1.repeatMode = Animation.RESTART
        birdAnimation1.repeatCount = Animation.INFINITE
        birdAnimation1.duration = 10000 // Adjust the duration as needed
        binding.bird1ImageView.startAnimation(birdAnimation1)

        val birdAnimation2 = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1.0f,
            Animation.RELATIVE_TO_PARENT, 1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f
        )
        birdAnimation2.repeatMode = Animation.RESTART
        birdAnimation2.repeatCount = Animation.INFINITE
        birdAnimation2.duration = 12000 // Adjust the duration as needed
        binding.bird2ImageView.startAnimation(birdAnimation2)
    }
}