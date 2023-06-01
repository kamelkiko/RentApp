package com.kiko.rentapp.Dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.kiko.rentapp.R

sealed class InitDialogs {

    class MainDialogs(val deleteDialog: Dialog) {
        var yes: MaterialButton
        var no: MaterialButton

        init {
            init()
            yes = deleteDialog.findViewById(R.id.yes)
            no = deleteDialog.findViewById(R.id.no)
        }

        private fun init() {
            deleteDialog.setContentView(R.layout.delete)
            deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    class PdfDialog(val pdfDialog: Dialog) {
        var notes: EditText
        var water: EditText
        var createPdf: MaterialButton

        init {
            init()
            notes = pdfDialog.findViewById(R.id.notes)
            water = pdfDialog.findViewById(R.id.water)
            createPdf = pdfDialog.findViewById(R.id.create_pdf)
        }

        private fun init() {
            pdfDialog.setContentView(R.layout.pdf_detalis)
            pdfDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


}