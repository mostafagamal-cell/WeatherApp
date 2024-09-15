buildscript {
    repositories {
        google()
    }


    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}
plugins {

    kotlin("kapt") version "2.0.20"


    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}