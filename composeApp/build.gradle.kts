import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)

    kotlin("plugin.serialization") version "1.9.21" //decompose step2

    id("app.cash.sqldelight") version "2.0.2"//sqdelight step1
}

kotlin {

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    jvm("desktop")

    //Step1 for Web config
    js(IR){
        moduleName = "kmpApp2"
        browser(){
            // Tool bundler for converting kotlin code to js code
            commonWebpackConfig(){
                outputFileName = "KmpApp2.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy()
            }
            binaries.executable()//it will generate executable js files
        }
    }

//    sourceSets {
//        val wasmJsMain by getting {
//            dependencies {
//                // Import libraries
//                //implementation(compose.html.core)
//                //implementation(compose.runtime)
//            }
//        }
//        val wasmJsTest by getting {
//            dependencies {
//                implementation(kotlin("test-js"))
//            }
//        }
//
//    }


//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        moduleName = "kmpApp2"
//        browser {
//            commonWebpackConfig {
//                outputFileName = "KmpApp2.js"
//            }
//        }
//        binaries.executable()
//    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        val desktopMain by getting
       // val wasmJsMain by getting

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)

            //decompose step3
            implementation("com.arkivanov.decompose:decompose:2.2.2-compose-experimental")
            implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.2.2-compose-experimental")

            implementation("app.cash.sqldelight:android-driver:2.0.1")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.mvvm.core)
            implementation(libs.image.loader)

            //decompose step1
            implementation("com.arkivanov.decompose:decompose:2.2.2-compose-experimental")
            implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.2.2-compose-experimental")

            implementation("org.slf4j:slf4j-simple:2.0.7")

            //Koin step1
            implementation("io.insert-koin:koin-core:3.5.3")


        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)

            implementation("app.cash.sqldelight:sqlite-driver:2.0.1")

        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)

            //Koin step2
            implementation("io.insert-koin:koin-android:3.5.3")
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)

            implementation("com.arkivanov.decompose:decompose:2.2.2-compose-experimental")
            implementation("com.arkivanov.essenty:lifecycle:1.3.0")

            implementation("app.cash.sqldelight:native-driver:2.0.1")
        }

        jsMain.dependencies {

            implementation(npm("@cashapp/sqldelight-sqljs-worker", "2.0.1"))
            implementation(npm("sql.js", "1.8.0"))
            implementation("app.cash.sqldelight:web-worker-driver:2.0.1")
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
        }
    }
}

android {
    namespace = "com.kmp.kmpapp2"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.kmp.kmpapp2"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}
dependencies {
    implementation(libs.androidx.material)

}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.kmp.kmpapp2"
            packageVersion = "1.0.0"
        }
    }
}

//Step3 for Web config
compose.experimental {
    web.application{}
}
sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.kmp.kmpapp2")
            generateAsync.set(true)
        }
    }
}