import java.net.URL

plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("org.jetbrains.dokka") version "1.5.31"
    id("io.codearte.nexus-staging") version "0.30.0"
}


tasks.dokkaJavadoc.configure {
    outputDirectory.set(File("C:\\Users\\Administrator\\Desktop\\Doc\\itemsystem"))
    dokkaSourceSets {
        configureEach {
            externalDocumentationLink {
                url.set(URL("https://doc.skillw.com/pouvoir/"))
            }
        }
    }

}
val api: String? by project
val order: String? by project

task("versionModify") {
    project.version = project.version.toString() + (order?.let { "-$it" } ?: "")
}

task("versionAddAPI") {
    if (api == null) return@task
    val origin = project.version.toString()
    project.version = "$origin-api"
}


task("releaseName") {
    println(project.name + "-" + project.version)
}

task("version") {
    println(project.version.toString())
}


taboolib {
    if (project.version.toString().contains("-api")) {
        options("skip-kotlin-relocate")
    }
    description {
        contributors {
            name("Glom_")
        }
        dependencies {
            name("Pouvoir")
            name("MythicMobs").optional(true)
            name("AttributeSystem").optional(true)
            name("BuffSystem").optional(true)

        }
    }
    install("common")
    install("module-chat")
    install("module-configuration")
    install("common-5")
    install("module-database")
    install("module-effect")
    install("module-nms-util")
    install("module-lang")
    install("module-metrics")
    install("platform-bukkit")
    install("module-nms")
    classifier = null
    version = "6.0.12-69"
}

repositories {
    mavenCentral()
    maven { url = uri("https://mvn.lumine.io/repository/maven-public/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.10")

    compileOnly("ink.ptms:nms-all:1.0.0")

//    compileOnly("ink.ptms.core:v12004-minimize:universal")

    compileOnly("io.lumine:Mythic-Dist:5.0.3")
    compileOnly("com.github.LoneDev6:api-itemsadder:3.0.0")

    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))

    taboo("ink.ptms:um:1.0.0-beta-18")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.register<Jar>("buildJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

tasks.register<Jar>("buildSourcesJar") {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

publishing {
    repositories {
        maven {
            url = if (project.version.toString().contains("-SNAPSHOT")) {
                uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
            } else {
                uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            }
            credentials {
                username = project.findProperty("username").toString()
                password = project.findProperty("password").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")

            }
        }
        mavenLocal()
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifact(tasks["buildJavadocJar"])
            artifact(tasks["buildSourcesJar"])
            version = project.version.toString()
            groupId = project.group.toString()
            pom {
                name.set(project.name)
                description.set("Bukkit Attribute Engine Plugin.")
                url.set("https://github.com/Glom-c/AttributeSystem/")

                dependencies {
//                    compileOnly("com.skillw.pouvoir:Pouvoir:1.6.4-8")
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/Glom-c/AttributeSystem/blob/main/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("Skillw")
                        name.set("Glom_")
                        email.set("glom@skillw.com")
                    }
                }
                scm {
                    connection.set("scm:git:git:https://github.com/Glom-c/AttributeSystem.git")
                    developerConnection.set("scm:git:ssh:https://github.com/Glom-c/AttributeSystem.git")
                    url.set("https://github.com/Glom-c/AttributeSystem.git")
                }
            }
        }
    }
}

nexusStaging {
    serverUrl = "https://s01.oss.sonatype.org/service/local/"
    username = project.findProperty("username").toString()
    password = project.findProperty("password").toString()
    packageGroup = "com.skillw"
}

signing {
    sign(publishing.publications.getAt("library"))
}