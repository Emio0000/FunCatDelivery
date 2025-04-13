plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)



}

android {
    namespace = "com.example.onlinefoodorderingsystem"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.onlinefoodorderingsystem"
        minSdk = 30
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)

    implementation("com.google.code.gson:gson:2.10.1") // ✅ Kotlin DSL syntax

    implementation ("androidx.recyclerview:recyclerview:1.4.0")

    implementation ("androidx.recyclerview:recyclerview-selection:1.1.0")

    implementation ("androidx.cardview:cardview:1.0.0")

    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

apply(plugin = "com.google.gms.google-services") // ✅ This goes at the **bottom** of this file!