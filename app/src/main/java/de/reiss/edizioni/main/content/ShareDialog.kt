package de.reiss.edizioni.main.content

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import de.reiss.edizioni.R
import de.reiss.edizioni.dailyTextToString
import de.reiss.edizioni.model.DailyText

class ShareDialog : DialogFragment() {

    companion object {

        private const val KEY_INITIAL_CONTENT = "KEY_INITIAL_CONTENT"

        fun createInstance(context: Context,
                           dailyText: DailyText) = ShareDialog().apply {
            arguments = Bundle().apply {
                putString(KEY_INITIAL_CONTENT, dailyTextToString(context, dailyText))
            }
        }

    }

    private lateinit var input: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog =
            activity.let { activity ->
                if (activity == null) {
                    throw NullPointerException()
                }
                AlertDialog.Builder(activity)
                        .setTitle(R.string.share_dialog_title)
                        .setNegativeButton(R.string.dialog_cancel, null)
                        .setPositiveButton(R.string.share_dialog_ok) { _, _ ->
                            startActivity(shareIntent())
                            dismiss()
                        }
                        .setView(createLayout(activity))
                        .create()
            }

    @SuppressLint("InflateParams")
    private fun createLayout(activity: Activity): View? {
        return activity.layoutInflater.inflate(R.layout.share_dialog, null).apply {
            input = findViewById<EditText>(R.id.share_dialog_input).apply {
                setText(createText(activity))
            }
        }
    }

    private fun createText(context: Context): String {
        val initialContent = arguments?.getString(KEY_INITIAL_CONTENT) ?: ""
        if (initialContent.isEmpty()) {
            return ""
        }
        return context.getString(R.string.share_content,
                initialContent,
                context.getString(R.string.copyright))
    }

    private fun shareIntent() = Intent.createChooser(Intent()
            .setAction(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, input.text.toString())
            .setType("text/plain"), resources.getText(R.string.share_dialog_chooser_title))

}