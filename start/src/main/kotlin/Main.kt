import kotlinx.html.a
import kotlinx.html.h1
import kotlinx.html.js.onClickFunction
import me.theghostin.nimble.app
import me.theghostin.nimble.html

// The model is all the data in your application.
// right now it is simple but it will grow
data class Model(val title: String = "nimble ⚒")

// a silly example of adding extension functions
// to the model to organize update logic
fun Model.flip() = when (title) {
    "nimble ⚒" ->
        copy(title = "⚒ nimble")
    "⚒ nimble" ->
        copy(title = "nimble ⚒")
    else -> this
}

// Messages describe in what ways your app model can be updated
sealed class Msg
// this is a generic way to update the entire app model
// you will add new messages as you go
data class Update(val model: Model) : Msg()

// use this to include any file type (supported by webpack.config.d)
// from the `src/main/resources` folder
// example: `img(src = "my-icon.png".resource) {}`
// NOTE: the KotlinDCE gradle plugin changes this path.
val String.resource: dynamic get() =
    kotlinext.js.require("../../resources/$this")

fun main(args: Array<String>) {
    // see webpack.config.d/css.js for plugins
    "main.css".resource

    app<Msg, Model>(Model()) {
        // automatically re-renders after every `Msg` is processed
        html {
            // `model` is always the latest result of `inbox`
            h1 { +model.title }

            a {
                +"⚒"
                // NOTE: you can send `Msg` or `Promise<Msg>` as well as `Model`
                // promises are useful for things like HTTP requests to APIs
                onClickFunction = { send(model.flip()) }
            }
        }
    }
}