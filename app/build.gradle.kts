@file:Suppress("DEPRECATION")

plugins {
    alias(libs.plugins.androidApplication)
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    flavorDimensions("FirmliTest")

    productFlavors {
        create("Firmli") {
            dimension = "FirmliTest"
            applicationId = "com.student.firmli"
            versionName = "1.0.6"
            versionCode = 6
            resValue("string", "app_name", "Firmli")
            buildConfigField("String", "BASE_URL", "\"https://c101b24stone10-api.firmli.com/v1/\"")
        }

        create("Admisiony") {
            dimension = "FirmliTest"
            applicationId = "com.student.Admissiony"
            versionName = "1.0.9"
            versionCode = 9
            resValue("string", "app_name", "Admissiony.com")
            buildConfigField("String", "BASE_URL", "\"https://c120h25steps19-api.firmli.com/v1\"")
        }

    }

    namespace = "com.student.Compass_Abroad"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true

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

tasks.whenTaskAdded {
    if (name.contains("process", ignoreCase = true) && name.contains(
            "GoogleServices",
            ignoreCase = true
        )
    ) {
        doFirst {
            val flavor = name.split("GoogleServices")[0]
                .removePrefix("process")
                .replace("Debug", "")
                .replace("Release", "")
                .trim()

            val srcFile = file("google-services-${flavor}.json")
            val destFile = file("google-services.json")

            if (srcFile.exists()) {

                srcFile.copyTo(destFile, overwrite = true)

            } else {

                throw GradleException("Missing google-services.json file for flavor: $flavor")
            }
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation("androidx.core:core-ktx:1.13.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    // socket io

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("io.socket:socket.io-client:2.0.0") {
        "exclude group: 'org.json', module: 'json'"
    }
    //UI Stuff
    implementation(libs.circleimageview)
    implementation(libs.ccp)
    implementation(libs.imagepicker)


    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    //glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    //validation for phone

    implementation("com.googlecode.libphonenumber:libphonenumber:8.2.0")
    implementation("com.github.pgreze:android-reactions:1.6")
    implementation("com.razorpay:checkout:1.6.33")
    implementation("com.daimajia.androidanimations:library:2.4@aar")

    implementation("me.relex:circleindicator:2.1.6")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //stripe
    implementation(libs.compressor)
    implementation("com.github.miteshpithadiya:SearchableSpinner:master")
    implementation(libs.stripe.java)
    implementation(libs.stripe.android)
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")


    // ExoPlayer (for normal URLs)
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
    implementation("androidx.media3:media3-exoplayer-hls:1.4.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.4.1")
// YouTube Player (for YouTube URLs)
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:13.0.0")

    // firebase

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.core)
    implementation(libs.google.firebase.crashlytics)

    implementation(libs.google.firebase.analytics)
    implementation(libs.app.update.ktx)

}
