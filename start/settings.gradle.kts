rootProject.name = "a-nimble-app"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://kotlin.bintray.com/kotlinx")
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "kotlin-platform-js",
                "kotlin-platform-common",
                "kotlin-platform-jvm" ->
                    useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                "kotlinx-serialization" ->
                    useModule("org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin:${requested.version}")
                "org.jetbrains.kotlin.frontend" ->
                    useModule("org.jetbrains.kotlin:kotlin-frontend-plugin:${requested.version}")
                "com.pascalwelsch.gitversioner" ->
                    useModule("com.pascalwelsch.gitversioner:gitversioner:${requested.version}")
                "org.jetbrains.dokka" ->
                    useModule("org.jetbrains.dokka:dokka-gradle-plugin:${requested.version}")
                "com.jfrog.bintray" ->
                    useModule("com.jfrog.bintray.gradle:gradle-bintray-plugin:${requested.version}")
            }
        }
    }
}
