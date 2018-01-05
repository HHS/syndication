package cms.manager

class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/" (controller: "rhythmyxSubscription", action: "home")

        "/akamai-test-object.html"(controller: 'akamaiTest', action: 'index')

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
