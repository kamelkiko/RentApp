package com.kiko.rentapp

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kiko.rentapp.Dialogs.InitDialogs
import com.kiko.rentapp.adapter.ReportAdapter
import com.kiko.rentapp.databinding.FragmentDetailsReportBinding
import com.kiko.rentapp.pdf.PDFConverter
import com.kiko.rentapp.pdf.UserPdf
import com.kiko.rentapp.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReportDetailsFragment : Fragment() {
    private var _binding: FragmentDetailsReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var initDialogs: InitDialogs.MainDialogs
    private lateinit var pdf: InitDialogs.PdfDialog
    private lateinit var mainViewModel: MainViewModel
    private val adapter by lazy { ReportAdapter() }
    private var netRent: Float? = null
    private val currentDate = LocalDate.now()
    private val args by navArgs<ReportDetailsFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(this@ReportDetailsFragment)[MainViewModel::class.java]
        _binding = FragmentDetailsReportBinding.inflate(inflater, container, false)
        val view = binding.root
        onClicks()
        showData()
        initDialogs = InitDialogs.MainDialogs(Dialog(requireContext()))
        pdf = InitDialogs.PdfDialog(Dialog(requireContext()))
        return view
    }

    private fun showData() {
        val diffYear = getDiffYears(convertToCustomFormat(args.currentPdf.date.toString()))
        netRent = args.currentPdf.raise?.let {
            args.currentPdf.rentalValue?.let { it1 ->
                getNetRent(
                    diffYear, it,
                    it1
                )
            }
        }
        binding.name.text = args.currentPdf.name
        binding.nameRent.text = args.currentPdf.nameRent
        binding.nameOwner.text = args.currentPdf.owner
        binding.date.text = convertToCustomFormat(args.currentPdf.date.toString())
        binding.diff.text =
            args.currentPdf.endDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .toString()
        binding.address.text = args.currentPdf.address
        binding.money.text = args.currentPdf.rentalValue.toString()
        binding.fromDate.text =
            args.currentPdf.fromDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .toString()
        binding.toDate.text =
            args.currentPdf.toDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .toString()
        binding.currentDate.text =
            args.currentPdf.currentDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .toString()
    }

    private fun onClicks() {
        binding.toPdf.setOnClickListener {
            pdf.pdfDialog.show()
            pdf.createPdf.setOnClickListener {
                if (checkPdf(
                        pdf.notes,
                        pdf.water,
                        requireContext()
                    )
                ) {
                    val pdfConverter = PDFConverter()
                    val list = args.currentPdf
                    pdfConverter.createPdf(
                        requireContext(), UserPdf(
                            0,
                            list.name,
                            list.owner,
                            list.nameRent,
                            list.address,
                            list.rentalValue,
                            list.fromDate,
                            list.date,
                            list.toDate,
                            list.endDate,
                            pdf.notes.text.toString(),
                            list.raise,
                            pdf.water.text.toString().toFloat(),
                            currentDate
                        ), requireActivity()
                    )
                    pdf.pdfDialog.dismiss()
                    Toast.makeText(context, "تم عمل فاتورة", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.remove.setOnClickListener {
            initDialogs.deleteDialog.show()
            initDialogs.yes.setOnClickListener {
                mainViewModel.deleteReport(args.currentPdf)
                adapter.list?.removeAt(args.position)
                initDialogs.deleteDialog.dismiss()
                showToast("تم الحذف")
                findNavController().popBackStack()
            }
            initDialogs.no.setOnClickListener {
                initDialogs.deleteDialog.dismiss()
            }
        }
    }

    private fun check() {

    }

    private fun PdfReport() {
        val pdfConverter = PDFConverter()
        val list = args.currentPdf
        pdfConverter.createPdf(
            requireContext(), UserPdf(
                0,
                list.name,
                list.owner,
                list.nameRent,
                list.address,
                list.rentalValue,
                list.fromDate,
                list.date,
                list.toDate,
                list.endDate,
                pdf.notes.text.toString(),
                pdf.water.text.toString().toFloat(),
                list.raise,
                currentDate
            ), requireActivity()
        )
        pdf.pdfDialog.dismiss()
        Toast.makeText(context, "تم عمل فاتورة", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}