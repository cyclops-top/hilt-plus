package top.cyclops.knit

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName

interface TypeVariableKnit : ModifierSupport, VarianceAnnotationModifiers, TypeParameterModifiers {
    fun bounds(type: TypeName, vararg more: TypeName)
    operator fun TypeName.unaryPlus()
}


private class TypeVariableNameBuilder(val name: String) : TypeVariableKnit {
    private var variance: KModifier? = null
    private val bounds = ArrayList<TypeName>()
    private var isReified = false
    override fun bounds(type: TypeName, vararg more: TypeName) {
        bounds.add(type)
        bounds.addAll(more)
    }

    override fun TypeName.unaryPlus() {
        bounds.add(this)
    }

    override fun KModifier.unaryPlus(): KModifier = apply {
        when (this) {
            KModifier.OUT -> variance = this
            KModifier.IN -> variance = this
            KModifier.REIFIED -> isReified = true
            else -> error("not support modifier $this")
        }
    }

    fun build(): TypeVariableName {
        return TypeVariableName(name, bounds, variance)
    }
}

fun typeVariable(name: String, block: TypeVariableKnit.() -> Unit = {}): TypeVariableName {
    val builder = TypeVariableNameBuilder(name)
    builder.block()
    return builder.build()
}

