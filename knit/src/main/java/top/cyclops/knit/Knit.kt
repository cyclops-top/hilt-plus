package top.cyclops.knit

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import kotlin.reflect.KClass


fun type(clazz: KClass<*>): TypeName {
    return clazz.asTypeName()
}

fun type(className: ClassName): TypeName {
    className.parameterizedBy()
    return className
}

fun type(name: String): TypeName {
    return ClassName.bestGuess(name)
}
