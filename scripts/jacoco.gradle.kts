apply<JacocoPlugin>()


configure<JacocoPluginExtension> {
    toolVersion = "0.8.2"
}

task<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
    sourceDirectories.setFrom(
        files("${project.projectDir}/src/main/java")
    )
    classDirectories.setFrom(
        fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
            exclude(
                setOf(
                    "**/R.class",
                    "**/R$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    "**/*Test*.*",
                    "android/**/*.*",
                    "com/andrewgiang/homecontrol/dagger/**"
                )
            )
        }
    )
    executionData.setFrom(
        fileTree("${project.buildDir}") {
            include("jacoco/testDebugUnitTest.exec")
        })
}