plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    //testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    // https://mvnrepository.com/artifact/org.testng/testng
      implementation("org.testng:testng:7.4.0")
    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20211205")
}

defaultTasks("cleaning", "running")

tasks.register("cleaning"){
    doLast{
        println("Cleaning Task running !!!!!")
    }
}

tasks.register("running"){
    doLast{
        println("Running  Task Works !")
    }
}

tasks.register("deploy"){
    doLast{
        println("Deploying !! !")
    }
}



tasks.getByName<Test>("test") {
    //useJUnitPlatform()
    useTestNG()
}

tasks.register("hello"){
    doLast{
        println("Hello form ")
    }
}


tasks.register("gradle"){
    dependsOn("hello")
    doLast{
        println("Gradle !!!!!!!")
    }
}
