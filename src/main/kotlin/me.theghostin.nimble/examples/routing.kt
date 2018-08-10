@file:Suppress("unused")

package me.theghostin.nimble.examples

import kotlinx.html.button
import kotlinx.html.h1
import kotlinx.html.js.onClickFunction
import me.theghostin.nimble.app
import me.theghostin.nimble.html
import me.theghostin.nimble.router

private enum class Switch {
    On,
    Off
}

private fun Switch.flip() = when (this) {
    Switch.On -> Switch.Off
    Switch.Off -> Switch.On
}

private data class Flip(val switch: Switch)

private fun routing() {
    app<Flip, Switch>(Switch.Off) {
        val routeTo = router {
            "/:switch".route {
                send(Flip(Switch.valueOf(it["switch"]!!)))
            }
        }

        inbox { it.switch }

        html {
            h1 { +"$model" }
            button {
                +"flip"
                onClickFunction = { routeTo("/${model.flip()}") }
            }
        }
    }
}
