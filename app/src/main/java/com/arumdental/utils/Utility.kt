package com.arumdental.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.arumdental.R
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


private var progressDialog: Dialog? = null


fun showProgress(context: Context) {
    try {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
            progressDialog!!.cancel()
        }
        progressDialog = Dialog(context)
        progressDialog!!.setContentView(R.layout.progressdialog)
        progressDialog!!.setCancelable(false)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog!!.show()
    } catch (e: Exception) {
        e.printStackTrace()

    }

}

fun hideProgress() {
    try {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
            progressDialog!!.cancel()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}


fun showSnackBar(view: View, message: CharSequence) {

    val snackbar = Snackbar
        .make(view, message, Snackbar.LENGTH_LONG)

    // snackbar.setActionTextColor(Color.BLACK)
    //  snackbar.setActionTextColor(Color.BLUE)
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(Color.parseColor("#344CA2"))
    snackbar.setActionTextColor(Color.parseColor("#ffffff"))
    snackbar.show()
}


fun isValidEmailId(email: String): Boolean {
    return Pattern.compile(
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    ).matcher(email).matches()
}
private val BASE_URL: String = Constants.BASE_URL

fun getDateFromCreatedAT(date: String): String? {
    val inputFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
// SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val outformat = SimpleDateFormat("dd/MM/yyyy")
    var date1: Date? = null
    try {
        date1 = inputFormat.parse(date)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return outformat.format(date1)
}



