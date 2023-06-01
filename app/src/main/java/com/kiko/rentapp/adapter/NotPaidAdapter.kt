package com.kiko.rentapp.adapter


import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kiko.rentapp.databinding.ItemBinding
import com.kiko.rentapp.model.UserNotPaid
import kotlin.collections.ArrayList

class NotPaidAdapter : RecyclerView.Adapter<NotPaidAdapter.Holder>() {
    var list: ArrayList<UserNotPaid>? = null
    private lateinit var onClick: OnClick

    fun setOnClick(onClick: OnClick) {
        this.onClick = onClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: ArrayList<UserNotPaid>) {
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
            itemMoney.text = currentItem?.newNumber.toString()
            click.setOnClickListener {
                onClick.onClick(position)
            }

        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class Holder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnClick {
        fun onClick(position: Int)
    }

}


