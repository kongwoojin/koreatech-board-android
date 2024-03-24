import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val localStoreFile: String = gradleLocalProperties(rootDir).getProperty("localStoreFile")
val localStorePassword: String = gradleLocalProperties(rootDir).getProperty("localStorePassword")
val localKeyAlias: String = gradleLocalProperties(rootDir).getProperty("localKeyAlias")
val localKeyPassword: String = gradleLocalProperties(rootDir).getProperty("localKeyPassword")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.gms.oss-licenses-plugin")
    id("dagger.hilt.android.plugin")
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlint
    id("com.google.devtools.ksp")
    kotlin("kapt")
    kotlin("plugin.parcelize")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kongjak.koreatechboard"
        minSdk = Project.minSdk
        targetSdk = Project.targetSdk
        versionCode = Project.versionCode
        versionName = Project.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(localStoreFile)
            storePassword = localStorePassword
            keyAlias = localKeyAlias
            keyPassword = localKeyPassword
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "proguard-debug.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    namespace = "com.kongjak.koreatechboard"
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Kotlin.kotlinCompilerExtension
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(Dependencies.AndroidX.activityCompose)
    implementation(Dependencies.AndroidX.browser)
    implementation(Dependencies.AndroidX.composeMaterial3)
    implementation(Dependencies.AndroidX.composeMaterial3PullRequest)
    implementation(Dependencies.AndroidX.composeRuntimeLivedata)
    implementation(Dependencies.AndroidX.composeUI)
    implementation(Dependencies.AndroidX.composeUIGraphics)
    implementation(Dependencies.AndroidX.composeUIToolingPreview)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.hiltNavigationCompose)
    implementation(Dependencies.AndroidX.lifecycleRuntime)
    implementation(Dependencies.AndroidX.lifecycleViewModelCompose)
    implementation(Dependencies.AndroidX.lifecycleViewModelKtx)
    implementation(Dependencies.AndroidX.livedataKtx)
    implementation(Dependencies.AndroidX.navigationCompose)
    implementation(Dependencies.AndroidX.pagingCompose)
    implementation(Dependencies.AndroidX.pagingRuntimeKtx)
    implementation(platform(Dependencies.AndroidX.composeBom))

    implementation(Dependencies.Etc.glide)
    implementation(Dependencies.Etc.glideOkhttp3)
    implementation(Dependencies.Etc.glideAnnotation)
    implementation(Dependencies.Etc.glideCompose)
    implementation(Dependencies.Etc.retrofit2)
    implementation(Dependencies.Etc.retrofit2Gson)

    implementation(Dependencies.Google.hilt)
    implementation(Dependencies.Google.material)
    implementation(Dependencies.Google.ossLicense)

    kapt(Dependencies.Google.hiltCompiler)
    ksp(Dependencies.Etc.glideKsp)

    debugImplementation(Dependencies.Debug.composeUITestManifest)
    debugImplementation(Dependencies.Debug.composeUITooling)

    testImplementation(Dependencies.Test.junit)

    androidTestImplementation(Dependencies.AndroidTest.composeJunit)
    androidTestImplementation(Dependencies.AndroidTest.espresso)
    androidTestImplementation(Dependencies.AndroidTest.junit)
    androidTestImplementation(platform(Dependencies.AndroidTest.composeBom))
}
