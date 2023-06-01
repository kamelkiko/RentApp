package com.kiko.rentapp.adapter


import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kiko.rentapp.ReportFragmentDirections
import com.kiko.rentapp.databinding.ItemBinding
import com.kiko.rentapp.pdf.UserPdf
import kotlin.collections.ArrayList

class ReportAdapter : RecyclerView.Adapter<ReportAdapter.Holder>() {
    var list: ArrayList<UserPdf>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<UserPdf>) {
        this.list = list
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = list?.get(position)
        holder.binding.apply {
            itemId.text = currentItem?.id.toString()
            itemName.text = currentItem?.name
            itemMoney.text = currentItem?.fromDate.toString()
            click.setOnClickListener {
                val action =
                    ReportFragmentDirections.toDetailsFromReport(
                        currentItem!!,
                        position
                    )
                holder.itemView.findNavController().navigate(action)
            }

        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)
}


