@file:Suppress("unused")

package top.cyclops.knit

import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.toKModifier

interface ModifierSupport {
    operator fun KModifier.unaryPlus(): KModifier
    operator fun Modifier.unaryPlus() {
        toKModifier()?.unaryPlus()
    }

    operator fun KModifier.plus(other: KModifier): KModifier {
        other.unaryPlus()
        return other
    }
}

interface ClassModifiers {
    val `public` get() = KModifier.PUBLIC
    val `protected` get() = KModifier.PROTECTED
    val `private` get() = KModifier.PRIVATE
    val `internal` get() = KModifier.INTERNAL
    val `annotation` get() = KModifier.ANNOTATION
    val `expect` get() = KModifier.EXPECT
    val `actual` get() = KModifier.ACTUAL

    val `final` get() = KModifier.FINAL
    val `open` get() = KModifier.OPEN
    val `abstract` get() = KModifier.ABSTRACT

    val `external` get() = KModifier.EXTERNAL
    val `inner` get() = KModifier.INNER

    val `companion` get() = KModifier.COMPANION

    val `data` get() = KModifier.DATA

    val `enum` get() = KModifier.ENUM

}


interface FunModifiers {

    val `public` get() = KModifier.PUBLIC
    val `protected` get() = KModifier.PROTECTED
    val `private` get() = KModifier.PRIVATE
    val `internal` get() = KModifier.INTERNAL

    val `expect` get() = KModifier.EXPECT
    val `actual` get() = KModifier.ACTUAL

    val `final` get() = KModifier.FINAL
    val `open` get() = KModifier.OPEN
    val `abstract` get() = KModifier.ABSTRACT


    val `external` get() = KModifier.EXTERNAL
    val `override` get() = KModifier.OVERRIDE
    val `tailrec` get() = KModifier.TAILREC
    val `suspend` get() = KModifier.SUSPEND
    val `inline` get() = KModifier.INLINE

    val `infix` get() = KModifier.INFIX
    val `operator` get() = KModifier.OPERATOR

}

interface ParameterModifiers {
    val `vararg` get() = KModifier.VARARG
    val `noinline` get() = KModifier.NOINLINE
    val `crossinline` get() = KModifier.CROSSINLINE
}

interface PropertyModifiers {

    val `public` get() = KModifier.PUBLIC
    val `protected` get() = KModifier.PROTECTED
    val `private` get() = KModifier.PRIVATE
    val `internal` get() = KModifier.INTERNAL

    val `expect` get() = KModifier.EXPECT
    val `actual` get() = KModifier.ACTUAL

    val `final` get() = KModifier.FINAL
    val `open` get() = KModifier.OPEN
    val `abstract` get() = KModifier.ABSTRACT
    val `const` get() = KModifier.CONST


    val `external` get() = KModifier.EXTERNAL
    val `override` get() = KModifier.OVERRIDE
    val `lateinit` get() = KModifier.LATEINIT
}

interface TypeParameterModifiers {
    val `reified` get() = KModifier.REIFIED
}

interface InterfaceModifiers {
    val `public` get() = KModifier.PUBLIC
    val `protected` get() = KModifier.PROTECTED
    val `private` get() = KModifier.PRIVATE
    val `internal` get() = KModifier.INTERNAL
    val `fun` get() = KModifier.FUN
}

interface VarianceAnnotationModifiers {
    val `in` get() = KModifier.IN
    val `out` get() = KModifier.OUT
}