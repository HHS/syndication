package syndication.error

import org.codehaus.groovy.grails.web.errors.GrailsWrappedRuntimeException
import grails.converters.JSON
import syndication.api.ApiResponse
import syndication.api.Message

/**
 * Created by nburk on 9/25/14.
 */
class errorService {

    def checkInternalError(def params, GrailsWrappedRuntimeException except){
        if((params?.max && !params?.max[0].isNumber()) || (params.offset && !params?.offset[0].isNumber()) || (params.sort && !params?.sort[0].isNumber())){
            Message message = new Message([errorCode:"400",errorMessage:except.message,userMessage:"Bad max, offset or sort input"])
            return [400, {ApiResponse.get400ResponseCustomMessage(message) as JSON}]
        }
        return [500, { ApiResponse.get500ResponseForException(except) as JSON }]
    }
}
