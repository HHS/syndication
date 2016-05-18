package com.ctacorp.syndication.jobs

import com.ctacorp.syndication.authentication.User

class InactiveAccountScanJob {
    static triggers = {
        cron name: 'inactiveAccountScanJob', cronExpression: "0 0 0 ? * *" //Every night at midnight
    }

    def adminUserService

    def execute() {
        log.info "running account scan job"
        Date lastYear = new Date() - 365

        User.findAllByLastLoginLessThan(lastYear).each{ user ->
            log.info "Inactive User found: ${user.username}, lastLogin: ${user.lastLogin}. Deleting..."
            adminUserService.deleteUser(user)
        }
    }
}