package top.cyclops.knit

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.TypeSpec

interface FileKnit {
    fun import(packageName: String, simpleName: String, alias: String = "") {
        import(MemberName(packageName, simpleName), alias)
    }

    fun import(name: MemberName, alias: String = "")
    operator fun TypeSpec.unaryPlus(): TypeSpec
    operator fun AnnotationSpec.unaryPlus(): AnnotationSpec

    companion object {
        operator fun invoke(builder: FileSpec.Builder): FileKnit {
            return object : FileKnit {
                override fun import(name: MemberName, alias: String) {
                    if (alias.isEmpty()) {
                        builder.addImport(name.packageName, name.simpleName)
                    } else {
                        builder.addAliasedImport(name, alias)
                    }
                }

                override fun TypeSpec.unaryPlus(): TypeSpec = apply {
                    builder.addType(this)
                }

                override fun AnnotationSpec.unaryPlus(): AnnotationSpec = apply {
                    builder.addAnnotation(this)
                }
            }
        }
    }
}


fun file(className: ClassName, block: FileKnit.() -> Unit): FileSpec {
    val builder = FileSpec.builder(className)
    FileKnit(builder).block()
    return builder.build()
}

fun file(packageName: String, fileName: String, block: FileKnit.() -> Unit): FileSpec {
    val builder = FileSpec.builder(packageName, fileName)
    FileKnit(builder).block()
    return builder.build()
}