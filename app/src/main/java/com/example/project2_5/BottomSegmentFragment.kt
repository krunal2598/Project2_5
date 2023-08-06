package com.example.project2_5

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.project2_5.R
import com.example.project2_5.databinding.FragmentBottomSegmentBinding
import java.text.SimpleDateFormat
import java.util.*

class BottomSegmentFragment : Fragment() {

    private lateinit var binding: FragmentBottomSegmentBinding
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private var timer: Timer? = null
    private var currentSeasonImageIndex = 0
    private var fadeInAnimation: Animation? = null
    private var fadeOutAnimation: Animation? = null
    private var currentStep = 0
    private var mediaPlayer: MediaPlayer? = null

    private val seasonImages = listOf(
        R.drawable.spring,
        R.drawable.summer,
        R.drawable.autumn,
        R.drawable.winter
    )

    private val backgroundColors = listOf(
        Color.parseColor("#FF4500"), // Orange Red
        Color.parseColor("#8FBC8F"), // Dark Sea Green
        Color.parseColor("#FFFF00"), // Yellow
        Color.parseColor("#FFFFFF")  // White
    )

    private val musicFiles = listOf(
        R.raw.spring_song,
        R.raw.summer_song,
        R.raw.autumn_song,
        R.raw.winter_song
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSegmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread {
                    updateDateTime()
                    updateBackgroundColorAndMusic()
                }
            }
        }, 0, 1000) // Update every second

        // Create fade-in and fade-out animations
        fadeInAnimation = AlphaAnimation(0f, 1f).apply {
            duration = 1000 // Adjust the duration as needed
        }
        fadeOutAnimation = AlphaAnimation(1f, 0f).apply {
            duration = 1000 // Adjust the duration as needed
        }

        // Apply rotation animation to the wheelImageView
        val rotateAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_animation)
        binding.wheelImageView.startAnimation(rotateAnimation)

        updateSeasonImage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        timer = null
    }

    private fun updateSeasonImage() {
        if (currentSeasonImageIndex >= seasonImages.size) {
            currentSeasonImageIndex = 0
        }

        // Fade-out the old image
        binding.bottomSeasonImageView.startAnimation(fadeOutAnimation)

        // Change the image resource
        binding.bottomSeasonImageView.setImageResource(seasonImages[currentSeasonImageIndex])

        // Fade-in the new image
        binding.bottomSeasonImageView.startAnimation(fadeInAnimation)

        currentSeasonImageIndex++

        // Update season image with a delay to make it visually distinguishable
        Handler(Looper.getMainLooper()).postDelayed({
            updateSeasonImage()
        }, 15000) // Change image every 15 seconds to match the top segment
    }

    private fun updateDateTime() {
        val currentDate = Date()
        val formattedDate = dateFormatter.format(currentDate)
        binding.currentDateTime.text = formattedDate
    }

    private fun updateBackgroundColorAndMusic() {
        val backgroundColor = backgroundColors[currentStep % backgroundColors.size]

        // Set the background color of the parent ConstraintLayout
        binding.root.setBackgroundColor(backgroundColor)

        // Change background color of currentDateTime and wheelImageView
        binding.currentDateTime.setBackgroundColor(backgroundColor)
        binding.wheelImageView.setBackgroundColor(backgroundColor)

        // Play the corresponding music file
        val musicFileResId = musicFiles[currentStep % musicFiles.size]
        playMusic(musicFileResId)

        currentStep++
    }

    private fun playMusic(musicFile: Int) {
        // Stop the previous music
        stopMusic()

        // Play the new music
        val mediaPlayer = MediaPlayer.create(requireContext(), musicFile)
        mediaPlayer.start()

        // Release the media player when the music is finished playing
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
    }

    private fun stopMusic() {
        // Stop the previous music if it's playing
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
}