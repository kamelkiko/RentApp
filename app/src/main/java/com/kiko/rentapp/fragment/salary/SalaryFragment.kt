package com.kiko.rentapp.fragment.salary

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kiko.rentapp.*
import com.kiko.rentapp.adapter.RecAdapter
import com.kiko.rentapp.databinding.FragmentSalaryBinding
import com.kiko.rentapp.model.User
import com.kiko.rentapp.model.UserNotPaid
import com.kiko.rentapp.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class SalaryFragment : Fragment() {
    private var _binding: FragmentSalaryBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { RecAdapter() }
    private var date: Date? = null
    private val current = LocalDate.now()
    private lateinit var fromDate: LocalDate
    private lateinit var toDate: LocalDate
    private lateinit var mainViewModel: MainViewModel
    private var numberOfMonth: Int? = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel = ViewModelProvider(this@SalaryFragment)[MainViewModel::class.java]
        _binding = FragmentSalaryBinding.inflate(inflater, container, false)
        val view = binding.root
        onClicks()
        getData()
        binding.rec.adapter = adapter
        binding.rec.layoutManager = LinearLayoutManager(requireContext())
        adapter.setOnClick(object : RecAdapter.OnClick {
            override fun onClick(position: Int) {
                pickTime().addOnPositiveButtonClickListener {
                    date = fromLongDateToString(it)
                    fromDate = date?.let { it1 -> convertToLocalDateViaInstant(it1) }!!
                    toDate = fromDate
                    showDialog(position)
                }

            }

        })
        return view
    }

    private fun convertToLocalDateViaInstant(dateToConvert: Date): LocalDate? {
        return dateToConvert.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    private fun save(numberOfMonth: Int, position: Int) {
        val list = adapter.list?.get(position)
        val diffYear = getDiffYears(convertToCustomFormat(list?.date.toString()))
        val netRent = list?.raise?.let {
            list.rentalValue?.let { it1 ->
                getNetRent(
                    diffYear, it,
                    it1
                )
            }
        }
        val newFrom = fromDate.withDayOfMonth(1)
        val endOfMonth = toDate.lengthOfMonth()
        val newTo = toDate.withDayOfMonth(endOfMonth)
        mainViewModel.saveUserNotPaid(
            UserNotPaid(
                0,
                adapter.list?.get(position)?.name,
                adapter.list?.get(position)?.owner,
                adapter.list?.get(position)?.nameRent,
                adapter.list?.get(position)?.address,
                netRent,
                newFrom,
                adapter.list?.get(position)?.date,
                newTo,
                list?.endDate,
                current,
                adapter.list?.get(position)?.raise,
                numberOfMonth,
                numberOfMonth
            )
        )
    }

    private fun fromLongDateToString(l: Long): Date? {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val dateString = formatter.format(l)
        return formatter.parse(dateString)!!
    }

    private fun getData() {
        mainViewModel.readAllDataUser.observe(viewLifecycleOwner) {
            adapter.updateList(it as ArrayList<User>)
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
        binding.toNotPaid.setOnClickListener {
            findNavController().navigate(R.id.fromSalaryToNotPaid)
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

    private fun showDialog(position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("اكتب كام شهر عليه")

        val input = EditText(requireContext())

        input.hint = "Enter Text"
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            numberOfMonth = input.text.toString().toInt()
            numberOfMonth?.let { it1 ->
                save(it1, position)
                showToast("تمت الاضافة الي الايجار المكسور")
            }
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}