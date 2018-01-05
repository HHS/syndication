package com.ctacorp.syndication.marshal

import com.ctacorp.grails.swagger.annotations.Definition
import com.ctacorp.grails.swagger.annotations.DefinitionProperty
import com.ctacorp.grails.swagger.annotations.DefinitionPropertyType

/**
 * Created by nburk on 3/23/17.
 */
@Definition
class SyndicateMarshaller {

    @DefinitionProperty(name='content', type = DefinitionPropertyType.STRING)
    def content
    @DefinitionProperty(name='description', type = DefinitionPropertyType.STRING)
    def description
    @DefinitionProperty(name='id', type = DefinitionPropertyType.INTEGER)
    def id
    @DefinitionProperty(name='mediaType', type = DefinitionPropertyType.STRING)
    def mediaType
    @DefinitionProperty(name='name', type = DefinitionPropertyType.STRING)
    def name
    @DefinitionProperty(name='sourceUrl', type = DefinitionPropertyType.STRING)
    def sourceUrl
}
