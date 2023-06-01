package com.kiko.rentapp.fragment.update

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kiko.rentapp.*
import com.kiko.rentapp.databinding.FragmentUpdateBinding
import com.kiko.rentapp.model.User
import com.kiko.rentapp.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*


class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateFragmentArgs>()
    private var uriPhotoFront: Uri? = null
    private var uriPhotoBack: Uri? = null
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
    private var date: Date? = null
    private lateinit var mainViewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(this@UpdateFragment)[MainViewModel::class.java]
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val view = binding.root
        onClicks()
        showData()
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showData() {
        binding.personName.setText(args.currentUser.name)
        binding.personNameRent.setText(args.currentUser.nameRent)
        binding.personNameOwner.setText(args.currentUser.owner)
        binding.personDate.text = convertToCustomFormat(args.currentUser.date.toString())
        binding.personPeriod.setText(args.currentUser.period)
        binding.personAddress.setText(args.currentUser.address)
        binding.rentalValue.setText(args.currentUser.rentalValue.toString())
        binding.personRaise.setText(args.currentUser.raise.toString())
        binding.personId.setText(args.currentUser.idNumber)
        binding.insuranceAmount.setText(args.currentUser.insuranceAmount.toString())
        Glide.with(requireContext()).load(args.currentUser.rentPictureFront).into(binding.rentImage)
        Glide.with(requireContext()).load(args.currentUser.rentPictureBack)
            .into(binding.rentImageBack)
        date = args.currentUser.date
        uriPhotoFront = Uri.parse(args.currentUser.rentPictureFront)
        uriPhotoBack = Uri.parse(args.currentUser.rentPictureBack)
    }

    private fun convertToCustomFormat(dateStr: String?): String {
        val utc = TimeZone.getTimeZone("UTC")
        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
        val destFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        sourceFormat.timeZone = utc
        val convertedDate = dateStr?.let { sourceFormat.parse(it) }
        return destFormat.format(convertedDate!!)
    }

    private fun onClicks() {
        binding.done.setOnClickListener {
            check()
        }
        binding.rentImage.setOnClickListener {
            selectRentImageFront()
        }
        binding.rentImageBack.setOnClickListener {
            selectRentImageBack()
        }
        binding.personDate.setOnClickListener {
            pickTime().addOnPositiveButtonClickListener {
                date = fromLongDateToString(it, binding.personDate)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun check() {
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
            update()
        }
    }

    private fun selectRentImageFront() {
        rentImageFront.launch("image/*")
    }

    private fun selectRentImageBack() {
        rentImageBack.launch("image/*")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun update() {
        val end = getEndDateFragment(
            binding.personDate.text.toString(),
            binding.personPeriod.text.toString().toLong()
        )
        val user = User(
            args.currentUser.id,
            binding.personName.text.toString(),
            binding.personNameRent.text.toString(),
            binding.personNameOwner.text.toString(),
            date,
            binding.personPeriod.text.toString(),
            end,
            binding.personAddress.text.toString(),
            binding.rentalValue.text.toString().toFloat(),
            binding.personRaise.text.toString().toFloat(),
            binding.personId.text.toString(),
            binding.insuranceAmount.text.toString().toFloat(),
            uriPhotoFront.toString(),
            uriPhotoBack.toString()
        )
        mainViewModel.updateUser(user)
        showToast("تم التعديل")
        findNavController().navigate(R.id.toUserFromUpdate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}