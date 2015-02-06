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

@Model(id = "Source", properties = [
    @ModelProperty(propertyName = "id",             attributes = [@PropertyAttribute(type = "integer", format = "int64",       required = true)]),
    @ModelProperty(propertyName = "name",           attributes = [@PropertyAttribute(type = "string",     required = true)]),
    @ModelProperty(propertyName = "acronym",        attributes = [@PropertyAttribute(type = "string",     required = true)]),
    @ModelProperty(propertyName = "websiteUrl",     attributes = [@PropertyAttribute(type = "string",     required = true)]),
    @ModelProperty(propertyName = "largeLogoUrl",   attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "smallLogoUrl",   attributes = [@PropertyAttribute(type = "string")]),
    @ModelProperty(propertyName = "contactEmail",   attributes = [@PropertyAttribute(type = "string")])
])
class Source {
    String name
    String acronym
    String description
    String websiteUrl
    String largeLogoUrl
    String smallLogoUrl
    String contactEmail

    static mapping = {
        description type: 'text'
    }

    static constraints = {
        name            nullable: false,    blank: false, unique:true,  minSize: 1,   maxSize: 255
        acronym         nullable: false,    blank: false, unique:true,  minSize: 1,   maxSize: 255
        description     nullable: true,     blank: true,                maxSize: 5000
        websiteUrl      nullable: false,    blank: false, url: true,    maxSize: 2000
        largeLogoUrl    nullable: true,     blank: false, url: true,    maxSize: 2000
        smallLogoUrl    nullable: true,     blank: false, url: true,    maxSize: 2000
        contactEmail    nullable: true,     blank: false, email:true,   maxSize: 2000
    }

    String toString() { name }

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
}