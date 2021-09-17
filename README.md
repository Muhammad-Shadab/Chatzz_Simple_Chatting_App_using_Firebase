# Chatzz_Simple_Chatting_App_using_Firebase
Hello this project is about creating a Chatting app using Firebase.

//Required dependencies



plugins {
    id 'com.android.application'
    id 'com.google.gms.google-services'
}

android {
    compileSdk 31

    defaultConfig {
        applicationId "com.example.chatzz"
        minSdk 28
        targetSdk 31
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation 'androidx.appcompat:appcompat:1.3.1'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.0'
    implementation 'com.google.firebase:firebase-auth:21.0.1'
    implementation 'com.google.firebase:firebase-database:20.0.1'
    implementation 'com.google.firebase:firebase-storage:20.0.0'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
    implementation 'de.hdodenhof:circleimageview:3.1.0'
    implementation 'com.squareup.picasso:picasso:2.71828'

        // ...

        // Import the Firebase BoM
        implementation platform('com.google.firebase:firebase-bom:28.4.1')

        // When using the BoM, you don't specify versions in Firebase library dependencies

        // Declare the dependency for the Firebase SDK for Google Analytics
        implementation 'com.google.firebase:firebase-analytics'

        // Declare the dependencies for any other desired Firebase products
        // For example, declare the dependencies for Firebase Authentication and Cloud Firestore
        implementation 'com.google.firebase:firebase-auth'
        implementation 'com.google.firebase:firebase-firestore'
    }


// You need to create Firebase Account and connect this app using firebase.

<img src="https://user-images.githubusercontent.com/90592402/133766059-f7c66fb0-fa04-41fb-a857-d10ae5996068.png" width="250" height="400" />

<img src="https://user-images.githubusercontent.com/90592402/133766067-68b95da1-d563-4f40-b9ce-4e6cef682c97.png" width="250" height="400" />

<img src="https://user-images.githubusercontent.com/90592402/133766069-cae4ce40-77f1-49d7-a9f4-a48739cf2522.png" width="250" height="400" />

<img src="https://user-images.githubusercontent.com/90592402/133766072-54ee6bee-ba37-47b5-ad3d-a76a2aa968a2.png" width="250" height="400" />

<img src="https://user-images.githubusercontent.com/90592402/133766095-394bc650-5607-487e-b61d-1d1cb18270b9.png" width="250" height="400" />

<img src="https://user-images.githubusercontent.com/90592402/133766102-4e37069e-14cc-4a86-b00f-3441a152ee65.png" width="250" height="400" />

<img src="https://user-images.githubusercontent.com/90592402/133766105-ee2be98a-64c7-40b0-ab42-b7026da5b8d6.png" width="250" height="400" />










