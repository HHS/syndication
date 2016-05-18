package com.ctacorp.syndication.authentication

import grails.transaction.Transactional

@Transactional
class UserService {
    def springSecurityService
    def passwordService

    def updateUser(params) {
        if (springSecurityService.isLoggedIn()) {
            User userInstance = springSecurityService.currentUser as User
            userInstance.name = params.name ?: null
            userInstance.password = params.password

            def passwordValidation = passwordService.validatePassword(params.password, params.passwordRepeat)
            userInstance.validate()
            if(userInstance.hasErrors() || !passwordValidation.valid) {
                if(passwordValidation?.validationMessage) {
                    userInstance.errors.rejectValue("password", "bad_password", passwordValidation?.validationMessage)
                }
                userInstance.discard()
                return userInstance
            } else {
                if (!userInstance.save(flush: true)) {
                    throw new RuntimeException("Could not update user model")
                    return
                }
            }
            userInstance
        }
    }

    def createUser(params){
        User userInstance = new User(params)
        userInstance.lastLogin = new Date()
        boolean validInstance = userInstance.validate()
        def passwordValidation = passwordService.validatePassword(params.password, params.passwordRepeat)
        if(userInstance.hasErrors() || !passwordValidation.valid) {
            if(passwordValidation?.validationMessage) {
                userInstance.errors.rejectValue("password", "bad_password", passwordValidation?.validationMessage)
            }
            return userInstance
        }

        userInstance.enabled = true
        if (!userInstance.save(flush: true)) {
            return userInstance
        }

        UserRole.create userInstance, Role.findByAuthority("ROLE_STOREFRONT_USER"), true
        userInstance
    }
}
