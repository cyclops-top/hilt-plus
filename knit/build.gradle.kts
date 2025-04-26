import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.vanniktech.maven.publish)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    api(libs.kotlinpoet.ksp)
    implementation(libs.ksp.api)
}

mavenPublishing {
//    publishToMavenCentral(SonatypeHost.DEFAULT)
    // or when publishing to https://s01.oss.sonatype.org
//    publishToMavenCentral(SonatypeHost.S01)
    // or when publishing to https://central.sonatype.com/
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
    coordinates("top.cyclops", "knit", "0.1.0")
    pom {
        name = "knit"
        description = "kotlinpoet dsl"
        url = "https://github.com/cyclops-top/hilt-plus"
        licenses {
            license {
                name = "Apache-2.0"
                url = "https://spdx.org/licenses/Apache-2.0.html"
            }
        }

        developers {
            developer {
                id = "cyclops-top/hilt-plus"
                name = "Justin cheng"
                url = "https://www.cyclops.top"
            }
        }
        scm {
            url = "https://github.com/cyclops-top/hilt-plus/tree/master/knit"
            connection = "scm:git:git@github.com:cyclops-top/hilt-plus.git"
            developerConnection = "scm:git:ssh://git@github.com:cyclops-top/hilt-plus.git"
        }
    }
}