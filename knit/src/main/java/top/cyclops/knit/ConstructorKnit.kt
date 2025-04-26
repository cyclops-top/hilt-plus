package top.cyclops.knit

import com.squareup.kotlinpoet.FunSpec

interface ConstructorKnit : FunKnit {
    companion object {
        operator fun invoke(builder: FunSpec.Builder): ConstructorKnit {
            return object : ConstructorKnit, FunKnit by FunKnit(builder) {

            }
        }
    }
}

fun constructor(block: ConstructorKnit.() -> Unit): ConstructorSpec {
    val builder = FunSpec.constructorBuilder()
    ConstructorKnit(builder).block()
    return ConstructorSpec(builder.build())
}
