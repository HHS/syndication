import com.ctacorp.syndication.commons.util.Hash
import spock.lang.Specification

/**
 * Created by sgates on 6/12/15.
 */
class HashSpec extends Specification {
    def "hashing should work with string inputs"() {
        given: "a string input"
            String input = "hey there what's up?"
        when: "the input is hashed"
            String result = Hash.md5(input)
        then: "the result should be non-null"
            result
        and: "it should be a correct md5 hash"
            result == "6d6e83e745bf2b9a6537e1480175c0e6"
    }

    def "hashing should work with byte inputs"() {
        given: "a byte input"
            byte[] input = "hey there what's up?".bytes
        when: "the input is hashed"
            String result = Hash.md5(input)
        then: "the result should be non-null"
            result
        and: "it should be a correct md5 hash"
            result == "6d6e83e745bf2b9a6537e1480175c0e6"
    }
}