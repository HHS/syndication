eventCompileStart = { msg ->
    def hash = {->def proc = "git log -n 1".execute(); proc.waitFor(); (proc.in.text =~ /commit\s+(.+)/)[0][1]}()[32..39]
    def date = {->def proc = "git log -n 1".execute(); proc.waitFor(); (proc.in.text =~ /Date:\s+(\w{3}\s\w{3}.+\d{4})\s[-+]\d+/)[0][1]}()

    File metaDataFile = new File("web-app/WEB-INF/MetaData.groovy")
    String metaData = "app.buildHash = '$hash'\napp.lastGitCommitDate = '$date'\napp.buildDate = '${new Date().format('EEE MMM d HH:mm:ss yyyy')}'"
    metaDataFile.write(metaData)
    // Ants Project is available via: kind.ant.project
    executeGruntTasks()
    // Obtain status and output
}

private void executeGruntTasks(){
    println "| Load js dependencies from cache..."
    def npmInstall = ["bash","-c","npm install"].execute()  // execute default task to load dependencies from local cache.
    npmInstall.waitFor()
    def proc = ["bash","-c","grunt"].execute()  // execute default task to load dependencies from local cache.
    proc.waitFor()
    if(proc.exitValue()!=0){
        println "| Error occured while loading dependencies from local cache : ${proc.err.text}"
        println "| Try loading dependencies from web..."
        proc = ["bash", "-c", "grunt webInstall"].execute()
        proc.waitFor()                               // Wait for the command to finish
        println "Output: ${proc.in.text}"
    }
}