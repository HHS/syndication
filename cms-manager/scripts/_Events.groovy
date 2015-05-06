eventCompileStart = { msg ->
    def hash = {->def proc = "git log -n 1".execute(); proc.waitFor(); (proc.in.text =~ /commit\s+(.+)/)[0][1]}()[32..39]
    def date = {->def proc = "git log -n 1".execute(); proc.waitFor(); (proc.in.text =~ /Date:\s+(\w{3}\s\w{3}.+\d{4})\s[-+]\d+/)[0][1]}()
    metadata.'app.buildHash' = hash
    metadata.'app.lastGitCommitDate' = date
    metadata.'app.buildDate' = new Date().format("EEE MMM d HH:mm:ss yyyy")
    metadata.persist()
}

