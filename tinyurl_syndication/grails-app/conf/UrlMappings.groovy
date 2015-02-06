class UrlMappings {
	static mappings = {
        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

        "/$token"(controller:"redir", action:"resolveTinyToken"){
            constraints{
                //dev consoles
                token(validator: { !['dbconsole', 'console'].contains(it) })
            }
        }

        "/api/v1/mediaMappings/getByTargetUrl(.${format})?"(controller: "mediaMappingQuery", action:"getByTargetUrl")
        "/api/v1/mediaMappings/getByToken(.${format})?"(controller: "mediaMappingQuery", action:"getByToken")
        "/api/v1/mediaMappings/getByTinyUrl(.${format})?"(controller: "mediaMappingQuery", action:"getByTinyUrl")
        "/api/v1/mediaMappings/getBySyndicationId/$id(.${format})?"(controller: "mediaMappingQuery", action:"getBySyndicationId")

        "/api/v1/mediaMappings(.${format})?"(controller:'mediaMappingRest', parseRequest: true){
            action = [GET:'index', POST:'save']
        }
        "/api/v1/mediaMappings/$id(.${format})?"(controller:'mediaMappingRest', parseRequest: true){
            action = [GET:'show']
        }

        "/statusCheck"(controller:"statusCheck")
        "/appInfo"(controller:'appInfo')
        "/"(controller: 'mediaMapping')
        "500"(view:'/error')
	}
}
