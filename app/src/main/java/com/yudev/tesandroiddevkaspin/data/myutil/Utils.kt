package com.yudev.tesandroiddevkaspin.data.myutil

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

fun View.snack(msg: String, anchorView: View? = null) {
    Snackbar.make(this,msg,Snackbar.LENGTH_SHORT).setAnchorView(anchorView).show()
}

fun checkInput(input: Any): Boolean {
    var check = false
    if (input is TextInputLayout) {
        if (input.editText?.text.toString().trim().isEmpty()) {
            check = false
            input.error = "${input.hint ?: ""} tidak boleh kosong!"
            input.requestFocus()
//            Snackbar.Build(input.rootView,"${input.hint} tidak boleh kosong!",Snackbar.LENGTH_SHORT).show()
        } else {
            check = true
            input.isErrorEnabled = false
        }
    }
    return check
}