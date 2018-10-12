# Nimble [ ![Download](https://api.bintray.com/packages/spookyspecter/me.theghostin/nimble/images/download.svg) ](https://bintray.com/spookyspecter/me.theghostin/nimble/_latestVersion) 

A Nimble `app` exposes a small but effective set of APIs that make
writing web apps in Kotlin fun and easy!

> simple should be simple...

## Example App

**Simple Example:**
```kotlin
import me.theghostin.nimble.app
import me.theghostin.nimble.html
import kotlinx.html.a
import kotlinx.html.h1
import kotlinx.html.js.onClickFunction

data class Model(val switch: Boolean = false)

sealed class Msg

fun main() {
    app<Msg, Model>(Model()) {
        html {
            h1 { +when (model.switch) {
                true -> "On"
                false -> "Off"
            } }
            a {
                +"flip"
                onClickFunction = { send(model.copy(switch = !model.switch)) }
            }
        }
    }
}
```

> ...though, it shouldn't be hard to grow...

**Some things to know:**

- the `inbox` receives `Msg`s and returns new copies of `Model`
- the latest result of `inbox` is always available as `model`
- use `send` to deliver `Msg`s to the inbox
- the `html` is automatically rendered after each `Msg` is processed.

**Inbox Example:**

```kotlin
import me.theghostin.nimble.app
import me.theghostin.nimble.html
import kotlinx.html.a
import kotlinx.html.h1
import kotlinx.html.js.onClickFunction

data class Model(val switch: Boolean = false)

sealed class Msg
data class Set(val switch: Boolean) : Msg()

fun main() {
    app<Msg, Model>(Model()) {
        inbox { when (it) {
          is Set -> mode.copy(switch = it.switch)
        } }
        
        html {
            h1 { +when (model.switch) {
                true -> "On"
                false -> "Off"
            } }
            a {
                +"flip"
                onClickFunction = { send(model.copy(switch = !model.switch)) }
            }
            a {
                +"on"
                onClickFunction = { send(Set(true)) }
            }
            a {
                +"off"
                onClickFunction = { send(Set(false)) }
            }
        }
    }
}
```

> ...before you go...

`send` will handle `Promise<Msg>` as well, so you can use it to make HTTP
requests that resolve `Msg` with the fetched data, or other async tasks.
 
# Getting started

To get up and running right away 
- copy the `start` folder at the root of this repo
- and use `gradelw -t run` to start the dev server at http://localhost:8088

> ...good luck friend! Take it slow. ⚒️
