plugins {
    java
    application
    id("org.springframework.boot") version "3.5.9"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.13.0"
    id("org.flywaydb.flyway") version "10.20.1"
}

group = "br.com.agibank.customers"
version = "0.0.1-SNAPSHOT"
description = "Customer management API"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2025.0.1"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/src/main/resources/spec/customer_management_api-v1.yaml")
    outputDir.set("$rootDir/build/generated/sources/openapi/v1")
    apiPackage.set("br.com.agibank.customers.api.v1")
    modelPackage.set("br.com.agibank.customers.api.v1.model")
    generateApiTests.set(false)
    generateModelTests.set(false)
    modelNameSuffix = "DTO"
    apiNameSuffix = "ApiV1"
    skipValidateSpec.set(false)
    configOptions.set(
        mapOf(
            "library" to "spring-boot",
            "useSpringBoot3" to "true",
            "serializationLibrary" to "jackson",
            "dateLibrary" to "java8",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "useResponseEntity" to "true",
            "returnSuccessCode" to "true",
            "openApiNullable" to "true",
            "documentationProvider" to "springdoc",
        )
    )
}

sourceSets {
    main {
        java {
            srcDir("$rootDir/build/generated/sources/openapi/v1/src/main/java")
        }
    }
}
application {
    mainClass = "br.com.agibank.customers.CustomersApplication"
}

tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.named<Jar>("jar") {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
}
