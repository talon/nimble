import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

group = "me.theghostin"
version = "1.0.3"

plugins {
    idea
    id("kotlin-platform-js") version "1.2.71"
    `maven-publish`
    // TODO: https://github.com/Kotlin/dokka/issues/212
    // id("org.jetbrains.dokka") version "0.9.17"
    id("com.jfrog.bintray") version "1.8.4"
}

repositories {
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://dl.bintray.com/danfma/kotlin-kodando")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.6.8")
    implementation("br.danfma.kodando:kodando-history:0.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:0.23.3")
}

kotlin {
    experimental {
        coroutines = Coroutines.ENABLE
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    classifier = "sources"
    from(java.sourceSets["main"].output)
}

bintray {
    user =  System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    publish = true
    setPublications("Nimble")
    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "me.theghostin"
        name = "nimble"
        userOrg = user
        setLicenses("GPL-3.0")
        setLabels("kotlin")
    })
}

publishing {
    publications {
        create("Nimble", MavenPublication::class.java) {
            artifactId = project.name
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}

tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            outputFile = "${buildDir.path}/js/${project.name}.js"
            sourceMap = true
            metaInfo = true
            moduleKind = "commonjs"
            main = "call"
        }
    }
    "cleanDocs"(Delete::class) {
        delete("$rootDir/docs")
    }
    // "dokka"(DokkaTask::class) {
    //     outputFormat = "html"
    //     outputDirectory = "$rootDir/docs"
    //     impliedPlatforms = listOf("JS").toMutableList()
    //     // reportUndocumented = false
    //     dependsOn("cleanDocs")
    // }
}
