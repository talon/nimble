@file:Suppress("unused")

package me.theghostin.nimble.utils

import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import org.w3c.dom.get

fun Event.asInput(): HTMLInputElement =
        (target as HTMLInputElement)

fun Event.asSelect(): HTMLSelectElement =
        (target as HTMLSelectElement)

var Event.value: String
    get() = asInput().value
    set(v) {
        asInput().value = v
    }

val Event.checked
    get(): Boolean =
        asInput().checked

fun Event.setSelectionRange(start: Int, end: Int): Unit = asInput().setSelectionRange(start, end)

val Event.selected
    get(): String {
        val el = asSelect()
        return (el.options[el.selectedIndex] as HTMLOptionElement).value
    }
