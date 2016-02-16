
/*
Copyright (c) 2014-2016, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package syndication.authentication

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.NOT_FOUND

import grails.transaction.Transactional

import com.ctacorp.syndication.authentication.User

@Transactional(readOnly = true)
class UserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]
    
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model:[userInstanceCount: User.count()]
    }

    def show(User userInstance) {
        respond userInstance
    }

    def create() {        
        respond new User(params)
    }

    @Transactional
    def save(User userInstance) {
        if(userInstance.hasErrors()) {
            respond userInstance.errors, view:'create'
        }
        else {
            userInstance.save flush:true
            request.withFormat {
                form { 
                    flash.message = message(code: 'default.created.message', args: [message(code: 'userInstance.label', default: 'User'), userInstance.id])
                    redirect userInstance
                }
                '*' { respond userInstance, [status: CREATED] }
            }
        }
    }

    def edit(User userInstance) { 
        respond userInstance  
    }

    @Transactional
    def update(User userInstance) {
        if(userInstance == null) {
            render status:404
        }
        else if(userInstance.hasErrors()) {
            respond userInstance.errors, view:'edit'
        }
        else {
            userInstance.save flush:true
            request.withFormat {
                form { 
                    flash.message = message(code: 'default.updated.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                    redirect userInstance 
                }
                '*'{ respond userInstance, [status: OK] }
            }
        }        
    }

    @Transactional
    def delete(User userInstance) {
        if(userInstance) {
            userInstance.delete flush:true
            request.withFormat {
                form { 
                    flash.message = message(code: 'default.deleted.message', args: [message(code: 'User.label', default: 'User'), userInstance.id])
                    redirect action:"index", method:"GET" 
                }
                '*'{ render status: NO_CONTENT }
            }                
        }
        else {
            render status: NOT_FOUND
        }
    }
}

