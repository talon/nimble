# Nimble
> Nimble is for writing web apps with Kotlin

## [Docs](https://talon.github.io/nimble/me.theghostin.nimble/index.html)

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

# Usage 

```kotlin
repositories { 
    maven("https://dl.bintray.com/spookyspecter/me.theghostin") 
}
```
