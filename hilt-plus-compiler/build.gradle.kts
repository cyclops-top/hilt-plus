import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    kotlin("kapt")
    alias(libs.plugins.vanniktech.maven.publish)
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    implementation(project(":hilt-plus"))
    implementation(kotlin("reflect"))
    implementation(project(":knit"))
    implementation(libs.hilt.core)
    implementation(libs.hilt.dagger.core)
    implementation(libs.room.common)
    implementation(libs.ksp.api)
    implementation(libs.auto.service.annotations)
    kapt(libs.auto.service)
}

mavenPublishing {
//    publishToMavenCentral(SonatypeHost.DEFAULT)
    // or when publishing to https://s01.oss.sonatype.org
//    publishToMavenCentral(SonatypeHost.S01)
    // or when publishing to https://central.sonatype.com/
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
    coordinates("top.cyclops", "hilt-plus-compiler", "0.1.0")
    pom {
        name = "hilt-plus-compiler"
        description = "generate room and api provider for hilt"
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
            url = "https://github.com/cyclops-top/hilt-plus/tree/master/hilt-plus-compiler"
            connection = "scm:git:git@github.com:cyclops-top/hilt-plus.git"
            developerConnection = "scm:git:ssh://git@github.com:cyclops-top/hilt-plus.git"
        }
    }
}