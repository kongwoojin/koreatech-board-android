import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

val localStoreFile: String = gradleLocalProperties(rootDir).getProperty("localStoreFile")
val localStorePassword: String = gradleLocalProperties(rootDir).getProperty("localStorePassword")
val localKeyAlias: String = gradleLocalProperties(rootDir).getProperty("localKeyAlias")
val localKeyPassword: String = gradleLocalProperties(rootDir).getProperty("localKeyPassword")

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.koreatechboard.application)
    alias(libs.plugins.koreatechboard.firebase)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.google.services)
    alias(libs.plugins.serialization)
    alias(libs.plugins.licenses)
    kotlin("plugin.parcelize")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    androidTarget {
        compilations.configureEach {
            compilerOptions.configure {
                jvmTarget.set(
                    org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
                )
            }
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {

            implementation(libs.accompanist.permissions)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.browser)
            implementation(libs.androidx.webkit)
            implementation(libs.firebase.analytics)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.firebase.messaging)
            implementation(libs.firebase.crashlytics)
            implementation(libs.koin.android)
            implementation(libs.material)
            implementation(libs.paging.androidx.compose)
            implementation(libs.paging.androidx.runtime)
            implementation(libs.sqldelight.android.driver)

            implementation("androidx.compose.material:material-ripple:1.7.0-alpha05")
        }

        commonMain.dependencies {

            api(project(":data"))
            api(project(":domain"))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.animation)
            implementation(compose.components.resources)

            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.coil)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.okhttp)
            implementation(libs.coroutine.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.serialization)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.negotiation)
            implementation(libs.ktor.kotlinx.serialization)
            implementation(libs.ktxml)
            implementation(libs.material3.window.size.multiplatform)
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.napier)
            implementation(libs.orbit.core)
            implementation(libs.paging.common)
            implementation(libs.paging.compose.common)
            implementation(libs.sqldelight.coroutines.extensions)
            implementation(libs.zoomable)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)

            implementation(libs.compose.material3.desktop)
            implementation(libs.coroutine.swing)
            implementation(libs.kcef)
            implementation(libs.ktor.engine.cio)
            implementation(libs.sqldelight.sqlite.driver)
        }
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

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

compose.desktop {
    application {
        mainClass = "com.kongjak.koreatechboard.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            modules("java.sql")
            packageName = "Koreatech Board"
            packageVersion = libs.versions.project.version.name.get()
            description = "A desktop client of Koreatech Board"
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            includeAllModules = true
            licenseFile.set(rootProject.file("LICENSE"))

            macOS {
                iconFile.set(project.file("icon.icns"))
            }
            windows {
                iconFile.set(project.file("icon.ico"))
                perUserInstall = true
                shortcut = true
                // See: https://wixtoolset.org/docs/v3/howtos/general/generate_guids/
                upgradeUuid = "d9530008-389b-40e5-85d3-89cbfb59eaf1"
            }
            linux {
                iconFile.set(project.file("icon.png"))
                shortcut = true
            }
        }
    }
}

afterEvaluate {
    tasks.withType<JavaExec> {
        jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
        jvmArgs(
            "--add-opens",
            "java.desktop/java.awt.peer=ALL-UNNAMED"
        )

        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
            jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
        }
    }
}

licenseReport {
    generateCsvReport = false
    generateHtmlReport = false
    generateJsonReport = false
    generateTextReport = true

    copyCsvReportToAssets = false
    copyHtmlReportToAssets = false
    copyJsonReportToAssets = false
    copyTextReportToAssets = false
    useVariantSpecificAssetDirs = false
}

ktlint {
    filter {
        exclude { element ->
            element.file.path.contains("generated")
        }
    }
}
