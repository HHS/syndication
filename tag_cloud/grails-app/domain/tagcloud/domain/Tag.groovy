/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 */

package tagcloud.domain
import com.ctacorp.syndication.Language

class Tag {
    String name
    TagType type
    Language language

    static hasMany = [contentItems:ContentItem]

    //Audit
    Date dateCreated
    Date lastUpdated

    static constraints = {
        name            nullable: false, blank: false, unique: ['language','type'], maxSize:255
        type            nullable: false
        language        nullable: false
    }

    String toString(){
        "$name (${id})"
    }

    static marshalling = {
        tagList{
            shouldOutputIdentifier true
            shouldOutputClass false
            ignore "contentItems", "dateCreated", "lastUpdated"
            deep "language", "type"
            //example serializer and virtual usage
//            serializer{
//                name { value, json -> json.value("${value.name.toUpperCase()}") }
//            }
//            virtual{
//                time { value, json -> json.value("${new Date()}") }
//            }
        }

        showTag{
            shouldOutputIdentifier true
            shouldOutputClass true
            deep "language", "type", "contentItems"
        }
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

        restrictToSet { String inList ->
            def parts = inList.split(",")
            def ids = []
            parts.each {
                ids << it as Long
            }
            'in' 'id', ids
        }

        idIs { Long id ->
            idEq(id)
        }

        nameIs{ String name ->
            like 'name', name
        }

        nameContains{ String name ->
            ilike 'name', "%${name}%"
        }

        urlIs{String url ->
            contentItems {
                eq 'url', url
            }
        }

        urlContains {String url ->
            contentItems {
                ilike 'url',"%${url}%"
            }
        }

        syndicationIdIs {Long syndicationId ->
            contentItems {
                eq 'syndicationId',syndicationId
            }
        }

        languageIdIs { Long languageId ->
            language{
                eq 'id', languageId
            }
        }

        typeIdIs{ Long typeId ->
            type {
                eq "id", typeId
            }
        }

        typeNameIs{ String typeName ->
            type {
                eq "name", typeName
            }
        }

        typeNameContains{ String typeName ->
            type {
                ilike "name", "%${typeName}%"
            }
        }

        facetedSearch{ params = [:] ->
            and {
                if(params.id)                       { idIs(            (Long)   params.long('id'))}
                if(params.name)                     { nameIs(          (String) params.name)}
                if(params.nameContains)             { nameContains(    (String) params.nameContains)}
                // Collaborators
                if(params.typeId)                   { typeIdIs(        (Long)   params.long('typeId'))}
                if(params.languageId)               { languageIdIs(    (Long)   params.long('languageId'))}
                if(params.typeName)                 { typeNameIs(      (String) params.typeName)}
                if(params.typeNameContains)         { typeNameContains((String) params.typeNameContains)}
                if(params.url)                      { urlIs(           (String) params.url)}
                if(params.urlContains)              { urlContains(     (String) params.urlContains)}
                if(params.mediaId)                  { syndicationIdIs( (Long)   params.long('mediaId'))}
                if(params.restrictToSet)            { restrictToSet(   (String) params.restrictToSet)}
            }
            if(params.sort){
                multiSort(params.sort)
            }
        }
    }
}