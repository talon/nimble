import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

group = "me.theghostin"
version = "1.0-SNAPSHOT"

plugins {
    idea
    id("kotlin-platform-js") version "1.2.61"
    `maven-publish`
    id("org.jetbrains.dokka") version "0.9.14"
    id("com.jfrog.bintray") version "1.8.4"
}

repositories {
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://dl.bintray.com/danfma/kotlin-kodando")
}

dependencies {
    implementation(kotlin("stdlib-js"))
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
    "wrapper"(Wrapper::class) {
         // FIXME: while this issue (https://github.com/Kotlin/dokka/issues/265) is fixed in newer dokka plugins
         // newer dokka plugins don't work because of this https://github.com/Kotlin/dokka/issues/212
         gradleVersion = "4.4.1" //version required
    }
    "cleanDocs"(Delete::class) {
        delete("$rootDir/docs")
    }
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

publishing {
    publications {
        create("default", MavenPublication::class.java) {
            artifactId = project.name
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}

// TODO
// dokka {
//     outputFormat = "html"
//     outputDirectory = "$rootDir/docs"
//     impliedPlatforms = ["JS"]
//     // reportUndocumented = false
//     dependsOn("cleanDocs")
// }
// build.dependsOn dokka
//
// bintray {
//     user =  System.getenv("BINTRAY_USER")
//     key = System.getenv("BINTRAY_KEY")
//     publications = ["Nimble"]
//     publish = true
//     pkg {
//         repo = "me.theghostin"
//      name = "nimble"
//      userOrg = user
//      licenses = ["GPL-3.0"]
//    }
// }

