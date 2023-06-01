package com.kiko.rentapp.pdf


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import com.kiko.rentapp.R
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter


class PDFConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createBitmapFromView(
        context: Context,
        view: View,
        user: UserPdf,
        activity: Activity,
    ): Bitmap {
        val name = view.findViewById<TextView>(R.id.pdf_name)
        val owner = view.findViewById<TextView>(R.id.pdf_name_owner)
        val money = view.findViewById<TextView>(R.id.pdf_money)
        val nameRent = view.findViewById<TextView>(R.id.pdf_name_rent)
        val address = view.findViewById<TextView>(R.id.pdf_address)
        val endDate = view.findViewById<TextView>(R.id.pdf_end_date)
        val fromDate = view.findViewById<TextView>(R.id.pdf_from_date)
        val toDate = view.findViewById<TextView>(R.id.pdf_to_date)
        val note = view.findViewById<TextView>(R.id.pdf_notes)
        val water = view.findViewById<TextView>(R.id.pdf_water)
        val currentDate = view.findViewById<TextView>(R.id.pdf_current_date)
        name.text = user.name
        owner.text = user.owner
        nameRent.text = user.nameRent
        money.text = user.rentalValue.toString()
        address.text = user.address
        fromDate.text = user.fromDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()
        toDate.text = user.toDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()
        note.text = user.notes
        water.text = user.water.toString()
        currentDate.text =
            user.currentDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()
        endDate.text = user.endDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()
        return createBitmap(context, view, activity)
    }

    private fun createBitmap(
        context: Context,
        view: View,
        activity: Activity,
    ): Bitmap {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        } else {
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        }
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                1500, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                3100, View.MeasureSpec.EXACTLY
            )
        )
        view.layout(0, 0, 1085, 2345)
        val bitmap = Bitmap.createBitmap(
            1500,
            3100, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return Bitmap.createScaledBitmap(bitmap, 1500, 3100, true)
    }

    private fun convertBitmapToPdf(bitmap: Bitmap, context: Context) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0F, 0F, null)
        pdfDocument.finishPage(page)
        val filePath = File(context.getExternalFilesDir(null), "bitmapPdf.pdf")
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()
        renderPdf(context, filePath)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams", "CutPasteId", "ResourceType")
    fun createPdf(
        context: Context,
        user: UserPdf,
        activity: Activity
    ) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.pdf_layout, null)
        val bitmap = createBitmapFromView(context, view, user, activity)
        convertBitmapToPdf(bitmap, activity)
    }


    private fun renderPdf(context: Context, filePath: File) {
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            filePath
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, "application/pdf")

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage?.toString() ?: "Error", Toast.LENGTH_SHORT)
                .show()
        }
    }
}