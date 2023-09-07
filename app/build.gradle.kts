
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("io.realm.kotlin")
    id("org.jetbrains.kotlin.plugin.serialization")
}



android {
    namespace = "com.example.smarthouse"
    compileSdk = 34

    defaultConfig {
        applicationId = "catrine.dev.smarthouse"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.0")

    implementation ("io.realm.kotlin:library-base:1.11.0")
    implementation ("io.realm.kotlin:library-sync:1.11.0")// If using Device Sync
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // If using coroutines with the SDK

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
    implementation("com.google.accompanist:accompanist-pager:0.13.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.13.0")

    implementation("com.google.code.gson:gson:2.10.1")

    //Ktor dependencies
    val ktorVersion="2.3.4"
    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    // HTTP engine: The HTTP client used to perform network requests.
    implementation("io.ktor:ktor-client-android:${ktorVersion}")
    // The serialization engine used to convert objects to and from JSON
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
    // Ktor logging
    implementation("io.ktor:ktor-client-logging:${ktorVersion}")
    // Content negotiation
    implementation ("io.ktor:ktor-client-content-negotiation:$ktorVersion")


    var serialization_version = "1.3.0"
    // Serialization plugin support
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}