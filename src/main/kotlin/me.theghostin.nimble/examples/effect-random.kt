@file:Suppress("unused", "DEPRECATION")

package me.theghostin.nimble.examples

import kotlinx.html.button
import kotlinx.html.h1
import kotlinx.html.js.onClickFunction
import me.theghostin.nimble.app
import me.theghostin.nimble.html
import kotlin.browser.window
import kotlin.js.Math
import kotlin.js.Promise

/**
 *  Model
 */
private data class Counter(val value: Int = 0)

/**
 *  Messages are sent to the app and describe how the state should change.
 *
 *  For our counter we would like to add and subtract from the count.
 */
private sealed class Message

private data class Set(val integer: Int) : Message()
private object AddOne : Message()
private object Sequential : Message()

/**
 * Effects
 */
private fun asyncSet(integer: Int) = Promise { resolve: (Message) -> Unit, _ ->
    window.setTimeout({
        resolve(Set(integer))
    }, 1000)
}

private fun asyncRandom() = asyncSet((Math.random() * 100).toInt())

/**
 * App
 */
private fun example() {
    app<Message, Counter>(Counter()) {

        // The `inbox` is where you handle any messages sent to your app.
        inbox {
            when (it) {
                is Set -> model.copy(value = it.integer)
                AddOne -> model.copy(value = model.value + 1)
                Sequential -> {
                    val updated = model.value + 1
                    send(asyncSet(updated + 3))
                    model.copy(value = updated)
                }
            }
        }

        // after the inbox handles a message it re-renders the html
        html {
            h1 { +"${model.value}" }
            button {
                +"+1"
                onClickFunction = { send(AddOne) }
            }
            button {
                +"+1 then +3"
                onClickFunction = { send(Sequential) }
            }
            button {
                +"random"
                onClickFunction = { send(asyncRandom()) }
            }
        }
    }
}
