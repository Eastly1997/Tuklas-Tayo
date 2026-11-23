plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// Mock is the default. Firebase is explicitly enabled only after local configuration.
val useFirebase = providers.gradleProperty("tuklastayo.useFirebase").orNull == "true"
val authConfigured = file("google-services.json").isFile
if (useFirebase || authConfigured) {
    require(file("google-services.json").isFile) {
        "Firebase requires your own app/google-services.json. See README.md."
    }
    apply(plugin = "com.google.gms.google-services")
}

android {
    namespace = "com.lakbay.tuklas.app"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.lakbay.tuklas"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "com.lakbay.tuklas.TuklasTestRunner"
        buildConfigField("boolean", "USE_FIREBASE", useFirebase.toString())
        buildConfigField("boolean", "AUTH_CONFIGURED", authConfigured.toString())
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    // Local JVM auth tests use fake repositories; Android Log has no JVM implementation.
    testOptions { unitTests.isReturnDefaultValues = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
dependencies {
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play)
    implementation(libs.google.id)
    implementation(libs.google.base)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.coroutines.play.services)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(project(":core:designsystem"))
    implementation(project(":feature:discover"))
    implementation(project(":feature:manage"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.koin.android)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    // Compose's transitive Espresso baseline predates Android 16 input API changes.
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.compose.ui.test.manifest)
    debugImplementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.coroutines.android)




    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
}
