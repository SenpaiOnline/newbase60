plugins {
    kotlin("jvm") version "1.5.20"
    id("me.qoomon.git-versioning") version "4.2.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("io.kotest:kotest-runner-junit5:4.6.0")
}

tasks {
    compileKotlin {
        kotlinOptions.languageVersion = "1.2"
        kotlinOptions.jvmTarget = "6"
    }
    compileTestKotlin {
        kotlinOptions.languageVersion = "1.2"
        kotlinOptions.jvmTarget = "6"
    }
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
    processResources {
        duplicatesStrategy = DuplicatesStrategy.FAIL
    }
}

gitVersioning.apply(closureOf<me.qoomon.gradle.gitversioning.GitVersioningPluginConfig> {
    branch(closureOf<me.qoomon.gradle.gitversioning.GitVersioningPluginConfig.VersionDescription> {
        pattern = "main"
        versionFormat = "\${version}"
    })
    branch(closureOf<me.qoomon.gradle.gitversioning.GitVersioningPluginConfig.VersionDescription> {
        pattern = "feature/(?<feature>.+)"
        versionFormat = "\${feature}-SNAPSHOT"
    })
    branch(closureOf<me.qoomon.gradle.gitversioning.GitVersioningPluginConfig.VersionDescription> {
        pattern = "pull/(.+)"
        versionFormat = "\${branch}-SNAPSHOT"
    })
    tag(closureOf<me.qoomon.gradle.gitversioning.GitVersioningPluginConfig.VersionDescription> {
        pattern = "v(?<tagVersion>[0-9].*)"
        versionFormat = "\${tagVersion}"
    })
})
