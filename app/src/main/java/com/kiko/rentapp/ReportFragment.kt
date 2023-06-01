package com.kiko.rentapp

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kiko.rentapp.Dialogs.InitDialogs
import com.kiko.rentapp.adapter.ReportAdapter
import com.kiko.rentapp.databinding.FragmentReportBinding
import com.kiko.rentapp.pdf.UserPdf
import com.kiko.rentapp.viewmodel.MainViewModel

class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var initDialogs: InitDialogs.MainDialogs
    private lateinit var pdf: InitDialogs.PdfDialog
    private val adapter by lazy { ReportAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(this@ReportFragment)[MainViewModel::class.java]
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val view = binding.root
        onClicks()
        getData()
        initDialogs = InitDialogs.MainDialogs(Dialog(requireContext()))
        pdf = InitDialogs.PdfDialog(Dialog(requireContext()))
        setRecyclerViewItemTouchListener()
        return view
    }

    private fun getData() {
        mainViewModel.readAllReport.observe(viewLifecycleOwner) {
            adapter.updateList(it as java.util.ArrayList<UserPdf>)
            binding.rec.adapter = adapter
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
    }

    private fun searchDataBase(query: String) {
        val searchQuery = "%$query%"
        mainViewModel.searchDataBaseReport(searchQuery).observe(this) {
            it.let {
                adapter.updateList(it as ArrayList<UserPdf>)
            }
        }
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
                    adapter.list?.removeAt(position)?.let { it1 -> mainViewModel.deleteReport(it1) }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}