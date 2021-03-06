package de.reiss.edizioni.util.extensions

import android.app.Activity
import android.support.annotation.StringRes
import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import de.reiss.edizioni.R


fun Activity.showShortSnackbar(message: String,
                               action: (() -> Unit)? = null,
                               actionLabel: String? = null,
                               callback: (() -> Unit)? = null): Snackbar =
        doShowSnackbar(this, message, Snackbar.LENGTH_SHORT, action, actionLabel, callback)

fun Activity.showLongSnackbar(message: String,
                              action: (() -> Unit)? = null,
                              actionLabel: String? = null,
                              callback: (() -> Unit)? = null): Snackbar =
        doShowSnackbar(this, message, Snackbar.LENGTH_LONG, action, actionLabel, callback)

fun Activity.showIndefiniteSnackbar(message: String,
                                    action: (() -> Unit)? = null,
                                    actionLabel: String? = null,
                                    callback: (() -> Unit)? = null): Snackbar =
        doShowSnackbar(this, message, Snackbar.LENGTH_INDEFINITE, action, actionLabel, callback)

fun Fragment.showShortSnackbar(message: String,
                               action: (() -> Unit)? = null,
                               actionLabel: String? = null,
                               callback: (() -> Unit)? = null): Snackbar? {
    activity?.let {
        return doShowSnackbar(it, message, Snackbar.LENGTH_SHORT, action, actionLabel, callback)
    }
    return null
}

fun Fragment.showLongSnackbar(message: String,
                              action: (() -> Unit)? = null,
                              actionLabel: String? = null,
                              callback: (() -> Unit)? = null): Snackbar? {
    activity?.let {
        return doShowSnackbar(it, message, Snackbar.LENGTH_LONG, action, actionLabel, callback)
    }
    return null
}

fun Activity.showShortSnackbar(@StringRes message: Int,
                               action: (() -> Unit)? = null,
                               actionLabel: String? = null,
                               callback: (() -> Unit)? = null): Snackbar =
        doShowSnackbar(this, getString(message), Snackbar.LENGTH_SHORT, action, actionLabel, callback)

fun Activity.showLongSnackbar(@StringRes message: Int,
                              action: (() -> Unit)? = null,
                              actionLabel: String? = null,
                              callback: (() -> Unit)? = null): Snackbar =
        doShowSnackbar(this, getString(message), Snackbar.LENGTH_LONG, action, actionLabel, callback)

fun Activity.showIndefiniteSnackbar(@StringRes message: Int,
                                    action: (() -> Unit)? = null,
                                    actionLabel: String? = null,
                                    callback: (() -> Unit)? = null): Snackbar =
        doShowSnackbar(this, getString(message), Snackbar.LENGTH_INDEFINITE, action, actionLabel, callback)

fun Fragment.showShortSnackbar(@StringRes message: Int,
                               action: (() -> Unit)? = null,
                               actionLabel: String? = null,
                               callback: (() -> Unit)? = null): Snackbar? = activity?.let {
    doShowSnackbar(it, getString(message), Snackbar.LENGTH_SHORT, action, actionLabel, callback)
}

fun Fragment.showLongSnackbar(@StringRes message: Int,
                              action: (() -> Unit)? = null,
                              actionLabel: String? = null,
                              callback: (() -> Unit)? = null): Snackbar? {
    activity?.let {
        return doShowSnackbar(it, getString(message), Snackbar.LENGTH_LONG, action, actionLabel, callback)
    }
    return null
}

fun Fragment.showIndefiniteSnackbar(@StringRes message: Int,
                                    action: (() -> Unit)? = null,
                                    actionLabel: String? = null,
                                    callback: (() -> Unit)? = null): Snackbar? {
    activity?.let {
        return doShowSnackbar(it, getString(message), Snackbar.LENGTH_INDEFINITE, action, actionLabel, callback)
    }
    return null
}


private fun doShowSnackbar(activity: Activity,
                           message: String,
                           duration: Int,
                           action: (() -> Unit)?,
                           actionLabel: String? = null,
                           callback: (() -> Unit)?): Snackbar {

    return Snackbar.make(activity.findViewById(android.R.id.content),
            message,
            duration).apply {

        if (action != null) {
            if (actionLabel == null) {
                setAction(R.string.snackbar_retry) {
                    action()
                }
            } else {
                setAction(actionLabel) {
                    action()
                }
            }
        }

        if (callback != null) {
            addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    callback()
                }
            })
        }
    }.apply {
        show()
    }
}
