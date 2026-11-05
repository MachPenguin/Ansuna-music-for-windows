plugins {
  id("org.jetbrains.kotlin.jvm")
  id("org.jetbrains.kotlin.plugin.serialization") version "2.3.10"
  id("application")
}

group = "com.ansuna.music"
version = "1.0.0"

kotlin {
  jvmToolchain(21)
}

dependencies {
  // Coroutines + JSON (suspend networking, kotlinx.serialization)
  // Swing, java.net.http.HttpClient and javax.sound.sampled are JDK built-ins.
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
}

application {
  mainClass = "com.ansuna.music.window.MainKt"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
  compilerOptions {
    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
  }
}
