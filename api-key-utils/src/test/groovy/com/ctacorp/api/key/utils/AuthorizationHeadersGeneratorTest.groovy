package com.ctacorp.api.key.utils

import com.ctacorp.commons.api.key.utils.AuthorizationHeaderGenerator
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse

public class AuthorizationHeadersGeneratorTest {

    private AuthorizationHeaderGenerator.KeyAgreement keyAgreement = new AuthorizationHeaderGenerator.KeyAgreement()
    private AuthorizationHeaderGenerator generator = new AuthorizationHeaderGenerator("api_key", keyAgreement)

    @Before
    public void b4() {

        keyAgreement.setPrivateKey("eWV8zlOHK52dbF9r1v6EZMfQtWnvgcwPbn79W0jSHJW3elE4n2rsD1Fuy8uxpB1EMcwcqXRil6xLqJ0ISL9fAQ==")
        keyAgreement.setPublicKey("e24cv82JldJrMZCjhTeGFTqCHmOTcIgyya7jTf37K7iM4N265Myqqt32qoBjz+oEZEglwmDW28Eru1gjKw6Iw==")
        keyAgreement.setSecret("x+Cm5SRyQDGGLyd3J7QYZFB2oKjD6WQ0NcA+uFZz/qH0XRuX+u7Q6U9tkAgiLc5ZyONtyPfGUPBEuCxychodmw==")
    }

    @Test
    public void testGetCanonicalizedHeaders() {

        HashMap<String, String> headers = new HashMap<String, String>()
        headers.put("Date", "Wed Dec 31 19:00:00 EST 1969")
        headers.put("Content-Type", "application/json")
        headers.put("Content-Length", "25")
        headers.put("Content-Beef-Supreme", "No Thanks")
        headers.put("Content-Beef-Weight", "10 lb")

        String canonicalizedHeaders = generator.getCanonicalizedHeaders(headers)
        assertEquals("date:Wed Dec 31 19:00:00 EST 1969\n" +
                "content-type:application/json\n" +
                "content-length:25", canonicalizedHeaders)
    }

    @Test
    public void testGetCanonicalizedResource() {

        String requestUrl = "http://localhost:8080/Syndication/api/v2/resources/media/htmls.json"
        String canonicalizedResource = generator.getCanonicalizedResource(requestUrl)
        assertEquals("/Syndication/api/v2/resources/media/htmls.json",canonicalizedResource)
    }

    @Test
    public void testGetHttpMethod() {

        String httpMethod = generator.normalizeHttpMethod("get")
        assertEquals("GET",httpMethod)
        String httpMethod2 = generator.normalizeHttpMethod("PUT")
        assertEquals("PUT",httpMethod2)
    }

    @Test
    public void testHashData() throws Exception {

        assertEquals("0b811a6c0b93b02597834193c6090d51", generator.hashData("Tacobell cannon!"))
        assertEquals("0b811a6c0b93b02597834193c6090d51", generator.hashData("Tacobell cannon!"))
        assertFalse("0b811a6c0b93b02597834193c6090d51".equals(generator.hashData("Tacobell cannony!")))
    }

    @Test
    public void testCreateSigningString() throws Exception {

        String signingString = generator.createSigningString("httpMethod", "md5", "canonicalizedHeaders", "canonicalizedResource")

        assertEquals("httpMethod\n" +
                "md5\n" +
                "canonicalizedHeaders\n" +
                "canonicalizedResource", signingString)
    }

    @Test
    public void testSignString() throws Exception {

        assertEquals("H+UmXMb4ooWsCFogqFuJOg==", generator.signString("The Amazing Signing String!"))
        assertEquals("H+UmXMb4ooWsCFogqFuJOg==", generator.signString("The Amazing Signing String!"))
        assertFalse("H+UmXMb4ooWsCFogqFuJOg==".equals(generator.signString("The Amazing Signed String!")))
    }

    @Test
    public void testGetApiKeyHeaderValue() throws Exception {

        String requestBody = "wheres the beef?"

        HashMap<String, String> headers = new HashMap<String, String>()
        headers.put("Date", "Wed Dec 31 19:00:00 EST 1969")
        headers.put("Content-Type", "text/plain")
        headers.put("Content-Length", ""+requestBody.length())

        String apiKeyHeaderValue = generator.getApiKeyHeaderValue(headers, "http://dude.com/dudes?duddettes=yes", "GET", requestBody)
        assertEquals("api_key e24cv82JldJrMZCjhTeGFTqCHmOTcIgyya7jTf37K7iM4N265Myqqt32qoBjz+oEZEglwmDW28Eru1gjKw6Iw==:Gf1ogUK0wY1lEU+71Wphzw==",apiKeyHeaderValue)
    }
}
