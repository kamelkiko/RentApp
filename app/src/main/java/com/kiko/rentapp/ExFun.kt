package com.kiko.rentapp

import android.content.Context
import android.os.Build
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

fun Fragment.showToast(msg: Any) {
    Toast.makeText(requireContext(), msg.toString(), Toast.LENGTH_SHORT).show()
}

fun checkInputs(
    personName: EditText,
    personNameRent: EditText,
    personNameOwner: EditText,
    personDate: TextView,
    personPeriod: EditText,
    personAddress: EditText,
    personRaise: EditText,
    personRentalValue: EditText,
    personInsuranceAmount: EditText,
    personId: EditText,
    context: Context
): Boolean {
    try {
        return if (personName.text.isEmpty() || personNameOwner.text.isEmpty() || personNameRent.text.isEmpty() ||
            personDate.text.toString() == "تاريخ السكن" || personPeriod.text.isEmpty()
            || personAddress.text.isEmpty() || personRentalValue.text.isEmpty()
            || personRaise.text.isEmpty() || personId.text.isEmpty() || personInsuranceAmount.text.isEmpty()
        ) {
            Toast.makeText(context, "لازم تملي كل الخانات", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    } catch (e: Exception) {
        Toast.makeText(context, "لازم تملي كل الخانات", Toast.LENGTH_SHORT).show()
    }
    return true
}

fun checkPdf(
    personRentalValue: EditText,
    personInsuranceAmount: EditText,
    context: Context
): Boolean {
    try {
        return if (personRentalValue.text.isEmpty() || personInsuranceAmount.text.isEmpty()
        ) {
            Toast.makeText(context, "لازم تملي كل الخانات", Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    } catch (e: Exception) {
        Toast.makeText(context, "لازم تملي كل الخانات", Toast.LENGTH_SHORT).show()
    }
    return true
}

fun convertToCustomFormat(dateStr: String?): String {
    val utc = TimeZone.getTimeZone("UTC")
    val sourceFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
    val destFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    sourceFormat.timeZone = utc
    val convertedDate = dateStr?.let { sourceFormat.parse(it) }
    return destFormat.format(convertedDate!!)
}

fun Fragment.pickTime(): MaterialDatePicker<Long> {
    val picker = MaterialDatePicker.Builder.datePicker()
        .setTitleText("Select Date")
        .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
        .build()
    picker.show(requireFragmentManager(), "Date Picker")
    return picker
}

fun fromLongDateToString(l: Long, t: TextView): Date? {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val dateString = formatter.format(l)
    t.text = dateString
    return formatter.parse(dateString)!!
}

@RequiresApi(Build.VERSION_CODES.O)
fun getEndDateFragment(s: String, n: Long): LocalDate {
    val from = LocalDate.parse(s, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US))
    return from.plusYears(n)
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDiffYears(s: String): Int {
    val from = LocalDate.parse(s, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.US))
    val now = LocalDate.now()
    val diff = Period.between(now, from)
    return abs(diff.years)
}

fun getNetRent(year: Int, raise: Float, rent: Float): Float {
    val newRaise = year * (raise / 100.0f)
    var newRent = newRaise * rent
    newRent += rent
    return newRent
}

