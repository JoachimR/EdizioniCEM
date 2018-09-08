package de.reiss.edizioni.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.Html
import de.reiss.edizioni.App
import de.reiss.edizioni.R

fun appVersion(context: Context): String {
    val version = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    return context.getString(R.string.app_version, version)
}

@Suppress("DEPRECATION")
fun htmlize(text: String) =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(text)
        }

fun copyToClipboard(context: Context, text: String) {
    clipboardManager.primaryClip =
            ClipData.newPlainText(context.getString(R.string.app_name), text)
}

private val clipboardManager: ClipboardManager by lazy {
    App.component.clipboardManager
}
