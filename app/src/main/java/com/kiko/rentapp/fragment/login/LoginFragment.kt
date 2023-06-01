package com.kiko.rentapp.fragment.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kiko.rentapp.R
import com.kiko.rentapp.databinding.FragmentLoginBinding
import com.kiko.rentapp.showToast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var show = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        swapFromDayToNight()
        validation()
        onClicks()
        return view

    }

    private fun onClicks() {
        binding.showHide.setOnClickListener {
            if (show) {
                binding.pw.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.showHide.setImageResource(R.drawable.ic_baseline_hide_source_24)
                show = false
            } else if (!show) {
                binding.pw.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showHide.setImageResource(R.drawable.ic_baseline_remove_red_eye_24)
                show = true
            }
        }
    }

    private fun validation() {
        binding.login.setOnClickListener {
            val user = binding.username.text.toString()
            val pw = binding.pw.text.toString()
            if (user == "rent for rent" && pw == "qq17111987") {
                findNavController().navigate(R.id.toHomeFromLogin)
                showToast("♥ نورت يا باشا ♥")
            } else if (user == "") {
                binding.username.error = "لازم تدخل اسم"
                if (pw == "")
                    binding.pw.error = "لازم تدخل الباسورد"
            } else if (pw == "") {
                binding.pw.error = "لازم تدخل الباسورد"
                if (user == "")
                    binding.username.error = "لازم تدخل اسم"
            } else
                showToast("غلط حاول مره اخري")

        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun swapFromDayToNight() {
        val df: DateFormat = SimpleDateFormat("h:mm a")
        val date = df.format(Calendar.getInstance().time).uppercase()

        if (date.contains("PM") || date.contains("م")) {
            binding.background.setImageResource(R.drawable.good_night_img)
            binding.textView.text = " مساء"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}