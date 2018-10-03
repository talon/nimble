# Nimble [ ![Download](https://api.bintray.com/packages/spookyspecter/me.theghostin/nimble/images/download.svg) ](https://bintray.com/spookyspecter/me.theghostin/nimble/_latestVersion) 
> Nimble is for writing web apps with Kotlin 

## Example App

```kotlin
import me.theghostin.nimble.app
import kotlinx.html.button
import kotlinx.html.h1
import kotlinx.html.js.onClickFunction

// state
data class Model(val switch: Boolean = false)

// messages
sealed class Msg
object Flip : Msg()

fun main() {
    // within the `app` scope, `model` is the latest state
    app<Msg, Model>(Model()) { 

        // handle messages and update the state
        inbox { when (it) {
            Flip -> model.copy(switch = !model.switch)
        }}

        // the html renders automatically after each message
        html {
            h1 { +when (model.switch) {
                true -> "On"
                false -> "Off"
            } }
            button {
                +"flip"
                // `send` is also available within the `app` scope
                onClickFunction = { send(Flip) }
            }
        }

    }
}
```

# Getting started

To get up and running right away 
- copy the `start` folder at the root of this repo
- and use `gradelw -t run` to start the dev server at http://localhost:8088

