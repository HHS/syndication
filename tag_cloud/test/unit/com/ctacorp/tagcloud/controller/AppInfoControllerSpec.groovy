package com.ctacorp.tagcloud.controller

import tagcloud.AppInfoController
import grails.test.mixin.TestFor
import spock.lang.Specification


/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 5/9/14
 * Time: 4:56 PM
 */
@TestFor(AppInfoController)
class AppInfoControllerSpec extends Specification {
    def "index action should render index view"(){
        when: "index action is called"
            controller.index()
        then: "The view should be index"
            view == "/appInfo/index"
    }
}