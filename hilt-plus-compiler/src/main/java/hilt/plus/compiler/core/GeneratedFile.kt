package hilt.plus.compiler.core

import com.google.devtools.ksp.symbol.KSFile
import com.squareup.kotlinpoet.FileSpec

data class GeneratedFile(
    val file: FileSpec,
    val dependencies: List<KSFile>? = null
)