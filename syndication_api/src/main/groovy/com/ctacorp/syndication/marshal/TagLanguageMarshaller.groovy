package com.ctacorp.syndication.marshal

import com.ctacorp.grails.swagger.annotations.Definition
import com.ctacorp.grails.swagger.annotations.DefinitionProperty
import com.ctacorp.grails.swagger.annotations.DefinitionPropertyType

/**
 * Created by nburk on 4/18/17.
 */
@Definition
class TagLanguageMarshaller {
    @DefinitionProperty(name='id', type = DefinitionPropertyType.INTEGER)
    def languageId
    @DefinitionProperty(name='name', type = DefinitionPropertyType.STRING)
    def languageName
    @DefinitionProperty(name='isoCode', type = DefinitionPropertyType.STRING)
    def languageIsoCode
    @DefinitionProperty(name='isActive', type = DefinitionPropertyType.BOOLEAN)
    def languageIsActive
}
