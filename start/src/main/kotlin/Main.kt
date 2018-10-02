import kotlinx.html.h1
import me.theghostin.nimble.app
import me.theghostin.nimble.html

data class Model(val title: String = "lab.theghostin.me")

sealed class Msg

fun main(args: Array<String>) {
    app<Msg, Model>(Model()) {
        html {
            h1 { +model.title }
        }
    }
}