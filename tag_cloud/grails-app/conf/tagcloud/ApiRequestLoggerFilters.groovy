package tagcloud

class ApiRequestLoggerFilters {

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                log.info "TagCloud request on ${controllerName}/${actionName}?${params.collect{ "${it.key}=${it.value}" }.join('&')}"
                true
            }
            after = { Map model ->

            }
            afterView = { Exception e ->

            }
        }
    }
}
