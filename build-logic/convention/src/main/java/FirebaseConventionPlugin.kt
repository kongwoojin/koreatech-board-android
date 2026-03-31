import com.android.build.api.dsl.LibraryExtension
import com.kongjak.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


internal class FirebaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.firebase.crashlytics")
            }
        }
    }
}
