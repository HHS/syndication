package com.ctacorp.syndication.jobs


class PeriodicalScanJob {
    def periodicalService

    static triggers = {
        cron name: 'periodicalScanTrigger', cronExpression: "0 0 0 * * ?" //Every day
    }

    def execute() {
        log.info("Periodical scan initiated.")
        periodicalService.updateDailies()
        periodicalService.updateMonthlies()
        periodicalService.updateWeeklies()
    }
}
