plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.kotlin.examen_moviles"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kotlin.examen_moviles"
        minSdk = 27
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.fragment:fragment-ktx:1.5.0")
    implementation("androidx.activity:activity-ktx:1.5.0")
    implementation("androidx.databinding:databinding-runtime:7.1.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("com.github.parse-community.Parse-SDK-Android:parse:4.3.0")
    implementation("com.google.android.material:material:1.11.0")  // Use latest version

    // Retrofir
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("jp.wasabeef:glide-transformations:4.3.0")

    //Corrutinas
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")

    //Fragment
    implementation ("androidx.fragment:fragment-ktx:1.5.0")

    //Activity
    implementation ("androidx.activity:activity-ktx:1.5.0")

    //Data binding
    implementation ("androidx.databinding:databinding-runtime:7.1.2")

    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
}