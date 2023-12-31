import org.gradle.internal.impldep.com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential.ACCESS_TOKEN
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "io.keiji.sample.mastodonclient"
    compileSdk = 33

    buildFeatures {
        dataBinding = true
        buildConfig = true
        viewBinding = true
    }

    defaultConfig {
        applicationId = "io.keiji.sample.mastodonclient"
        minSdk = 19
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true

        val prop = Properties().apply {
            val file =project.rootProject.file("instance.properties")
            if (!file.exists()){
                file.createNewFile()
            }
            val stream = FileInputStream(file)
            load(stream)
        }
        val INSTANCE_URL = prop.getProperty("instance_url") ?: ""
        val USERNAME = prop.getProperty("username") ?: ""
        val ACCESS_TOKEN = prop.getProperty("access_token") ?: ""
        val CLIENT_KEY = prop.getProperty("client_key") ?: ""
        val CLIENT_SECRET = prop.getProperty("client_secret") ?: ""
        val CLIENT_SCOPES = prop.getProperty("client_scopes") ?: ""
        val CLIENT_REDIRECT_URI = prop.getProperty("client_redirect_uri") ?: ""

        buildConfigField("String", "INSTANCE_URL", "\"${INSTANCE_URL}\"")
        buildConfigField("String", "USERNAME", "\"${USERNAME}\"")
        buildConfigField("String", "ACCESS_TOKEN", "\"${ACCESS_TOKEN}\"")
        buildConfigField("String", "CLIENT_KEY", "\"${CLIENT_KEY}\"")
        buildConfigField("String", "CLIENT_SECRET", "\"${CLIENT_SECRET}\"")
        buildConfigField("String", "CLIENT_SCOPES", "\"${CLIENT_SCOPES}\"")
        buildConfigField("String", "CLIENT_REDIRECT_URI", "\"${CLIENT_REDIRECT_URI}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.2.3")
    implementation("com.squareup.retrofit2:retrofit:2.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.3")
    implementation("com.squareup.moshi:moshi-kotlin:1.9.2")
    implementation("com.squareup.retrofit2:converter-moshi:2.7.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("com.github.bumptech.glide:glide:4.10.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    kapt("com.github.bumptech.glide:compiler:4.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}