package com.ctacorp.syndication.commons.testing

/**
 * Created by sgates on 9/28/15.
 */
class DSLTester {
    static mock(clazz, method, result) {
        def tester = new DSLTester()
        clazz.metaClass.static."${method}" = { Closure dsl ->
            dsl.delegate = tester
            dsl.resolveStrategy = Closure.DELEGATE_FIRST
            dsl()
            return result
        }
        return tester
    }

    def result = []

    def methodMissing(String name, args) {
        result << [ name: name, args: args.toList() ]
    }
}
