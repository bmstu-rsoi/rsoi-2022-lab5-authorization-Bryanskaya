plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.liquibase:liquibase-core")
    implementation("org.hibernate:hibernate-gradle-plugin:5.6.11.Final")
    implementation("org.hibernate:hibernate-validator:8.0.0.Final")
    implementation("javax.validation:validation-api:2.0.1.Final")

    implementation("org.postgresql:postgresql")

    //LOMBOK Dependencies
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.h2database:h2")
}

tasks {
    test {
        useJUnitPlatform()
    }
}