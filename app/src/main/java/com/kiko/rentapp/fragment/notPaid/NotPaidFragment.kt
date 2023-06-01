package com.kiko.rentapp.fragment.notPaid

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kiko.rentapp.*
import com.kiko.rentapp.Dialogs.InitDialogs
import com.kiko.rentapp.adapter.NotPaidAdapter
import com.kiko.rentapp.databinding.FragmentNotPaidBinding
import com.kiko.rentapp.model.UserNotPaid
import com.kiko.rentapp.pdf.PDFConverter
import com.kiko.rentapp.pdf.UserPdf
import com.kiko.rentapp.viewmodel.MainViewModel
import java.time.LocalDate
import kotlin.collections.ArrayList

class NotPaidFragment : Fragment() {
    private var _binding: FragmentNotPaidBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    private val current = LocalDate.now()
    private val adapter by lazy { NotPaidAdapter() }
    private lateinit var pdf: InitDialogs.PdfDialog
    private lateinit var mainViewModel: MainViewModel
    private lateinit var initDialogs: InitDialogs.MainDialogs
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotPaidBinding.inflate(inflater, container, false)
        pdf = InitDialogs.PdfDialog(Dialog(requireContext()))
        mainViewModel = ViewModelProvider(this@NotPaidFragment)[MainViewModel::class.java]
        val view = binding.root
        onClicks()
        showData()
        setRecyclerViewItemTouchListener()
        initDialogs = InitDialogs.MainDialogs(Dialog(requireContext()))
        binding.rec.adapter = adapter
        binding.rec.layoutManager = LinearLayoutManager(requireContext())
        adapter.setOnClick(object : NotPaidAdapter.OnClick {
            override fun onClick(position: Int) {
                val list = adapter.list?.get(position)
                if (list?.newNumber == list?.number) {
                    createPdf(position, list?.fromDate!!, list.toDate!!)

                } else if (list?.newNumber!! < list.number!!) {
                    val diff: Long = (list.number!! - list.newNumber!!).toLong()
                    val newFrom = list.fromDate?.plusMonths(diff)
                    val newTo = newFrom?.withDayOfMonth(newFrom.lengthOfMonth())
                    createPdf(position, newFrom!!, newTo!!)
                }


            }

        })
        return view
    }

    private fun createPdf(position: Int, fromDate: LocalDate, toDate: LocalDate) {
        pdf.pdfDialog.show()
        pdf.createPdf.setOnClickListener {
            check(position, fromDate, toDate)
        }
    }

    private fun check(position: Int, fromDate: LocalDate, toDate: LocalDate) {
        if (checkPdf(
                pdf.notes,
                pdf.water,
                requireContext()
            )
        ) {
            pdfUser(position, fromDate, toDate)
        }
    }

    private fun pdfUser(position: Int, fromDate: LocalDate, toDate: LocalDate) {
        val pdfConverter = PDFConverter()
        val list = adapter.list?.get(position)
        pdfConverter.createPdf(
            requireContext(), UserPdf(
                0,
                list?.name,
                list?.owner,
                list?.nameRent, list?.address, list?.netRent,
                fromDate, list?.date, toDate, list?.endDate,
                pdf.notes.text.toString(), list?.raise, pdf.water.text.toString().toFloat(), current
            ), requireActivity()
        )
        Toast.makeText(context, "تم عمل فاتورة", Toast.LENGTH_SHORT).show()
        mainViewModel.saveReport(
            UserPdf(
                0,
                list?.name,
                list?.owner,
                list?.nameRent,
                list?.address,
                list?.netRent,
                fromDate,
                adapter.list?.get(position)?.date,
                toDate,
                list?.endDate,
                pdf.notes.text.toString(),
                adapter.list?.get(position)?.raise,
                pdf.water.text.toString().toFloat(),
                current
            )
        )
        pdf.pdfDialog.dismiss()
        val newNumber = list?.newNumber?.minus(1)
        mainViewModel.updateUserNotPaid(
            UserNotPaid(
                list?.id!!,
                list.name,
                list.owner,
                list.nameRent,
                list.address,
                list.netRent,
                list.fromDate,
                list.date,
                list.toDate,
                list.endDate,
                list.currentDate,
                list.raise,
                list.number,
                newNumber
            )
        )
        if (list.newNumber == 1) {
            adapter.list?.removeAt(position)
                ?.let { it1 -> mainViewModel.deleteUserNotPaid(it1) }
            binding.rec.adapter!!.notifyItemRemoved(position)
        }
    }

    private fun showData() {
        mainViewModel.readAllDataUserNotPaid.observe(viewLifecycleOwner) {
            adapter.updateList(it as ArrayList<UserNotPaid>)
        }
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
        binding.removeAll.setOnClickListener {
            deleteAll()
        }
    }

    private fun deleteAll() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("نعم") { _, _ ->
            mainViewModel.deleteAllUsersNotPaid()
            binding.rec.adapter?.notifyDataSetChanged()
            showToast("تم مسح جميع البيانات بنجاح")
        }
        builder.setNegativeButton("لا") { _, _ ->
        }
        builder.setTitle("مسح جميع البيانات")
        builder.setMessage("هل انت متأكد من محو جميع البيانات")
        builder.create().show()
    }

    private fun searchDataBase(query: String) {
        val searchQuery = "%$query%"
        mainViewModel.searchDataBaseUserNotPaid(searchQuery).observe(this) {
            it.let {
                adapter.updateList(it as ArrayList<UserNotPaid>)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setRecyclerViewItemTouchListener() {
        val itemTouchCallback = object
            : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                initDialogs.deleteDialog.show()
                initDialogs.yes.setOnClickListener {
                    adapter.list?.removeAt(position)
                        ?.let { it1 -> mainViewModel.deleteUserNotPaid(it1) }
                    binding.rec.adapter!!.notifyItemRemoved(position)
                    initDialogs.deleteDialog.dismiss()
                    showToast("تم الحذف")
                }
                initDialogs.no.setOnClickListener {
                    binding.rec.adapter!!.notifyDataSetChanged()
                    initDialogs.deleteDialog.dismiss()
                }
            }

        }
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.rec)
    }


}