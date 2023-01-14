package android.com.demo.utils

import android.app.Dialog
import android.com.demo.R
import android.com.demo.databinding.DialogConfirmBinding
import android.com.demo.databinding.DialogLoadingBinding
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import java.util.*


object DialogUtils {

    fun getLoading(context: Context, message: String? = null): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.WrapContentDialog)
        val binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        message?.let {
            binding.loadingMsg.text = it
        }
        builder.setView(binding.root)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // This should be called once in your Fragment's onViewCreated() or in Activity onCreate() method to avoid dialog duplicates.
        return dialog
    }

    fun showAlertDialog(context: Context, title: String? = null, message: String? = null,
                        confirmButton: String? = context.getString(R.string.dialog_confirm_button),
                        callback: ((Int) -> Unit)? = null){
        showConfirmDialog(context, title, message, positiveButton = confirmButton, negativeButton = null,callback)
    }

    /**
     * callback return Dialog.BUTTON_POSITIVE or Dialog.BUTTON_NEGATIVE
     */
    private fun showConfirmDialog(
        context: Context,
        title: String? = null, message: String? = null,
        positiveButton: String? = context.getString(R.string.dialog_positive_button),
        negativeButton: String? = context.getString(R.string.dialog_negative_button),
        callback: ((Int) -> Unit)? = null
    ) {

        lateinit var dialog: Dialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.WrapContentDialog)
        val binding = DialogConfirmBinding.inflate(LayoutInflater.from(context))

        title?.let {
            binding.txtTitle.text = it
            binding.txtTitle.visibility = View.VISIBLE
        }
        message?.let {
            binding.txtMessage.text = it
            binding.txtMessage.visibility = View.VISIBLE
        }
        positiveButton?.let {
            binding.buttonPositive.text = it
            binding.buttonPositive.setSafeOnClickListener {
                dialog.dismiss()
                callback?.invoke(Dialog.BUTTON_POSITIVE)
            }
            binding.buttonPositive.visibility = View.VISIBLE
        }
        negativeButton?.let {
            binding.buttonNegative.text = it
            binding.buttonNegative.setSafeOnClickListener {
                dialog.dismiss()
                callback?.invoke(Dialog.BUTTON_NEGATIVE)
            }
            binding.buttonNegative.visibility = View.VISIBLE
        }

        builder.setView(binding.root)
        dialog = builder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // This should be called once in your Fragment's onViewCreated() or in Activity onCreate() method to avoid dialog duplicates.
        // This should be called once in your Fragment's onViewCreated() or in Activity onCreate() method to avoid dialog duplicates.
        dialog.show()
    }
}