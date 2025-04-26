@file:Suppress("unused")

package top.cyclops.knit

sealed interface Codes {
    class Simple(val code: String, val args: Array<out Any?>) : Codes
    class Named(val code: String, val args: Map<String, Any?>) : Codes
}

fun codes(code: String, vararg args: Any?): Codes {
    return Codes.Simple(code, args)
}

fun namedCodes(code: String, args: Map<String, Any?>): Codes {
    return Codes.Named(code, args)
}