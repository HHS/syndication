package syndication_storefront

class UrlMappings {

    static mappings = {

        "/akamai-test-object.html"(controller: 'akamaiTest', action: 'index')

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"storefront")
        "500"(view:'/error')
    }
}
