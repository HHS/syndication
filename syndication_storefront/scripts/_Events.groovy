// eventCreateWarStart = { warName, stagingDir ->
//    if (grailsEnv == "production") {
//        def sharedLibsDir = "${grailsSettings.projectWorkDir}/sharedLibs"

//        ant.mkdir dir: sharedLibsDir
//        ant.move todir: sharedLibsDir, {
//            fileset dir: "${stagingDir}/WEB-INF/lib", {
//                include name: "*.jar"
//                exclude name: "grails-*"
//            }
//        }

//        println "Shared JARs put into ${sharedLibsDir}"
//    }
// }