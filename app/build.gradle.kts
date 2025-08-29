plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("kotlin-parcelize")
    id ("androidx.navigation.safeargs.kotlin")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.programmingc"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.programmingc"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation (project(":database:databasedependencies"))
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation ("androidx.fragment:fragment-ktx:1.8.5")
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")

    // jet compose navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.5")

    // Firebase
    implementation (platform("com.google.firebase:firebase-bom:34.1.0"))
    implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore:26.0.0")
    implementation ("com.google.firebase:firebase-appcheck:19.0.0")
    implementation ("com.google.firebase:firebase-appcheck-ktx:18.0.0")
    implementation ("com.google.firebase:firebase-appcheck-safetynet:16.1.2")
    implementation("com.google.android.gms:play-services-auth:21.4.0")

    // library hilt
    implementation("com.google.dagger:hilt-android:2.56.2")
    implementation ("androidx.hilt:hilt-navigation-fragment:1.2.0")
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.recyclerview)
    kapt ("com.google.dagger:hilt-android-compiler:2.56.2")
    kapt ("androidx.hilt:hilt-compiler:1.2.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.activity:activity-ktx:1.9.3")

    implementation("androidx.room:room-runtime:2.6.1")

    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.1.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.1.0")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.navigation:navigation-runtime-ktx:2.8.5")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:2.6.1")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:2.6.1")
    implementation ("androidx.databinding:databinding-runtime:8.7.3")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")
}

kapt {
    correctErrorTypes = true
}