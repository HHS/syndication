package com.ctacorp.tagcloud.utils

import spock.lang.Specification
import com.ctacorp.syndication.commons.util.Util


/**
 * Created with IntelliJ IDEA.
 * User: Steffen Gates
 * Date: 5/12/14
 * Time: 1:36 PM
 */
class UtilSpec extends Specification {
    void "getMax should return a valid max when params max is negative"(){
        expect: "negative numbers aren't allowed to pass through"
            Util.getMax(max:-1) == 20
    }

    void "max greater that 1000 should be reduced to 1000"(){
        expect:
            Util.getMax(max:1001) == 1000
    }

    void "max's smaller than 1000 and greater than 0 should be passed through"(){
        expect:
            Util.getMax(max:500) == 500
    }

    void "max values as String should be converted to ints"(){
        expect:
            Util.getMax(max:"250") == 250
    }

    void "isTrue should respect the default value setting when input is null"(){
        expect: "when input is true and default is false, output should be false"
            Util.isTrue(null, false) == false
        and: "when input is false and default id false, output should be false"
            Util.isTrue(null, true) == true
    }

    void "isTrue should work with boolean values"(){
        expect: "when a boolean is supplied, it should work"
            Util.isTrue(true, false) == true
        and:
            Util.isTrue(false, true) == false
    }

    void "isTrue should work with strings"(){
        expect: "boolean string 'true' should be true"
            Util.isTrue('true', false) == true
        and:
            Util.isTrue('false', true) == false
    }

    void "isTrue should work with integer 0 and 1"(){
        expect: "1 should be true"
            Util.isTrue(1, false) == true
        and: "anything else should be false"
            Util.isTrue(0, true) == false
        and:
            Util.isTrue(100, true) == false
    }

    void "isTrue should work with string integers"(){
        expect: "string '1' should be true"
            Util.isTrue('1', false) == true
        and: "anything else should be false"
            Util.isTrue('100', true) == false
        and:
        Util.isTrue('0', true) == false
    }
}