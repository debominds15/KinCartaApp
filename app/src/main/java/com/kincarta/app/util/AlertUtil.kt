package com.kincarta.app.util

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface




object AlertUtil {
    fun createDialog(context: Activity){
        AlertDialog.Builder(context)
            .setTitle("No Internet Connection")
            .setMessage("Please turn on the internet to get the updated data from the network") // Specifying a listener allows you to take an action before dismissing the dialog.
            // The dialog is automatically dismissed when a dialog button is clicked.
            .setPositiveButton(
                R.string.yes,
                DialogInterface.OnClickListener { dialog, which ->
                    // Continue with delete operation
                    dialog.dismiss()
                }) // A null listener allows the button to dismiss the dialog and take no further action.
            .setIcon(R.drawable.ic_dialog_alert)
            .show()
    }
}