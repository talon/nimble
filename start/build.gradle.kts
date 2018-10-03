import org.jetbrains.kotlin.gradle.frontend.FrontendPlugin
import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinJsDcePlugin
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJsDce

group = ""
version = "1.0-SNAPSHOT"

plugins {
    idea
    id("kotlin-platform-js") version "1.2.71"
    id("kotlinx-serialization") version "0.6.2"
    id("org.jetbrains.kotlin.frontend") version "0.0.37"
}

repositories {
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://kotlin.bintray.com/kotlin-js-wrappers")
    maven("https://dl.bintray.com/danfma/kotlin-kodando")
    maven("https://dl.bintray.com/spookyspecter/me.theghostin")
}

apply<KotlinJsDcePlugin>()

dependencies {
    compile(kotlin("stdlib-js"))
    compile("org.jetbrains.kotlinx:kotlinx-html-js:0.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:0.24.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:0.6.2")
    implementation("org.jetbrains:kotlin-extensions:1.0.1-pre.54-kotlin-1.2.70")
    implementation("br.danfma.kodando:kodando-history:0.5.0")
    compile("me.theghostin:nimble:1.0.2")
}

// nimble - core
////
configure<KotlinFrontendExtension> {
    bundle<WebPackExtension>("webpack") { when (this) { is WebPackExtension ->
        bundleName = project.name
    } }
}
configure<NpmExtension> {
    // nimble - core
    ////
    dependency("kotlinx-coroutines-core")
    dependency("history")
    dependency("nanomorph")

    // webpack - css
    ////
    devDependency("html-webpack-plugin")
    devDependency("style-loader")
    devDependency("css-loader")
    devDependency("postcss-loader")
    devDependency("postcss-import")
    devDependency("postcss-preset-env")
    devDependency("cssnano")

    // webpack - images
    ////
    devDependency("file-loader")
    devDependency("image-webpack-loader")
}

tasks {
    // by tweaking the github repo settings
    // we can host files in the docs/ folder
    // at $user.github.io/$repo
    "deploy"(Copy::class) {
        from("$buildDir/${project.name}/bundle")
        into("$rootDir/docs/${project.name}")
        dependsOn("build")
    }
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            outputFile =  "$buildDir/js/${project.name}.js"
            sourceMap = true
            metaInfo = true
            moduleKind = "commonjs"
            main = "call"
        }
    }
}
