package com.kiko.rentapp.fragment.pdf

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiko.rentapp.*
import com.kiko.rentapp.Dialogs.InitDialogs
import com.kiko.rentapp.adapter.RecAdapter
import com.kiko.rentapp.databinding.FragmentPdfBinding
import com.kiko.rentapp.model.User
import com.kiko.rentapp.pdf.PDFConverter
import com.kiko.rentapp.pdf.UserPdf
import com.kiko.rentapp.viewmodel.MainViewModel
import java.time.LocalDate
import java.time.Period
import java.util.*
import kotlin.collections.ArrayList


@RequiresApi(Build.VERSION_CODES.O)
class PdfFragment : Fragment() {
    private var _binding: FragmentPdfBinding? = null
    private val binding get() = _binding!!
    private val current = LocalDate.now()
    private val fromDate = LocalDate.now()
    private val toDate = LocalDate.now()
    private lateinit var pdf: InitDialogs.PdfDialog
    private val adapter by lazy { RecAdapter() }
    private lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        pdf = InitDialogs.PdfDialog(Dialog(requireContext()))
        mainViewModel = ViewModelProvider(this@PdfFragment)[MainViewModel::class.java]
        _binding = FragmentPdfBinding.inflate(inflater, container, false)
        val view = binding.root
        onClicks()
        getData()
        binding.rec.adapter = adapter
        binding.rec.layoutManager = LinearLayoutManager(requireContext())
        adapter.setOnClick(object : RecAdapter.OnClick {
            override fun onClick(position: Int) {
                createPdf(position)
            }

        })
        return view
    }

    private fun createPdf(position: Int) {
        pdf.pdfDialog.show()
        pdf.createPdf.setOnClickListener {
            checkInputsPdf(position)
        }
    }

    private fun getData() {
        mainViewModel.readAllDataUser.observe(viewLifecycleOwner) {
            adapter.updateList(it as ArrayList<User>)
        }
    }

    private fun checkInputsPdf(position: Int) {
        if (checkPdf(
                pdf.notes,
                pdf.water,
                requireContext()
            )
        ) {
            pdfUser(position)
        }
    }

    private fun pdfUser(position: Int) {
        val pdfConverter = PDFConverter()
        val list = adapter.list?.get(position)
        val newFrom = fromDate.withDayOfMonth(1)
        val endOfMonth = toDate.lengthOfMonth()
        val newTo = toDate.withDayOfMonth(endOfMonth)
        val diffYear = getDiffYears(convertToCustomFormat(list?.date.toString()))
        val netRent = list?.raise?.let {
            list.rentalValue?.let { it1 ->
                getNetRent(
                    diffYear, it,
                    it1
                )
            }
        }
        pdfConverter.createPdf(
            requireContext(), UserPdf(
                0,
                list?.name,
                list?.owner,
                list?.nameRent, list?.address, netRent,
                newFrom, list?.date, newTo, list?.endDate,
                pdf.notes.text.toString(), list?.raise, pdf.water.text.toString().toFloat(), current
            ), requireActivity()
        )
        Toast.makeText(context, "تم عمل فاتورة", Toast.LENGTH_SHORT).show()
        mainViewModel.saveReport(
            UserPdf(
                0,
                list?.name,
                list?.owner,
                list?.nameRent, list?.address, netRent,
                newFrom,
                adapter.list
                    ?.get(position)?.date,
                newTo, list?.endDate,
                pdf.notes.text.toString(), adapter.list
                    ?.get(position)?.raise, pdf.water.text.toString().toFloat(), current
            )
        )
        pdf.pdfDialog.dismiss()
    }

    private fun onClicks() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null)
                    searchDataBase(p0.toString())
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null)
                    searchDataBase(p0.toString())
                return true
            }

        })
        binding.toReport.setOnClickListener {
            findNavController().navigate(R.id.fromPdfToReport)
        }
    }

    private fun searchDataBase(query: String) {
        val searchQuery = "%$query%"
        mainViewModel.searchDataBaseUser(searchQuery).observe(this) {
            it.let {
                adapter.updateList(it as ArrayList<User>)
            }
        }
    }

}