plugins {
    id("java")
    id("maven-publish")
}

group = "nx.peter.api.json"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("com.github.Petersburg2020:Data:1.0.8-beta")
    implementation("org.jetbrains:annotations:24.0.0")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "nx.peter.api.json"
                artifactId = "JsonApi"
                version = "1.0-SNAPSHOT"

                from(components["java"])
            }
        }
        /*repositories {
            maven {
                // change to point to your repo, e.g. http://my.org/repo
                url = uri(layout.buildDirectory.dir("https://github.com/Petersburg2020/JsonApi"))
            }
        }*/
    }
}

