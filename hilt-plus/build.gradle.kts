import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.vanniktech.maven.publish)
    alias(libs.plugins.dokka)
}
val hiltPlusVersion = project.properties["version"] as String

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
    implementation(libs.hilt.core)
    implementation(libs.room.common)
}

mavenPublishing {
//    publishToMavenCentral(SonatypeHost.DEFAULT)
    // or when publishing to https://s01.oss.sonatype.org
//    publishToMavenCentral(SonatypeHost.S01)
    // or when publishing to https://central.sonatype.com/
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()
    coordinates("top.cyclops", "hilt-plus", hiltPlusVersion)
    pom {
        name = "hilt-plus"
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
            url = "https://github.com/cyclops-top/hilt-plus/tree/master/hilt-plus"
            connection = "scm:git:git@github.com:cyclops-top/hilt-plus.git"
            developerConnection = "scm:git:ssh://git@github.com:cyclops-top/hilt-plus.git"
        }
    }
}