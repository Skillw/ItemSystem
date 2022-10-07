import java.net.URL

plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.42"
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("org.jetbrains.dokka") version "1.6.10"
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

taboolib {
//    options("skip-kotlin-relocate")
    description {
        contributors {
            name("Glom_")
        }
        dependencies {
            name("Pouvoir")
            name("AttributeSystem").optional(true)
            name("MythicMobs").optional(true)
            name("BuffSystem").optional(true)

        }
    }
    install("common")
    install("module-chat")
    install("common-5")
    install("module-configuration")
    install("module-database")
    install("module-effect")
    install("module-nms-util")
    install("module-lang")
    install("platform-bukkit")
    install("module-metrics")
    install("module-nms")
    install("module-ui")
    classifier = null
    version = "6.0.9-111"
}

repositories {
    mavenCentral()
    maven { url = uri("https://mvn.lumine.io/repository/maven-public/") }
}

dependencies {
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.10")
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("com.google.code.gson:gson:2.9.0")
    compileOnly("io.lumine:Mythic-Dist:5.0.3")

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

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}