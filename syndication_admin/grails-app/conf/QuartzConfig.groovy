quartz {
    autoStartup = true
    jdbcStore = false
    waitForJobsToCompleteOnShutdown = false
    exposeSchedulerInRepository = false

    props {
        scheduler.skipUpdateCheck = true
    }
}

environments {
    test {
        quartz {
            autoStartup = false
        }
    }
}
