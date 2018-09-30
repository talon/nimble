package me.theghostin.nimble

import kotlinx.html.DIV
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.dom.create
import org.w3c.dom.HTMLElement
import kotlin.browser.document

private val morph = js("require('nanomorph')") as Function2<HTMLElement, HTMLElement, Unit>
/**
 * The `html` representation of your `model` over time.
 *
 * **Example:**
 * ```
 * import me.theghostin.nimble.app
 * import me.theghostin.nimble.html
 * import kotlinx.html.h1
 *
 * // ...
 *
 * fun main() {
 *     app<Msg, Model>(Model()) {
 *         inbox {
 *             // ...
 *         }
 *
 *         html {
 *             h1 { +model }
 *         }
 *     }
 * }
 * ```
 */
fun <Msg, Model> Nimble<Msg, Model>.html(container: HTMLElement = document.body?.append!!.div(), html: DIV.() -> Unit) =
        frame { morph(container, document.create.div { html() }) }
