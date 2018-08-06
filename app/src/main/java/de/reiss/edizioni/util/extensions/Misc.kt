package de.reiss.edizioni.util.extensions

import android.app.Activity
import android.support.v4.app.Fragment
import de.reiss.edizioni.events.eventBus

fun Activity.registerToEventBus() {
    eventBus.register(this)
}

fun Activity.unregisterFromEventBus() {
    eventBus.unregister(this)
}

fun Fragment.registerToEventBus() {
    eventBus.register(this)
}

fun Fragment.unregisterFromEventBus() {
    eventBus.unregister(this)
}