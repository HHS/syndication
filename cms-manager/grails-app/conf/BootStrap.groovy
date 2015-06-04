/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/


import com.ctacorp.syndication.manager.cms.*
import com.ctacorp.syndication.manager.cms.utils.marshalling.KeyAgreementMarshaller
import com.ctacorp.syndication.manager.cms.utils.marshalling.RestSubscriptionMarshaller
import com.ctacorp.syndication.manager.cms.utils.marshalling.SubscriberMarshaller
import grails.converters.JSON
import grails.util.Environment
import grails.util.Holders

class BootStrap {

    def config = Holders.config
    def testConfig = Holders.config.test
    def env = Environment.current

    String adminUsername = config.springsecurity.cmsManager.adminUsername
    String defaultPassword = config.springsecurity.cmsManager.defaultPassword
    String serverUrl = config.grails.serverURL
    boolean createTestDataInProductionMode = config.cmsManager.createTestDataInProductionMode

    def loggingService
    def subscriptionService
    def userSubscriberService

    def init = { servletContext ->
        log.info ("*** CMS Manager is running in --> ${Environment.current} <-- mode. ***")

        showConfig()

        initMarshallers()

        initRoles()
        initAdminUser()

        if (env in [Environment.TEST, Environment.DEVELOPMENT] || createTestDataInProductionMode) {

            createTestRhythmyxUser()
            createTestSubscriber()
            createTestRhythmyxSubscriber()
            createTestEmailSubscriber()
            createTestRestSubscribers()
        }

        deleteChildlessSubscriptions()

        printReadyMessage()
    }

    def deleteChildlessSubscriptions() {

        Subscription.list().each {
            subscriptionService.deleteSubscriptionIfChildless(it)
        }
    }

    def showConfig() {

        log.debug("User home dir: ${System.getProperty('user.home')}")
        log.debug("Environment is: ${env}")
        log.debug("Mail configuration: \n${(config.grails.mail.flatten() as JSON).toString(true)}\n")
        log.debug("Syndication configuration: \n${(config.syndication as JSON).toString(true)}\n")
        log.debug("API Key configuration: \n${(config.apiKey as JSON).toString(true)}\n")

        def basicRabbitConfig = [connectionfactory: config.rabbitmq.connectionfactory.flatten()]
        log.debug("Rabbit MQ is configured as: \n${(basicRabbitConfig.connectionfactory as JSON).toString(true)}\n")
    }

    @SuppressWarnings("GrUnresolvedAccess")
    def createTestRhythmyxSubscriber() {

        def testRhythmyxSubscriber = testConfig.rhythmyxSubscriber

        if (RhythmyxSubscriber.findByInstanceName(testRhythmyxSubscriber.instanceName as String)) {
            log.info("Skipping the creation of the '${testRhythmyxSubscriber.instanceName}' rhythmyx test subscriber as it already exists")
            return
        }

        def rhythmyxSubscriber = new RhythmyxSubscriber()
        rhythmyxSubscriber.instanceName = testRhythmyxSubscriber.instanceName
        rhythmyxSubscriber.rhythmyxHost = testRhythmyxSubscriber.rhythmyxHost
        rhythmyxSubscriber.rhythmyxPort = testRhythmyxSubscriber.rhythmyxPort
        rhythmyxSubscriber.rhythmyxUser = testRhythmyxSubscriber.rhythmyxUser
        rhythmyxSubscriber.rhythmyxPassword = testRhythmyxSubscriber.rhythmyxPassword
        rhythmyxSubscriber.rhythmyxCommunity = testRhythmyxSubscriber.rhythmyxCommunity

        def entityName = testConfig.subscriber.keyAgreement.entity2 as String
        rhythmyxSubscriber.subscriber = Subscriber.findByName(entityName)

        rhythmyxSubscriber.save(failOnError: true, flush: true)

        log.info("Created the test rhythmyx subscriber '${rhythmyxSubscriber.instanceName}'")
    }

    def void createTestEmailSubscriber() {

        String email = testConfig.emailSubscriber.email

        if (EmailSubscriber.findByEmail(email)) {
            log.info("Skipping the creation of the email test subscriber '${email}' as it already exists")
            return
        }

        def entityName = testConfig.subscriber.keyAgreement.entity2 as String
        def subscriber = Subscriber.findByName(entityName)
        new EmailSubscriber(email: email, subscriber: subscriber).save(failOnError: true, flush: true)

        log.info("Created the test email subscriber '${email}'")
    }

    def initRoles() {

        initAdminRole()
        initRhythmyxRole()
    }

    private void initRhythmyxRole() {

        Role rhythmyxRole = new Role(authority: 'ROLE_RHYTHMYX_USER')

        if (!Role.findByAuthority('ROLE_RHYTHMYX_USER')) {

            rhythmyxRole.save(flush: true)

            if (rhythmyxRole.hasErrors()) {
                throw new RuntimeException(loggingService.logDomainErrors(rhythmyxRole))
            }
        }
    }

    private void initAdminRole() {

        Role adminRole = new Role(authority: 'ROLE_ADMIN')

        if (!Role.findByAuthority('ROLE_ADMIN')) {

            adminRole.save(flush: true)

            if (adminRole.hasErrors()) {
                throw new RuntimeException(loggingService.logDomainErrors(adminRole))
            }
        }
    }

    def initAdminUser() {

        if (!defaultPassword || !adminUsername) {
            throw new RuntimeException("The 'springsecurity.cmsManager.adminUsername' or 'springsecurity.cmsManager.defaultPassword' has not been defined in ${config.externalConfig}")
        }

        String username = adminUsername

        if (!User.findByUsername(username)) {

            User adminUser = new User(username: username, enabled: true, password: defaultPassword)

            adminUser.save(flush: true)
            if (adminUser.hasErrors()) {
                throw new RuntimeException(loggingService.logDomainErrors(adminUser))
            }

            def adminRole = Role.findByAuthority('ROLE_ADMIN')
            UserRole.create(adminUser, adminRole, false).save(flush: true)

            log.info("Created the initial admin user '${username}' user with the password '${adminUser.password}'")
            log.info("You should immediately change this value in production.")
        }
    }

    def void createTestRhythmyxUser() {

        def username = 'rhythmyx'

        if (!User.findByUsername(username)) {

            User rhythmyxUser = new User(username: 'rhythmyx', enabled: true, password: defaultPassword).save(flush: true, failOnError: false)
            if (rhythmyxUser.hasErrors()) {
                log.error("Encountered errors when creating the rhythmyxUser '${rhythmyxUser}'")
                log.error(rhythmyxUser.errors)
            }

            def rhythmyxRole = Role.findByAuthority('ROLE_RHYTHMYX_USER')
            UserRole.create(rhythmyxUser, rhythmyxRole, false).save(flush: true)

            log.info("Created the test rhythmyx user '${rhythmyxUser.username}' with the password '${rhythmyxUser.password}'")
        }
    }

    def void createTestSubscriber() {

        def testSubscriber = testConfig.subscriber

        def entityName = testSubscriber.keyAgreement.entity2 as String
        def email = testSubscriber.email.address as String

        if (Subscriber.findByName(entityName) == null) {

            def subscriber = new Subscriber([
                    name            : entityName,
                    email           : email,
                    sendKeyAgreement: false,
                    isPrivileged    : true
            ])

            if (!subscriber.validate()) {
                log.info(subscriber.errors)
            } else {

                def keyAgreement = new KeyAgreement(testSubscriber.keyAgreement as Map).save(failOnError: true)

                subscriber.keyAgreement = keyAgreement
                subscriber.save(flush: true, failOnError: true)

                log.info("Created the test Subscriber and key agreement for '${entityName}'")

                def rhythmyxUser = User.findByUsername('rhythmyx')
                def adminUser = User.findByUsername(adminUsername)

                userSubscriberService.associateUserWithSubscriber(subscriber, rhythmyxUser)
                log.info("Associated user 'rhythmyx' with subscriber '${entityName}'")

                userSubscriberService.associateUserWithSubscriber(subscriber, adminUser)
                log.info("Associated user '${adminUser.username}' with subscriber '${entityName}'")
            }
        } else {
            log.info("Skipping creation of the Subscriber and KeyAgreement for '${entityName}' as it already exists")
        }
    }

    def void createTestRestSubscribers() {

        def restSubscribers = testConfig.restSubscribers
        restSubscribers.each {

            def subscriber = Subscriber.findByName(it.subscriber as String)
            String deliveryEndpoint = it.deliveryEndpoint

            def existingRestSubscriber = RestSubscriber.findByDeliveryEndpoint(deliveryEndpoint)

            if (!existingRestSubscriber) {

                def restSubscriber = new RestSubscriber(deliveryEndpoint: deliveryEndpoint, subscriber: subscriber)

                if (!restSubscriber.validate()) {
                    log.info(restSubscriber.errors)
                } else {
                    restSubscriber.save(flush: true)
                    log.info("Created the test RestSubscriber '${deliveryEndpoint}'")
                }
            } else {
                log.info("Skipping creation of the RestSubscriber '${deliveryEndpoint}' as it already exists")
            }
        }
    }

    @SuppressWarnings(["GrMethodMayBeStatic", "GroovyResultOfObjectAllocationIgnored"])
    private initMarshallers() {
        new RestSubscriptionMarshaller()
        new SubscriberMarshaller(serverUrl)
        new KeyAgreementMarshaller()
    }

    def destroy = {
    }

    private printReadyMessage(){
        log.info("==========================================")
        log.info("| -> CMS Manager Ready.                  |")
        log.info("==========================================")
    }
}
