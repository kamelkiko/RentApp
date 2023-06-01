package com.kiko.rentapp.fragment.userManage

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kiko.rentapp.adapter.RecAdapter
import com.kiko.rentapp.Dialogs.InitDialogs
import com.kiko.rentapp.R
import com.kiko.rentapp.viewmodel.MainViewModel
import com.kiko.rentapp.model.User
import com.kiko.rentapp.databinding.FragmentUserManagementBinding
import com.kiko.rentapp.showToast

class UserManagementFragment : Fragment() {
    private var _binding: FragmentUserManagementBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { RecAdapter() }
    private lateinit var initDialogs: InitDialogs.MainDialogs
    private lateinit var mainViewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(this@UserManagementFragment)[MainViewModel::class.java]
        _binding = FragmentUserManagementBinding.inflate(inflater, container, false)
        val view = binding.root
        onClicks()
        getData()
        initDialogs = InitDialogs.MainDialogs(Dialog(requireContext()))
        setRecyclerViewItemTouchListener()
        binding.rec.adapter = adapter
        binding.rec.layoutManager = LinearLayoutManager(requireContext())
        return view
    }

    private fun onClicks() {
        binding.addUser.setOnClickListener {
            findNavController().navigate(R.id.toAddUser)
        }
        binding.deleteAll.setOnClickListener {
            deleteAllUsers()
        }
        binding.search.setOnSearchClickListener {
            binding.apply {
                deleteAll.visibility = View.GONE
                addUser.visibility = View.GONE
            }
        }

        binding.search.setOnCloseListener(object : SearchView.OnCloseListener,
            androidx.appcompat.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                binding.apply {
                    deleteAll.visibility = View.VISIBLE
                    addUser.visibility = View.VISIBLE
                }
                return false
            }

        })
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

    private fun getData() {
        mainViewModel.readAllDataUser.observe(viewLifecycleOwner) {
            adapter.updateList(it as ArrayList<User>)
        }
    }

    private fun deleteAllUsers() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("نعم") { _, _ ->
            mainViewModel.deleteAllUsers()
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
        mainViewModel.searchDataBaseUser(searchQuery).observe(this) {
            it.let {
                adapter.updateList(it as ArrayList<User>)
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
                    adapter.list?.removeAt(position)?.let { it1 -> mainViewModel.deleteUser(it1) }
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