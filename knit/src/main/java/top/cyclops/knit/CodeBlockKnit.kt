@file:Suppress("unused")

package top.cyclops.knit

import com.squareup.kotlinpoet.CodeBlock

interface CodeBlockKnit {
    operator fun CodeBlock.unaryPlus(): CodeBlock
    operator fun String.unaryPlus()
    operator fun Codes.unaryPlus()

    companion object {
        operator fun invoke(builder: CodeBlock.Builder): CodeBlockKnit {
            return object : CodeBlockKnit {
                override fun CodeBlock.unaryPlus(): CodeBlock {
                    builder.add(this)
                    return this
                }

                override fun String.unaryPlus() {
                    builder.add(this)
                }

                override fun Codes.unaryPlus() {
                    when (this) {
                        is Codes.Named -> {
                            builder.addNamed(code, args)
                        }

                        is Codes.Simple -> {
                            builder.add(code, *args)
                        }
                    }
                }
            }
        }
    }
}

fun controlFlow(flow: String, vararg args: String, block: CodeBlockKnit.() -> Unit): CodeBlock {
    val builder = CodeBlock.builder()
    builder.beginControlFlow(flow, *args)
    CodeBlockKnit(builder).block()
    builder.endControlFlow()
    return builder.build()
}

fun codeBlock(block: CodeBlockKnit.() -> Unit): CodeBlock {
    val builder = CodeBlock.builder()
    CodeBlockKnit(builder).block()
    return builder.build()
}
