import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val localStoreFile: String = gradleLocalProperties(rootDir, providers).getProperty("localStoreFile")
val localStorePassword: String = gradleLocalProperties(rootDir, providers).getProperty("localStorePassword")
val localKeyAlias: String = gradleLocalProperties(rootDir, providers).getProperty("localKeyAlias")
val localKeyPassword: String = gradleLocalProperties(rootDir, providers).getProperty("localKeyPassword")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.koreatechboard.application)
    alias(libs.plugins.koreatechboard.compose)
    alias(libs.plugins.koreatechboard.hilt)
    alias(libs.plugins.koreatechboard.firebase)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.google.services)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.licenses)
    kotlin("plugin.parcelize")
}

android {
    defaultConfig {
        applicationId = "com.kongjak.koreatechboard"
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.project.version.code.get().toInt()
        versionName = libs.versions.project.version.name.get().toString()

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
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "proguard-debug.pro"
            )
        }
    }
    namespace = "com.kongjak.koreatechboard"
}

dependencies {

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.hilt.compose)
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.webkit)
    kapt(libs.androidx.room.compiler)

    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    implementation(libs.material)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.accompanist.permissions)

    implementation(libs.orbit.core)
    implementation(libs.orbit.viewmodel)
    implementation(libs.orbit.compose)

    implementation(libs.ktxml)

    implementation(libs.timber)

    implementation(libs.zoomable)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)

    androidTestImplementation(libs.compose.junit)
    androidTestImplementation(libs.esspresso)
    androidTestImplementation(libs.junit.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
}

licenseReport {
    generateCsvReport = false
    generateHtmlReport = false
    generateJsonReport = false
    generateTextReport = true

    copyCsvReportToAssets = false
    copyHtmlReportToAssets = false
    copyJsonReportToAssets = false
    copyTextReportToAssets = true
    useVariantSpecificAssetDirs = false
}
