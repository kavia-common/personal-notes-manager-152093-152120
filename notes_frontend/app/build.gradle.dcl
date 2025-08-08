androidApplication {
    namespace = "org.example.app"

    /*
     * Ensure the google() repository is specified globally (see settings.gradle.dcl or at top-level).
     * Compose dependencies must use the latest stable versions available on Maven Central/Google (1.6.1).
     * The Compose compiler plugin remains at 1.6.10 for Kotlin 2 compatibility.
     */

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))

        // Jetpack Compose dependencies (latest available stable is 1.6.1)
        implementation("androidx.activity:activity-compose:1.9.0")
        implementation("androidx.compose.ui:ui:1.6.1")
        implementation("androidx.compose.ui:ui-tooling-preview:1.6.1")
        implementation("androidx.compose.material3:material3:1.2.1")
        implementation("androidx.compose.foundation:foundation:1.6.1")
        implementation("androidx.compose.runtime:runtime-livedata:1.6.1")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
        implementation("androidx.compose.material:material-icons-extended:1.6.1")
        implementation("androidx.compose.material:material-icons-core:1.6.1")
        // Compose tooling dependencies for preview/debug are not supported via 'debugImplementation' in .dcl; do not add.
    }
}
