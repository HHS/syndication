package com.ctacorp.tinyurl.marshalling

import com.ctacorp.tinyurl.MediaMapping
import grails.converters.JSON

/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 8/29/13
 * Time: 9:13 AM
 * To change this template use File | Settings | File Templates.
 */
class MediaMappingMarshaller {
    MediaMappingMarshaller(){
        JSON.registerObjectMarshaller(MediaMapping){ MediaMapping mm ->
            return [
                id: mm.id,
                targetUrl: mm.targetUrl,
                tinyUrl: mm.tinyUrl,
                tinyUrlToken: mm.token,
                guid: mm.guid,
                syndicationId: mm.syndicationId
            ]
        }
    }
}
