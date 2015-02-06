/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */

class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/" (controller: "rhythmyxSubscription", action: "home")

        //This should do what the above did
        "/index(.$format)?"(controller: "rhythmyxSubscription")

        "/status"(controller: "debug") {
            action = [GET: 'serverStatus']
        }

        "/api/v1/debug/status.json"(controller: "debug") {
            action = [GET: 'serverStatus']
        }

        "/api/v1/subscribers.json"(controller: "authorization") {
            action = [GET: 'getSubscribers']
        }

        "/api/v1/subscriber.json?"(controller: "authorization", parseRequest: true) {
            action = [GET: "getSubscriber"]
        }

        "/api/v1/authorizations.json"(controller: "authorization") {
            action = [POST: 'authorize']
        }

        "/api/v1/subscriptions.json?"(controller: "subscriptionManagement", parseRequest: true) {
            action = [PUT: 'subscribe', GET: 'getAllSubscriptions']
        }

        "/api/v1/subscriptions/($id).json?"(controller: "subscriptionManagement", parseRequest: true) {
            action = [GET: "getSubscription", DELETE: "deleteSubscription"]
        }

        "500"(view: '/error')
    }
}
