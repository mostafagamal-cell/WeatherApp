plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {

    namespace = "com.example.weatherapp"
    compileSdk = 34
    bundle {
        language {
            enableSplit = false
        }
    }
    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {
    implementation(libs.androidx.junit.ktx)
    implementation(libs.core.ktx)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.core.i18n)
    implementation(libs.androidx.preference)
    testImplementation(libs.androidx.runner)
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // Jetpack Compose integration
    implementation(libs.converter.gson)

    // Views/Fragments integration
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation (libs.threetenabp)

    implementation ("org.osmdroid:osmdroid-android:6.1.14")
    implementation("androidx.room:room-ktx:$room_version")
    implementation ("com.google.android.gms:play-services-location:19.0.1")


    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // Lifecycles only (without ViewModel or LiveData)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Lifecycle utilities for Compose
    implementation(libs.androidx.lifecycle.runtime.compose)
    runtimeOnly("androidx.preference:preference:1.2.1")

    // Saved state module for ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)
    kapt(libs.androidx.lifecycle.compiler)
    implementation (libs.glide)
    testImplementation ("org.robolectric:robolectric:4.10.3")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation ("androidx.test:core:1.6.1") // from 1.4.0
    testImplementation ("androidx.arch.core:core-testing:2.2.0") // from 2.1.0
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // from 1.3.0


        val androidXTestVersion="1.6.1"
        val testRulesVersion="1.6.1"
        val testJunitVersion="1.1.5"
       val testRunnerVersion="1.6.1"
       val espressoVersion= "3.6.1"
       val  truthVersion= "1.5.0"
        // Core library
        androidTestImplementation("androidx.test:core:$androidXTestVersion")

        // AndroidJUnitRunner and JUnit Rules
        androidTestImplementation("androidx.test:runner:$testRunnerVersion")
        androidTestImplementation("androidx.test:rules:$testRulesVersion")

        // Assertions
        androidTestImplementation("androidx.test.ext:junit:$testJunitVersion")
        androidTestImplementation("androidx.test.ext:truth:$truthVersion")

            testImplementation ("org.robolectric:robolectric:4.10.3")
        // Espresso dependencies
        androidTestImplementation( "androidx.test.espresso:espresso-core:$espressoVersion")
        androidTestImplementation( "androidx.test.espresso:espresso-contrib:$espressoVersion")
        androidTestImplementation( "androidx.test.espresso:espresso-intents:$espressoVersion")
        androidTestImplementation( "androidx.test.espresso:espresso-accessibility:$espressoVersion")
        androidTestImplementation( "androidx.test.espresso:espresso-web:$espressoVersion")
        androidTestImplementation( "androidx.test.espresso.idling:idling-concurrent:$espressoVersion")

        // The following Espresso dependency can be either "implementation",
        // or "androidTestImplementation", depending on whether you want the
        // dependency to appear on your APK"s compile classpath or the test APK
        // classpath.
        androidTestImplementation( "androidx.test.espresso:espresso-idling-resource:$espressoVersion")
         implementation("androidx.core:core-splashscreen:1.0.1")

}
