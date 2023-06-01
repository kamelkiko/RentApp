package com.kiko.rentapp.fragment.addUser

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.kiko.rentapp.*
import com.kiko.rentapp.viewmodel.MainViewModel
import com.kiko.rentapp.model.User
import com.kiko.rentapp.databinding.FragmentAddUserBinding
import java.util.*


class AddUserFragment : Fragment() {
    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private var uriPhotoFront: Uri? = null
    private var uriPhotoBack: Uri? = null
    private var date: Date? = null
    private val rentImageFront =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                uriPhotoFront = it
                Glide.with(requireContext()).load(uriPhotoFront).into(binding.rentImage)
            }
        }
    private val rentImageBack =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                uriPhotoBack = it
                Glide.with(requireContext()).load(uriPhotoBack).into(binding.rentImageBack)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(this@AddUserFragment)[MainViewModel::class.java]
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        val view = binding.root
        onClicks()
        return view
    }

    private fun onClicks() {
        binding.personDate.setOnClickListener {
            pickTime().addOnPositiveButtonClickListener {
                date = fromLongDateToString(it, binding.personDate)
            }
            binding.done.setOnClickListener {
                checkInputsUser()
            }
        }
        binding.rentImage.setOnClickListener {
            selectRentImageFront()
        }
        binding.rentImageBack.setOnClickListener {
            selectRentImageBack()
        }
    }

    private fun selectRentImageFront() {
        rentImageFront.launch("image/*")
    }

    private fun selectRentImageBack() {
        rentImageBack.launch("image/*")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkInputsUser() {
        if (checkInputs(
                binding.personName,
                binding.personNameRent,
                binding.personNameOwner,
                binding.personDate,
                binding.personPeriod,
                binding.personAddress,
                binding.rentalValue,
                binding.personRaise,
                binding.personId,
                binding.insuranceAmount,
                requireContext()
            )
        ) {
            addUser()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addUser() {
        val end = getEndDateFragment(
            binding.personDate.text.toString(),
            binding.personPeriod.text.toString().toLong()
        )
        val user = User(
            0,
            binding.personName.text.toString(),
            binding.personNameRent.text.toString(),
            binding.personNameOwner.text.toString(),
            date,
            binding.personPeriod.text.toString(),
            end,
            binding.personAddress.text.toString(),
            binding.rentalValue.text.toString().toFloat(),
            (binding.personRaise.text.toString().toFloat()),
            binding.personId.text.toString(),
            binding.insuranceAmount.text.toString().toFloat(),
            uriPhotoFront.toString(),
            uriPhotoBack.toString()
        )
        mainViewModel.saveUser(user)
        showToast("تمت الاضافة")
        findNavController().navigate(R.id.toUserFromAdd)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}