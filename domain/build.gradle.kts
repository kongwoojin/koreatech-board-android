@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.koreatechboard.library)
    alias(libs.plugins.koreatechboard.hilt)
    alias(libs.plugins.koreatechboard.firebase)
    alias(libs.plugins.ktlint)
}

android {
    defaultConfig {
        targetSdk = 34

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
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
    namespace = "com.kongjak.koreatechboard.domain"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.paging.common)

    implementation(libs.coroutine.android)

    testImplementation(libs.junit)

    androidTestImplementation(libs.esspresso)
    androidTestImplementation(libs.junit.test)
}
