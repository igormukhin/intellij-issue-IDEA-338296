import com.google.protobuf.gradle.GenerateProtoTask

plugins {
    id("java")
    id("com.google.protobuf").version("0.9.4")
    id("org.jsonschema2pojo").version("1.2.1")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.fasterxml.jackson.core:jackson-annotations:2.16.0")
    compileOnly("com.google.protobuf:protobuf-java:3.25.0")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.0"
    }
}

jsonSchema2Pojo {
    targetDirectory = layout.buildDirectory.dir("generated/sources/json").get().asFile
    setSource(files("$projectDir/src/main/resources/schemas"))
    targetPackage = "com.mypackage.json"
    generateBuilders = true
    includeAdditionalProperties = false
    setAnnotationStyle("JACKSON2")
}

// The presence of jsonschema2pojo plugin causes the default configuration of the protobuf not to work:
// the "generateProto" does not get executed automatically anymore.
// So we need to add it manually.
tasks.withType<ProcessResources>().configureEach {
    dependsOn(tasks.withType<GenerateProtoTask>())
}
