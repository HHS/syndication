/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication

import com.ctacorp.grails.swagger.annotations.Model
import com.ctacorp.grails.swagger.annotations.ModelProperty
import com.ctacorp.grails.swagger.annotations.PropertyAttribute

@Model(id = "AlternateImage", properties = [
    @ModelProperty(propertyName = "id",     attributes = [@PropertyAttribute(type = "integer", format = "int64",       required = true)]),
    @ModelProperty(propertyName = "url",    attributes = [@PropertyAttribute(type = "string",     required = true)]),
    @ModelProperty(propertyName = "name",   attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "width",  attributes = [@PropertyAttribute(type = "integer", format = "int32")]),
    @ModelProperty(propertyName = "height", attributes = [@PropertyAttribute(type = "integer", format = "int32")])
])
class AlternateImage {
    Integer width
    Integer height
    String  name
    String  url

    static belongsTo = [mediaItem:MediaItem]

    static constraints = {
        name    nullable: false,             blank: false,  maxSize: 255
        url     nullable: false,  url:true, blank: false
        width   nullable: true, min: 1, max: (Integer.MAX_VALUE - 1)
        height  nullable: true, min: 1, max: (Integer.MAX_VALUE - 1)
    }

    String toString(){
        "${name}"
    }
}
