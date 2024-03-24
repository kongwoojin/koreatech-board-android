import org.gradle.api.JavaVersion

object Versions {
    object Android {
        const val application = "8.1.1"
        const val library = "8.1.1"
    }

    object AndroidX {
        const val activityCompose = "1.7.2"
        const val browser = "1.6.0"
        const val composeBom = "2023.03.00"
        const val composeMaterial3 = "1.2.0-alpha04"
        const val composeRuntimeLivedata = "1.5.0"
        const val composeUI = "1.5.0"
        const val composeUIGraphics = "1.5.0"
        const val composeUIToolingPreview = "1.5.0"
        const val coreKtx = "1.10.1"
        const val datastorePreference = "1.0.0"
        const val hiltNavigationCompose = "1.1.0-alpha01"
        const val lifecycle = "2.6.1"
        const val livedataKtx = "2.6.1"
        const val navigationCompose = "2.7.1"
        const val paging = "3.2.0"
    }

    object Etc {
        const val glide = "4.16.0"
        const val glideCompose = "1.0.0-beta01"
        const val okhttp3 = "4.10.0"
        const val retrofit2 = "2.9.0"
    }

    object Google {
        const val hilt = "2.46.1"
        const val material = "1.9.0"
        const val ossLicense = "17.0.1"
        const val ossLicensePlugin = "0.10.6"
    }

    object Hilt {
        const val hilt = "2.43.2"
    }

    object Kotlin {
        const val kotlin = "1.8.10"
        const val kotlinBom = "1.8.0"
        const val kotlinCompilerExtension = "1.4.3"
        const val kotlinCoroutine = "1.7.1"
    }

    object Debug {
        const val composeUITooling = "1.5.0"
        const val composeUITestManifest = "1.5.0"
    }

    object Test {
        const val junit = "4.13.2"
    }

    object AndroidTest {
        const val composeBom = "2023.03.00"
        const val composeJunit = "1.4.3"
        const val espresso = "3.5.1"
        const val junit = "1.1.5"
    }

    const val ksp = "1.8.10-1.0.9"
    const val ktlint = "11.5.1"
}