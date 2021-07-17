plugins {
    `maven-publish`
    signing
    kotlin("jvm") version "1.5.21"
    id("me.qoomon.git-versioning") version "4.2.1"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("org.jetbrains.dokka") version "1.5.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("io.kotest:kotest-runner-junit5:4.6.1")
    testImplementation("io.kotest:kotest-framework-datatest:4.6.1")
    testImplementation("io.kotest:kotest-property:4.6.1")
}

tasks {
    compileKotlin {
        kotlinOptions.languageVersion = "1.3"
        kotlinOptions.jvmTarget = "1.6"
    }
    compileTestKotlin {
        kotlinOptions.languageVersion = "1.3"
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
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

    val sourcesJar: TaskProvider<Jar> by registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
    val javadocJar: TaskProvider<Jar> by registering(Jar::class) {
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaHtml)
    }
    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
        archives(jar)
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

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            val ossrhUsername: String? by project
            val ossrhPassword: String? by project
            username.set(ossrhUsername)
            password.set(ossrhPassword)
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                packaging = "jar"
                name.set("newbase60")
                description.set("Yet another implementation of NewBase60.")
                url.set("https://github.com/SenpaiOnline/newbase60")
                scm {
                    connection.set("scm:git:ssh://github.com/SenpaiOnline/newbase60.git")
                    developerConnection.set("scm:git:ssh://github.com/SenpaiOnline/newbase60.git")
                    url.set("https://github.com/SenpaiOnline/newbase60")
                }
                issueManagement {
                    system.set("github")
                    url.set("https://github.com/SenpaiOnline/newbase60/issues")
                }
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("aiscy")
                        name.set("Maxim Pavlov")
                        email.set("maxpav137@gmail.com")
                    }
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["mavenJava"])
}
