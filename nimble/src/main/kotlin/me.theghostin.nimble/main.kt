@file:Suppress("unused")

package me.theghostin.nimble

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.js.Promise

/**
 * A Nimble `app` exposes a small but effective set of APIs that make
 * writing web apps in Kotlin fun and easy!
 *
 * **Some things to know:**
 *
 * - the `inbox` receives `Msg`s and returns new copies of `Model`
 * - the latest result of `inbox` is always available as `model`
 * - use `send` to deliver `Msg`s to the inbox
 * - the `html` is automatically rendered after each `Msg` is processed.
 *
 * **Example:**
 * ```
 * import me.theghostin.nimble.app
 * import me.theghostin.nimble.html
 * import kotlinx.html.button
 * import kotlinx.html.h1
 * import kotlinx.html.js.onClickFunction
 *
 * data class Model(val switch: Boolean = false)
 *
 * sealed class Msg
 *
 * fun main() {
 *     app<Msg, Model>(Model()) {
 *         html {
 *             h1 { +when (model.switch) {
 *                 true -> "On"
 *                 false -> "Off"
 *             } }
 *             button {
 *                 +"flip"
 *                 onClickFunction = { send(model.copy(switch = !model.switch)) }
 *             }
 *         }
 *     }
 * }
 * ```
 */
fun <Msg, Model> app(
        model: Model,
        init: Promise<Msg>? = null,
        block: Nimble<Msg, Model>.() -> Unit
): Nimble<Msg, Model> =
        object : Nimble<Msg, Model>(model, init) {}.apply(block)

/**
 * Nimble is a web application framework for Kotlin
 *
 * @property model the entire state of your application
 * @param init the first `Promise` your application should have
 */
abstract class Nimble<Msg, Model>(
        var model: Model,
        init: Promise<Msg>?
) {
    /**
     * A `frame` represents your `model` over time.
     *
     * TODO: currently only one extension can wrap `frame` at a time (ex: `html`)
     *       is it worth making `frameHandler : `List<Functiono<Unit>>`?
     *       (I'm skeptical but want to consider)
     *
     * **Basic Usage:**
     * ```
     * // ...
     * fun main() {
     *   app<Msg, Model>(Model()) {
     *     inbox { /* ... */ }
     *     frame {
     *       console.log(model)
     *     }
     *   }
     * }
     * ```
     *
     * **Nimble Extension:**
     * ```
     * fun <Msg, Model> Nimble<Msg, Model>.prefixedLogger(prefix: String) =
     *   frame { console.log("$prefix $model) }
     * ```
     */
    @JsName("frame")
    fun frame(handler: () -> Unit) {
        frameHandler = handler
        frameHandler()
    }

    private var frameHandler: () -> Unit = {}

    /**
     * Handle updates to the `model`
     *
     * The latest result of `inbox` is available as `model` inside the `app` scope
     *
     * **Example:**
     * ```
     * import me.theghostin.nimble.app
     * import me.theghostin.nimble.html
     *
     * data class Model(val switch: Boolean = false)
     *
     * sealed class Msg
     * object Flip : Msg()
     *
     * fun main() {
     *     app<Msg, Model>(Model()) {
     *         inbox { when (it) {
     *             Flip -> model.copy(switch = !model.switch)
     *         }}
     *     }
     * }
     * ```
     */
    @JsName("inbox")
    fun inbox(inbox: (Msg) -> Model) {
        inboxHandler = inbox
    }

    private var inboxHandler: (Msg) -> Model = { model }

    /**
     * Send a `Msg` to the `inbox`
     *
     * useful for updating the state of your application
     */
    fun send(msg: Msg) {
        GlobalScope.launch { messages.send(msg) }
    }

    /**
     * Send a `Promise`
     *
     * Nimble handles consuming the `Promise` and sending the resolved `Msg` to your `inbox`.
     */
    fun send(promise: Promise<Msg>) {
        GlobalScope.launch { messages.send(promise.await()) }
    }

    /**
     * Send a `Msg` and a `Promise`
     *
     * Useful for updating the state right away while also firing off a promise to be processed.
     */
    fun send(msg: Msg, promise: Promise<Msg>) {
        GlobalScope.launch {
            messages.send(msg)
            messages.send(promise.await())
        }
    }

    /**
     * Send a new `Model`
     *
     * This is a simpler way to update the application state
     * best used in tandem with `model.copy`
     *
     * (without this every app would implement it on their own with `Msg`
     * this feels much more understandable for simple apps)
     *
     * **Example**
     * ```
     * send(model.copy(switch = true))
     * ```
     */
    fun send(m: Model) {
        model = m
        frameHandler()
    }

    private val messages: Channel<Msg> = Channel<Msg>().also {
        GlobalScope.launch {
            for (msg in it) {
                model = inboxHandler(msg)
                frameHandler()
            }
        }
    }

    init {
        init?.let { GlobalScope.launch { messages.send(it.await()) } }
        frameHandler()
    }
}
