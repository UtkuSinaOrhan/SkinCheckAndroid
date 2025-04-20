plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

}

buildscript {
    repositories {
        google()
        mavenCentral()

    }
    dependencies {
        // Sadece bir tane sürümü kullanın
        classpath("com.google.gms:google-services:4.4.2")  // 4.4.2 sürümü ile uyumlu

    }
}
