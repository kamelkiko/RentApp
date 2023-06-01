package com.kiko.rentapp.adapter


import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kiko.rentapp.R
import com.kiko.rentapp.model.User
import com.kiko.rentapp.databinding.ItemBinding
import com.kiko.rentapp.fragment.userManage.UserManagementFragmentDirections
import com.kiko.rentapp.getDiffYears
import com.kiko.rentapp.getNetRent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecAdapter : RecyclerView.Adapter<RecAdapter.Holder>() {
    var list: ArrayList<User>? = null
    private lateinit var onClick: OnClick

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<User>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnClick(onClick: OnClick) {
        this.onClick = onClick
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
            val diffYear = getDiffYears(convertToCustomFormat(currentItem?.date.toString()))
            val netRent = currentItem?.raise?.let {
                currentItem.rentalValue?.let { it1 ->
                    getNetRent(
                        diffYear, it,
                        it1
                    )
                }
            }
            itemMoney.text = netRent.toString()
            click.setOnClickListener {
                when (holder.itemView.findNavController().currentDestination?.id) {
                    holder.itemView.findNavController()
                        .findDestination(R.id.userManagementFragment)?.id -> {
                        val action =
                            UserManagementFragmentDirections.toDetailsFromUser(
                                currentItem!!,
                                position
                            )
                        holder.itemView.findNavController().navigate(action)

                    }
                    holder.itemView.findNavController()
                        .findDestination(R.id.salaryFragment)?.id -> {
                        onClick.onClick(position)
                    }
                    holder.itemView.findNavController()
                        .findDestination(R.id.pdfFragment)?.id -> {
                        onClick.onClick(position)
                    }
                }

            }
        }
    }

    private fun convertToCustomFormat(dateStr: String?): String {
        val utc = TimeZone.getTimeZone("UTC")
        val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
        val destFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        sourceFormat.timeZone = utc
        val convertedDate = dateStr?.let { sourceFormat.parse(it) }
        return destFormat.format(convertedDate!!)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClick {
        fun onClick(position: Int)
    }
}


