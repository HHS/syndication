/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package com.ctacorp.syndication

import com.ctacorp.grails.swagger.annotations.Definition
import com.ctacorp.grails.swagger.annotations.DefinitionProperty
import com.ctacorp.grails.swagger.annotations.DefinitionPropertyType
import com.ctacorp.grails.swagger.annotations.PropertyAttribute
import com.ctacorp.syndication.media.MediaItem

@Definition
class AlternateImage {
    @DefinitionProperty(type=DefinitionPropertyType.INTEGER, format = 'int32')
    Integer width
    @DefinitionProperty(type=DefinitionPropertyType.INTEGER, format = 'int32')
    Integer height
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String  name
    @DefinitionProperty(type=DefinitionPropertyType.STRING)
    String  url

    static belongsTo = [mediaItem:MediaItem]

    static constraints = {
        name    nullable: false,             blank: false,  maxSize: 255
        url     nullable: false,  url:true,  blank: false, maxSize: 2000
        width   nullable: true, min: 1, max: (Integer.MAX_VALUE - 1)
        height  nullable: true, min: 1, max: (Integer.MAX_VALUE - 1)
    }

    String toString(){
        "${name}"
    }
}
