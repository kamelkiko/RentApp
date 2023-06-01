package com.kiko.rentapp.fragment.details

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.kiko.rentapp.*
import com.kiko.rentapp.Dialogs.InitDialogs
import com.kiko.rentapp.pdf.PDFConverter
import com.kiko.rentapp.pdf.UserPdf
import com.kiko.rentapp.adapter.RecAdapter
import com.kiko.rentapp.databinding.FragmentDetailsBinding
import com.kiko.rentapp.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var initDialogs: InitDialogs.MainDialogs
    private lateinit var mainViewModel: MainViewModel
    private val adapter by lazy { RecAdapter() }
    private val args by navArgs<DetailsFragmentArgs>()
    private lateinit var pdf: InitDialogs.PdfDialog
    private var netRent: Float? = null
    private val current = LocalDate.now()
    private val fromDate = LocalDate.now()
    private val toDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(this@DetailsFragment)[MainViewModel::class.java]
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        onClicks()
        showData()
        initDialogs = InitDialogs.MainDialogs(Dialog(requireContext()))
        pdf = InitDialogs.PdfDialog(Dialog(requireContext()))
        return view
    }

    private fun onClicks() {
        binding.remove.setOnClickListener {
            initDialogs.deleteDialog.show()
            initDialogs.yes.setOnClickListener {
                mainViewModel.deleteUser(args.currentUser)
                adapter.list?.removeAt(args.position)
                initDialogs.deleteDialog.dismiss()
                showToast("تم الحذف")
                findNavController().navigate(R.id.toUserMFromDetails)
            }
            initDialogs.no.setOnClickListener {
                initDialogs.deleteDialog.dismiss()
            }
        }
        binding.edit.setOnClickListener {
            val action = DetailsFragmentDirections.toUpdateFromDetails(args.currentUser)
            findNavController().navigate(action)
        }
        binding.toPdf.setOnClickListener {
            pdf.pdfDialog.show()
            pdf.createPdf.setOnClickListener {
                checkInputsPdf()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showData() {
        val diffYear = getDiffYears(convertToCustomFormat(args.currentUser.date.toString()))
        netRent = args.currentUser.raise?.let {
            args.currentUser.rentalValue?.let { it1 ->
                getNetRent(
                    diffYear, it,
                    it1
                )
            }
        }
        binding.name.text = args.currentUser.name
        binding.nameRent.text = args.currentUser.nameRent
        binding.nameOwner.text = args.currentUser.owner
        binding.date.text = convertToCustomFormat(args.currentUser.date.toString())
        binding.period.text = args.currentUser.period
        binding.diff.text =
            args.currentUser.endDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .toString()
        binding.address.text = args.currentUser.address
        binding.money.text = args.currentUser.rentalValue.toString()
        binding.raise.text = args.currentUser.raise.toString()
        binding.id.text = args.currentUser.idNumber
        binding.insurance.text = args.currentUser.insuranceAmount.toString()
        binding.netMoney.text = netRent.toString()
        Glide.with(requireContext()).load(args.currentUser.rentPictureFront).into(binding.front)
        Glide.with(requireContext()).load(args.currentUser.rentPictureBack).into(binding.back)
    }

    private fun checkInputsPdf() {
        if (checkPdf(
                pdf.notes,
                pdf.water,
                requireContext()
            )
        ) {
            pdfUser()
        }
    }

    private fun pdfUser() {
        val pdfConverter = PDFConverter()
        val newFrom = fromDate.withDayOfMonth(1)
        val endOfMonth = toDate.lengthOfMonth()
        val newTo = toDate.withDayOfMonth(endOfMonth)
        pdfConverter.createPdf(
            requireContext(), UserPdf(
                0,
                args.currentUser.name,
                args.currentUser.owner,
                args.currentUser.nameRent,
                args.currentUser.address,
                netRent,
                newFrom,
                args.currentUser.date,
                newTo,
                args.currentUser.endDate,
                pdf.notes.text.toString(),
                args.currentUser.raise,
                pdf.water.text.toString().toFloat(),
                current
            ), requireActivity()
        )
        showToast("تم عمل فاتورة")
        mainViewModel.saveReport(
            UserPdf(
                0,
                args.currentUser.name,
                args.currentUser.owner,
                args.currentUser.nameRent,
                args.currentUser.address,
                netRent,
                newFrom,
                args.currentUser.date,
                newTo,
                args.currentUser.endDate,
                pdf.notes.text.toString(),
                args.currentUser.raise,
                pdf.water.text.toString().toFloat(),
                current
            )
        )
        pdf.pdfDialog.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}