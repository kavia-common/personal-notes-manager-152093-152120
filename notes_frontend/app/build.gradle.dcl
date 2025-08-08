androidApplication {
    namespace = "org.example.app"

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))

        // Compose BOM to align all Compose library versions
        implementation(platform("androidx.compose:compose-bom:2023.10.01"))
        implementation("androidx.activity:activity-compose:1.8.2")
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.runtime:runtime-livedata")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
        implementation("androidx.compose.foundation:foundation")
        implementation("androidx.compose.material:material-icons-extended")
        implementation("androidx.compose.material:material-icons-core")
    }
}
