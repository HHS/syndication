/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication

import com.ctacorp.grails.swagger.annotations.*
import com.ctacorp.syndication.media.MediaItem
import com.ctacorp.syndication.metric.CampaignMetric

@Model(id = "Campaign", properties = [
    @ModelProperty(propertyName = "id",             attributes = [@PropertyAttribute(type = "integer", format = "int64",      required = true)]),
    @ModelProperty(propertyName = "name",           attributes = [@PropertyAttribute(type = "string",    required = true)]),
    @ModelProperty(propertyName = "startDate",      attributes = [@PropertyAttribute(type = "string", format = "date",      required = true)]),
    @ModelProperty(propertyName = "mediaItems",     attributes = [@PropertyAttribute(type = "array",     typeRef = "MediaItem")]),
    @ModelProperty(propertyName = "description",    attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "endDate",        attributes = [@PropertyAttribute(type = "string", format = "date")]),
    @ModelProperty(propertyName = "source",         attributes = [@PropertyAttribute(type = "Source")]),
    @ModelProperty(propertyName = "contactEmail",   attributes = [@PropertyAttribute(type = "string")])
])
class Campaign {
    String name
    String description
    Date startDate
    Date endDate
    Source source
    String contactEmail

    static hasMany = [mediaItems: MediaItem, campaignMetrics: CampaignMetric]

    static constraints = {
        name            nullable: false,    unique: true, blank: false,             maxSize: 255
        description     nullable: true,     blank: false,                           maxSize: 2000
        startDate       nullable: false
        endDate         nullable: true
        source          nullable: false
        contactEmail    nullable: true,     blank: false,  email: true,             maxSize: 255
    }

    static namedQueries = {
        //Sort Order -------------------------------------------------------------------
        multiSort{ String sortQuery = "id" ->
            def parts = sortQuery.split(",")
            parts.each{ part ->
                if(part.startsWith("-")){
                    String field = part[1..-1]
                    order(field, 'desc')
                } else{
                    order(part, 'asc')
                }
            }
        }
    }

    String toString() { name }
}
