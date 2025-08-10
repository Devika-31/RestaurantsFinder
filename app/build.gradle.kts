plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.dev.restaurantsfinder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dev.restaurantsfinder"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //for responsiveness
    implementation(libs.laibrarySdpAndroid)
    implementation(libs.laibrarySspAndroid)

    // Map
    implementation(libs.play.services.maps)
    implementation(libs.view)

    //For glide
    implementation(libs.glide)

    //Koin
    implementation(libs.insert.koin.koin.core)
    implementation(libs.insert.koin.koin.android)

    //REST - APIServices
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //Chucker  lib
    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)

    //swipe to refresh
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.play.services.location)

    implementation (libs.androidx.fragment.ktx)



}