package me.theghostin.nimble

import kodando.history.BrowserHistoryInstance
import kodando.history.History

private val history: BrowserHistoryInstance =
        History.createBrowserHistory()

private fun BrowserHistoryInstance.path(hash: Boolean): Pattern =
        Pattern(if (hash) location.hash.replace("#", "") else location.pathname)

/**
 * Handle changes, and read information from, the URL.
 *
 * **Example:**
 * ```
 * val routeTo = router {
 *   "/orders/:id".route { console.log(it["id"]!!) }
 * }
 * routeTo("/orders/8")
 * ```
 */
fun router(hash: Boolean = false, block: Router.() -> Unit): (String) -> Unit =
        Router(hash).apply {
            block()
            history.listen { _, _ -> block() }
        }::routeTo

data class Router(private val hash: Boolean) {
    /**
     * match a route and pull data off the URL
     *
     * **Example:**
     * ```
     * router {
     *   "/:thing".route {
     *      console.log(it["thing"]!!)
     *   }
     * }
     * ```
     */
    fun String.route(block: (Map<String, String>) -> Unit) =
            Pattern(this).let {
                it.match(history.path(hash)) { params ->
                    when (params) {
                        null -> {
                            Unit
                        }
                        else -> {
                            block(params.unsafeCast<Map<String, String>>())
                        }
                    }
                }
            }

    /**
     * update the URL
     *
     * **Example:**
     * ```
     * val routeTo = router {
     *   "/:thing".route {
     *      console.log(it["thing"]!!)
     *   }
     *   "/redirect".route {
     *     routeTo("/cats")
     *   }
     * }
     * routeTo("/dogs")
     * ```
     */
    fun routeTo(path: String): Unit =
            history.push(if (hash) "#$path" else path)
}

private data class Param(private val string: String) {
    val name
        get(): String =
            string.match(named)?.get(1) ?: string

    val isStatic
        get(): Boolean =
            !string.matches(named)

    private val named = "^:(.+)"
}

private data class Pattern(private val string: String) {
    fun match(path: Pattern, block: (Map<String, String>?) -> Unit): Unit =
            block(when {
                !matches(path) -> null
                else -> paramsFrom(path)
            })

    private val params
        get(): List<Param> =
            string.split("/").map { Param(it) }

    private fun matches(path: Pattern): Boolean =
            when {
                params.size != path.params.size -> false
                else -> params.foldRightIndexed(false) { index, param, _ ->
                    when {
                        param.isStatic -> param == path.params[index]
                        else -> true
                    }
                }
            }

    private fun paramsFrom(path: Pattern): Map<String, String> =
            params.mapIndexed { index, param ->
                param.name to path.params[index].name
            }.toMap()
}
