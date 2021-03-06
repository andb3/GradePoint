import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

val loginProperties = Properties().apply {
    load(
        FileInputStream(rootProject.file("login.properties"))
    )
}

android {
    compileSdkVersion(Versions.compile_sdk)
    defaultConfig {
        applicationId = "com.andb.apps.aspen"
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = 10
        versionName = "0.2.0-internal02"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "app_name", "GradePoint")
    }
    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            buildConfigField("String", "TEST_USERNAME", "")
            buildConfigField("String", "TEST_PASSWORD", "")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            resValue("string", "app_name", "GradePoint Beta")
            buildConfigField("String", "TEST_USERNAME", loginProperties["USERNAME"] as String)
            buildConfigField("String", "TEST_PASSWORD", loginProperties["PASSWORD"] as String)
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }
    buildToolsVersion = "30.0.0"

    composeOptions {
        kotlinCompilerVersion = "1.4.0"
        kotlinCompilerExtensionVersion = Versions.jetpackCompose
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/NOTICE")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE.txt")
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(Deps.material_x)
    implementation(Deps.app_compat_x)
    implementation(Deps.core_ktx)
    implementation(Deps.Ktor.androidCore)
    implementation(Deps.constraintlayout)
    implementation(Deps.SqlDelight.runtimeJdk)
    implementation(Deps.SqlDelight.driverAndroid)
    implementation(Deps.Koin.core)
    implementation(Deps.Koin.viewModel)
    testImplementation(Deps.junit)
    implementation(Deps.JetpackCompose.runtime)
    implementation(Deps.JetpackCompose.core)
    implementation(Deps.JetpackCompose.tooling)
    implementation(Deps.JetpackCompose.layout)
    implementation(Deps.JetpackCompose.material)
    implementation(Deps.JetpackCompose.foundation)
    implementation(Deps.JetpackCompose.animation)
    implementation(Deps.JetpackCompose.test)
    implementation(Deps.JetpackCompose.icons)
    implementation(Deps.composeBackstack)
    implementation(Deps.Klock.android)
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += arrayOf("-Xallow-jvm-ir-dependencies", "-Xskip-prerelease-check")
    }
}
