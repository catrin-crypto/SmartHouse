// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    dependencies{
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.10")
    }
}
plugins {
    id("com.android.application") version "8.0.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("io.realm.kotlin") version "1.11.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21" apply false
}