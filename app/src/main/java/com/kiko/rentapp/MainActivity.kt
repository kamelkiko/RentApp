package com.kiko.rentapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        val fromDate = "23/08/2022"
//        val toDate = "23/08/2019"
//        val from = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//        val r = from.plusYears(4)
//        val to = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//        val diff = Period.between(r, to)
//        Log.d(
//            "here", "The difference between " + r.format(DateTimeFormatter.ISO_LOCAL_DATE)
//                    + " and " + to.format(DateTimeFormatter.ISO_LOCAL_DATE) + " is "
//                    + abs(diff.years) + " years, " + abs(diff.months) + " months and "
//                    + abs(diff.days) + " days"
//        )

    }
}