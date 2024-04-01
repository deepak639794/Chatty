plugins {

    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
  id("kotlin-kapt")
}

android {
    namespace = "com.example.whatsapp1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.whatsapp1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding = true
    }
    allprojects {
        repositories {
//            maven { url "https://jitpack.io" }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "-keep class io.agora.**{*;}"
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:24.10.3")

    //noinspection BomWithoutPlatform

    implementation("com.google.firebase:firebase-bom:32.7.2")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")



    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("com.google.gms:google-services:4.4.1")
    implementation("com.google.firebase:firebase-storage")


    // implementation("com.google.firebase:firebase-messaging:23.4.1")


    implementation ("io.agora.rtc:full-sdk:4.3.0")
    implementation ("commons-codec:commons-codec:1.11")

    implementation("com.google.firebase:firebase-firestore:24.4.4")
    implementation(libs.androidx.activity)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")



    implementation("androidx.room:room-runtime:2.5.1")
    annotationProcessor("androidx.room:room-compiler:2.5.1")

    kapt("androidx.room:room-compiler:2.5.1")
//    implementation ("androidx.room:room-runtime:2.6.1")
//    implementation ("android.arch.persistence.room:runtime:2.6.1")
//    annotationProcessor ("android.arch.persistence.room:compiler:2.6.1")

//    implementation ("android.arch.persistence.room:runtime:2.6.1")
//    kapt ("android.arch.persistence.room:compiler:2.6.1")


}