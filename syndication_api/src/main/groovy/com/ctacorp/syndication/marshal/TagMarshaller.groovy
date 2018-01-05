package com.ctacorp.syndication.marshal

import com.ctacorp.grails.swagger.annotations.Definition
import com.ctacorp.grails.swagger.annotations.DefinitionProperty
import com.ctacorp.grails.swagger.annotations.DefinitionPropertyType

/**
 * Created by nburk on 3/21/17.
 */
@Definition
class TagMarshaller {
    @DefinitionProperty(name='id', type = DefinitionPropertyType.INTEGER)
    def id
    @DefinitionProperty(name='name', type = DefinitionPropertyType.STRING)
    def name
    @DefinitionProperty(name='language', type=DefinitionPropertyType.OBJECT, reference = 'TagLanguageMarshaller')
    def language
    @DefinitionProperty(name='type', type=DefinitionPropertyType.OBJECT, reference = 'TagTypeMarshaller')
    def type
}
