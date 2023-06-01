package com.kiko.rentapp.fragment.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kiko.rentapp.R
import com.kiko.rentapp.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var animationImage: Animation
    private lateinit var animationText: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        val view = binding.root
        lifecycleScope.launchWhenStarted {
            delay(1500L)
            animationImage()
            delay(1000L)
            animationText()
            delay(2500L)
            withContext(Dispatchers.Main) {
                findNavController().navigate(R.id.fromSplashToLogin)
            }

        }
        return view
    }

    private fun animationImage() {
        animationImage = AnimationUtils.loadAnimation(requireContext(), R.anim.blink)
        binding.apply {
            logo.visibility = View.VISIBLE
            logo.startAnimation(animationImage)
        }
    }

    private fun animationText() {
        animationText = AnimationUtils.loadAnimation(requireContext(), R.anim.blink2)
        binding.apply {
            welcomeText.visibility = View.VISIBLE
            welcomeText.startAnimation(animationText)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}