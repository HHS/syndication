import com.ctacorp.syndication.commons.util.Util
import spock.lang.Specification


/**
 * Created by sgates on 9/9/15.
 */
class UtilSpec extends Specification {
    def "getMax should return expected max when input is null"() {
        given: "a null params input"
            def params = null
        when: "getMax is called"
            def max = Util.getMax(params)
        then: "max should be 20"
            max == 20
    }

    def "getMax should return overriden max when too big of a max is supplied"() {
        given: "a max that is too big"
            def params = [max:Integer.MAX_VALUE]
        when: "getMax is called"
            def result = Util.getMax(params)
        then: "resulting max should be limited to global max"
            result == 1000
    }

    def "getMax should return supplied max if it is less than global max"() {
        given: "a reasonable max value"
            def params = [max:35]
        when: "getMax is called"
            def result = Util.getMax(params)
        then: "result should be the same value supplied"
            result == 35
    }

    def "getMax should work with small values too"() {
        given: "a small max"
            def params = [max:1]
        when: "getMax is called"
            def result = Util.getMax(params)
        then: "result should be supplied valued"
            result == 1
    }

    def "buildQuery should ignore query params containing reserved words"() {
        given: "a query with reserved words"
            def params = [controller:"someController", action:"someAction", format:"someFormat"]
        when: "buildQuery is called"
            def result = Util.buildQuery(params)
        then: "the result should not contain the reserved words"
            !result
    }

    def "buildQuery should allow non-reserved words"() {
        given: "a query without reserved words"
            def params = [alpha:"a", beta:"b", gamma:"g"]
        when: "buildQuery is called"
            def result = Util.buildQuery(params)
        then: "the result should contain the expected words"
            result == "?alpha=a&beta=b&gamma=g"
    }

    def "buildQuery should return empty string if no params are supplied"() {
        given: "an empty query"
            def params = [:]
        when: "buildQuery is called"
            def result = Util.buildQuery(params)
        then: "the result should be empty"
            !result
    }

    def "isTrue should respect default value if input is null"() {
        given: "null input"
            def value = null
        when: "isTrue is called"
            def tResult = Util.isTrue(value, true)
            def fResult = Util.isTrue(value, false)
        then: "results should match expected values"
            tResult == true
            fResult == false
    }

    def "isTrue should work with boolean values"() {
        given: "sample boolean values"
            boolean t = true
            boolean f = false
        when: "isTrue is called"
            def tResult = Util.isTrue(t)
            def fResult = Util.isTrue(f)
        then: "the results should match the expected output"
            tResult == true
            fResult == false
    }

    def "isTrue should work with integer values"() {
        given: "sample boolean values"
            int t = 1
            int f = 0
        when: "isTrue is called"
            def tResult = Util.isTrue(t)
            def fResult = Util.isTrue(f)
        then: "the results should match the expected output"
            tResult == true
            fResult == false
    }

    def "isTrue should work with string number values"() {
        given: "sample boolean values"
            String t = "1"
            String f = "0"
        when: "isTrue is called"
            def tResult = Util.isTrue(t)
            def fResult = Util.isTrue(f)
        then: "the results should match the expected output"
            tResult == true
            fResult == false
    }

    def "isTrue should work with string boolean values"() {
        given: "sample boolean values"
            String t = "true"
            String f = "false"
        when: "isTrue is called"
            def tResult = Util.isTrue(t)
            def fResult = Util.isTrue(f)
        then: "the results should match the expected output"
            tResult == true
            fResult == false
    }

    def "isTrue should work with string boolean values and be case insensitive"() {
        given: "sample boolean values"
            String t = "TrUe"
            String f = "FaLsE"
        when: "isTrue is called"
            def tResult = Util.isTrue(t)
            def fResult = Util.isTrue(f)
        then: "the results should match the expected output"
            tResult == true
            fResult == false
    }
}