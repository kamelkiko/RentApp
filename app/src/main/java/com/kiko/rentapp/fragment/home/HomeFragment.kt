package com.kiko.rentapp.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kiko.rentapp.R
import com.kiko.rentapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        onClicks()
        return view
    }

    private fun onClicks() {
        binding.users.setOnClickListener {
            findNavController().navigate(R.id.toUserMFromHome)
        }
        binding.pdf.setOnClickListener {
            findNavController().navigate(R.id.fromHomeToPdf)
        }
        binding.netSalary.setOnClickListener {
            findNavController().navigate(R.id.fromHomeToSalary)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}