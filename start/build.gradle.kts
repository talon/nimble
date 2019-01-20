import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

group = "me.theghostin"
version = "1.0-SNAPSHOT"

plugins {
    idea
    id("kotlin-platform-js") version "1.3.11"
    id("kotlinx-serialization") version "1.3.11"
    id("org.jetbrains.kotlin.frontend") version "0.0.44"
}

repositories {
    jcenter()
    maven("https://kotlin.bintray.com/kotlin-eap")
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://kotlin.bintray.com/kotlin-js-wrappers")
    maven("https://dl.bintray.com/danfma/kotlin-kodando")
    maven("https://dl.bintray.com/spookyspecter/me.theghostin")
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.9.1")
    implementation("org.jetbrains:kotlin-extensions:1.0.1-pre.67-kotlin-1.3.11")
    implementation("me.theghostin:nimble:2.1.0")
}

kotlinFrontend {
    bundle<WebPackExtension>("webpack") {
        when (this) { is WebPackExtension ->
            contentPath = file("$buildDir/bundle")
        }
    }

    npm {
        // nimble - core
        ////
        dependency("history")
        dependency("nanomorph")
        // Webpack breaks a lot so it's a good idea to pin this version
        devDependency("webpack-cli", "3.1.2")
        devDependency("html-webpack-plugin")

        // nimble - css
        ////
        devDependency("style-loader")
        devDependency("css-loader")
        devDependency("postcss-loader")
        devDependency("postcss-import")
        devDependency("postcss-preset-env")
        devDependency("cssnano")

        // nimble - images
        ////
        devDependency("file-loader")
        devDependency("image-webpack-loader")
        devDependency("favicons-webpack-plugin")
    }
}

tasks {
    withType<Kotlin2JsCompile> {
        kotlinOptions {
            outputFile = "$buildDir/js/${project.name}.js"
            sourceMap = true
            metaInfo = true
            moduleKind = "commonjs"
            main = "call"
        }
    }
}
