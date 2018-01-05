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
            def model = controller.index()
        then: "The view should be index"
            model.metaData == ["app":["buildHash":"7f771151", "lastGitCommitDate":"Wed Mar 8 11:35:13 2017", "buildDate":"Fri Mar 10 11:21:54 2017"]]
    }
}