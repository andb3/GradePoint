// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven("https://dl.bintray.com/ekito/koin")
    }
    dependencies {
        classpath(Deps.SqlDelight.gradle)
        classpath(Deps.cocoapodsext)
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}")

        classpath(kotlin("gradle-plugin", Versions.kotlin))
        classpath("com.android.tools.build:gradle:4.2.0-alpha09")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        maven(url = "https://kotlin.bintray.com/kotlin-js-wrappers/")
        maven(url = "https://dl.bintray.com/touchlabpublic/kotlin")
        maven(url = "https://dl.bintray.com/netguru/maven/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        maven(url = "https://www.jitpack.io")
        maven("https://dl.bintray.com/cfraser/muirwik")
        maven("https://dl.bintray.com/ekito/koin")
    }
}

/*
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
*/
