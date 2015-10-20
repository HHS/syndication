package com.ctacorp.syndication.media

import com.ctacorp.grails.swagger.annotations.ModelExtension
import com.ctacorp.grails.swagger.annotations.ModelProperty
import com.ctacorp.grails.swagger.annotations.PropertyAttribute

@ModelExtension(id = "Periodical", model = "MediaItem", addProperties = [
        @ModelProperty(propertyName = "period",  attributes = [@PropertyAttribute(type = "Period", required = true)]),
])
class Periodical extends MediaItem{
    Period period

    static constraints = {
        period nullable: false
    }

    enum Period{
        ANNUALLY("Annually"), MONTHLY("Monthly"), WEEKLY("Weekly"), DAILY("Daily")

        String displayName

        Period(String displayName){
            this.displayName = displayName
        }

        String toString(){
            displayName
        }
    }
}
