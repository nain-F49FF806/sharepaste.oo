plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "alt.nainapps.sharepaste"
    compileSdk = 34

    defaultConfig {
        applicationId = "alt.nainapps.sharepaste"
        minSdk = 26
        targetSdk = 34
        versionCode = 1728150000
        versionName = "2024.10.05"
        setProperty("archivesBaseName", "sharepaste.oo")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        splits {
            // Configures multiple APKs based on ABI.
            abi {
                // Enables building multiple APKs per ABI.
                isEnable = true
                // By default all ABIs are included, so use reset() and include to specify that you only
                // want APKs for x86 and x86_64.
                // Resets the list of ABIs for Gradle to create APKs for to none.
                reset()
                // Specifies a list of ABIs for Gradle to create APKs for.
                include("armeabi-v7a", "arm64-v8a")
                // Specify if you don't want to also generate a universal APK that includes all ABIs.
                isUniversalApk = true
            }
        }
    }

    signingConfigs {
        create("github") {
            storeFile = file(System.getenv("SIGNING_STORE_FILE_PATH") ?: "keystore.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    flavorDimensions += listOf("source")
    productFlavors {
        create("source-ambient") {
            dimension = "source"
            applicationIdSuffix = ".fork"
        }
        create("source-fdroid") {
            dimension = "source"
            applicationIdSuffix = ".fdroid"
        }
        create("source-original") {
            dimension = "source"
        }
    }
    ndkVersion = "25.1.8937393"
}

dependencies {

    implementation(project(":rsnative"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.org.jetbrains.kotlin.plugin.serialization.gradle.plugin)
    implementation(libs.composePrefs)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
