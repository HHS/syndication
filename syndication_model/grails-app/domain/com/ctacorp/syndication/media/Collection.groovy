/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication.media

import com.ctacorp.grails.swagger.annotations.Model
import com.ctacorp.grails.swagger.annotations.ModelProperty
import com.ctacorp.grails.swagger.annotations.PropertyAttribute
import com.ctacorp.syndication.Campaign

@Model(id="Collection", properties = [
    @ModelProperty(propertyName = "id",                      attributes = [@PropertyAttribute(type = "integer", format = "int64")]),
    @ModelProperty(propertyName = "name",                    attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "description",             attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "sourceUrl",               attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "dateContentAuthored",     attributes = [@PropertyAttribute(type = "string", format = "date")]),
    @ModelProperty(propertyName = "dateContentUpdated",      attributes = [@PropertyAttribute(type = "string", format = "date")]),
    @ModelProperty(propertyName = "dateContentPublished",    attributes = [@PropertyAttribute(type = "string", format = "date")]),
    @ModelProperty(propertyName = "dateContentReviewed",     attributes = [@PropertyAttribute(type = "string", format = "date")]),
    @ModelProperty(propertyName = "dateSyndicationCaptured", attributes = [@PropertyAttribute(type = "string", format = "date")]),
    @ModelProperty(propertyName = "dateSyndicationUpdated",  attributes = [@PropertyAttribute(type = "string", format = "date")]),
    @ModelProperty(propertyName = "dateSyndicationVisible",  attributes = [@PropertyAttribute(type = "string", format = "date")]),
    @ModelProperty(propertyName = "language",                attributes = [@PropertyAttribute(type = "Language")]),
    @ModelProperty(propertyName = "externalGuid",            attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "hash",                    attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "campaigns",               attributes = [@PropertyAttribute(type = "array", typeRef = "Campaign")]),
    @ModelProperty(propertyName = "mediaItems",              attributes = [@PropertyAttribute(type = "array", typeRef = "MediaItem")]),
    @ModelProperty(propertyName = "source",                  attributes = [@PropertyAttribute(type = "Source")])
])
class Collection extends MediaItem {
    static hasMany = [mediaItems: MediaItem, campaigns: Campaign]

    static constraints = {
        mediaItems()
        campaigns()
    }
}
