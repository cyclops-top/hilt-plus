package top.cyclops.knit

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName

interface PropertyKnit : ModifierSupport, PropertyModifiers {
    fun initializer()
    fun initializer(name: String)

    operator fun TypeVariableName.unaryPlus(): TypeVariableName

    companion object {
        operator fun invoke(name: String, builder: PropertySpec.Builder): PropertyKnit {
            return object : PropertyKnit {
                override fun KModifier.unaryPlus(): KModifier = apply {
                    builder.addModifiers(this)
                }

                override fun initializer() {
                    builder.initializer(name)
                }

                override fun initializer(name: String) {
                    builder.initializer(name)
                }

                override fun TypeVariableName.unaryPlus(): TypeVariableName {
                    builder.addTypeVariable(this)
                    return this
                }
            }
        }
    }
}


fun String.property(typeName: TypeName, block: PropertyKnit.() -> Unit = {}): PropertySpec {
    val builder = PropertySpec.builder(this, typeName)
    PropertyKnit(this, builder).block()
    return builder.build()
}