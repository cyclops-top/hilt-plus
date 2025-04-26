#Knit 介绍
Knit是一个针对`kotlinpoet`开发的快速生成kotlin代码的工具。

## 示例

生成一个名为Simple.Kt的代码文件，代码如下：

```kotlin
package top.cyclops.simple

annotation class Description(val value: string)
interface Cat {
    fun jump()
}

@Description("tom is a cat")
class Tom : Cat {
    override fun jump() {
        //do jump
    }
}

```

生成代码

```kotlin

file("top.cyclops.simple", "Simple") {
    val descriptionClassName = ClassName("top.cyclops.simple", "Description")
    +classType(descriptionClassName) {
        +`annotation`

        +constructor {
            +"value".parameter(String::class.asTypeName())
        }
        +"value".property(String::class.asTypeName()) {
            initializer()
        }
    }
    val catClassName = ClassName("top.cyclops.simple", "Cat")
    +interfaceType(catClassName) {
        +"jump".func {
            +abstract
        }
    }
    val tomClassName = ClassName("top.cyclops.simple", "Tom")
    +classType(tomClassName) {
        +annotation(descriptionClassName) {
            "value" set "tom is a cat"
        }
        +superInterface(catClassName)
        +"jump".func {
            +override
        }
    }
}
```
