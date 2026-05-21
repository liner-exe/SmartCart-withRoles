plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "com.liner_exe.smartcart"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.liner_exe.smartcart"
        minSdk = 29
        targetSdk = 35
        versionCode = 12
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    applicationVariants.all {
        val variant = this
        variant.outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            if (variant.buildType.name == "release") {
                val apkName = "SmartCart-v${variant.versionName}.apk"
                output.outputFileName = apkName
            }

            if (variant.buildType.name == "debug") {
                val apkName = "SmartCart-v${variant.versionName}-debug.apk"
                output.outputFileName = apkName
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.fragment)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)

    implementation(libs.rxjava3)
    implementation(libs.rxandroid)
    implementation(libs.room.rxjava3)

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    implementation(libs.androidx.emoji2)
    implementation(libs.androidx.emoji2.views)
    implementation(libs.androidx.emoji2.emojipicker)
    implementation(libs.androidx.emoji2.bundled)

    implementation(libs.splashscreen)

    implementation(libs.mp.android.chart)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}