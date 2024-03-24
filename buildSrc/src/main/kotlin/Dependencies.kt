object Dependencies {
    object AndroidX {
        const val activityCompose = "androidx.activity:activity-compose:${Versions.AndroidX.activityCompose}"
        const val browser = "androidx.browser:browser:${Versions.AndroidX.browser}"
        const val composeBom = "androidx.compose:compose-bom:${Versions.AndroidX.composeBom}"
        const val composeMaterial3 = "androidx.compose.material3:material3:${Versions.AndroidX.composeMaterial3}"
        const val composeMaterial3PullRequest = "me.omico.lux:lux-androidx-compose-material3-pullrefresh"
        const val composeRuntimeLivedata = "androidx.compose.runtime:runtime-livedata:${Versions.AndroidX.composeRuntimeLivedata}"
        const val composeUI = "androidx.compose.ui:ui:${Versions.AndroidX.composeUI}"
        const val composeUIGraphics = "androidx.compose.ui:ui-graphics:${Versions.AndroidX.composeUIGraphics}"
        const val composeUIToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.AndroidX.composeUIToolingPreview}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.AndroidX.coreKtx}"
        const val datastorePreference = "androidx.datastore:datastore-preferences:${Versions.AndroidX.datastorePreference}"
        const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.AndroidX.hiltNavigationCompose}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.AndroidX.lifecycle}"
        const val lifecycleViewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.AndroidX.lifecycle}"
        const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidX.lifecycle}"
        const val livedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.AndroidX.livedataKtx}"
        const val navigationCompose = "androidx.navigation:navigation-compose:${Versions.AndroidX.navigationCompose}"
        const val pagingCommon = "androidx.paging:paging-common:${Versions.AndroidX.paging}"
        const val pagingCompose = "androidx.paging:paging-compose:${Versions.AndroidX.paging}"
        const val pagingRuntimeKtx = "androidx.paging:paging-runtime:${Versions.AndroidX.paging}"
    }

    object Etc {
        const val glide = "com.github.bumptech.glide:glide:${Versions.Etc.glide}"
        const val glideAnnotation = "com.github.bumptech.glide:annotations:${Versions.Etc.glide}"
        const val glideOkhttp3 = "com.github.bumptech.glide:okhttp3-integration:${Versions.Etc.glide}"
        const val glideCompose = "com.github.bumptech.glide:compose:${Versions.Etc.glideCompose}"
        const val okhttp3 = "com.squareup.okhttp3:okhttp:${Versions.Etc.okhttp3}"
        const val okhttp3Logging = "com.squareup.okhttp3:logging-interceptor:${Versions.Etc.okhttp3}"
        const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.Etc.retrofit2}"
        const val retrofit2Gson = "com.squareup.retrofit2:converter-gson:${Versions.Etc.retrofit2}"

        const val glideKsp = "com.github.bumptech.glide:ksp:${Versions.Etc.glide}"
    }

    object Google {
        const val hilt = "com.google.dagger:hilt-android:${Versions.Google.hilt}"
        const val material = "com.google.android.material:material:${Versions.Google.material}"
        const val ossLicense = "com.google.android.gms:play-services-oss-licenses:${Versions.Google.ossLicense}"
        const val ossLicensePlugin = "com.google.android.gms:oss-licenses-plugin:${Versions.Google.ossLicensePlugin}"

        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.Google.hilt}"
    }

    object Kotlin {
        const val kotlinBom = "org.jetbrains.kotlin:kotlin-bom:${Versions.Kotlin.kotlinBom}"
        const val kotlinCoroutineAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.kotlinCoroutine}"
        const val kotlinCoroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.kotlinCoroutine}"
    }

    object Debug {
        const val composeUITestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.Debug.composeUITestManifest}"
        const val composeUITooling = "androidx.compose.ui:ui-tooling:${Versions.Debug.composeUITooling}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.Test.junit}"
    }

    object AndroidTest {
        const val composeBom = "androidx.compose:compose-bom:${Versions.AndroidTest.composeBom}"
        const val composeJunit = "androidx.compose.ui:ui-test-junit4:${Versions.AndroidTest.composeJunit}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.AndroidTest.espresso}"
        const val junit = "androidx.test.ext:junit:${Versions.AndroidTest.junit}"
    }
}