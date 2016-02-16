package com.ctacorp.commons.api.key.utils

import groovy.util.logging.Log4j
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Base64
import org.bouncycastle.util.encoders.Hex

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.MessageDigest
import java.security.Security

@Log4j
public class AuthorizationHeaderGenerator {

    static {
        Security.addProvider(new BouncyCastleProvider())
    }

    static final String HMAC_MD5_ALGORITHM = "HMac-MD5"

    final String keyName
    final KeyAgreement keyAgreement

    boolean printToConsole = false

    static class KeyAgreement {

        String publicKey
        String secret
    }

    AuthorizationHeaderGenerator(String keyName, KeyAgreement keyAgreement) {

        this.keyName = keyName
        this.keyAgreement = keyAgreement
    }

    String getApiKeyHeaderValue(Map<String, String> headers, String requestUrl, String httpMethod, String requestBody) throws Exception {

        String computedHash = computeHash(headers, requestUrl, httpMethod, requestBody)
        "${keyName} ${keyAgreement.publicKey}:${computedHash}"
    }

    String computeHash(Map<String, String> headers, String requestUrl, String httpMethod, String requestBody) throws Exception {

        if(!requestBody) {
            requestBody = ""
        }

        String canonicalizedHeaders = getCanonicalizedHeaders(headers)
        String canonicalizedResource = getCanonicalizedResource(requestUrl)
        String method = normalizeHttpMethod(httpMethod)
        String hashedData = hashData(requestBody)

        String signingString = createSigningString(method,hashedData, canonicalizedHeaders, canonicalizedResource)
        String computedHash = signString(signingString)

        def requestBodyLogMessage = "Request body is: ${requestBody}"
        def signingStringLogMessage = "Signing string is: \n${signingString}"
        def computedHashLogMessage = "Computed hash is: ${computedHash}"

        log.info(requestBodyLogMessage)
        log.info(signingStringLogMessage)
        log.info(computedHashLogMessage)

        if(printToConsole) {
            println requestBodyLogMessage
            println signingStringLogMessage
            println computedHashLogMessage
        }

        computedHash
    }

    String signString(String signingString) throws Exception {

        Mac mac = Mac.getInstance(HMAC_MD5_ALGORITHM, "BC")
        SecretKeySpec signingKey = new SecretKeySpec(keyAgreement.secret.getBytes(), HMAC_MD5_ALGORITHM)
        mac.init(signingKey)
        byte[] hash = mac.doFinal(signingString.getBytes())
        new String(Base64.encode(hash))
    }

    String createSigningString(String requestMethod, String md5, String canonicalizedHeaders, String canonicalizedResource) {
        "${requestMethod}\n${md5}\n${canonicalizedHeaders}\n${canonicalizedResource}"
    }

    String hashData(String requestBody) throws Exception {

        MessageDigest digest = MessageDigest.getInstance("MD5", "BC")
        byte[] encoded = Hex.encode(digest.digest(requestBody.getBytes("UTF-8")))
        new String(encoded)
    }

    String normalizeHttpMethod(String httpMethod) {
        httpMethod.toUpperCase()
    }

    String getCanonicalizedResource(String requestUrl) {

        if (requestUrl.indexOf('?') > 0) {
            requestUrl = requestUrl.substring(0, requestUrl.indexOf('?'))
        }

        if (requestUrl.startsWith("http")) {

            requestUrl = requestUrl.replace("http://", "")
            requestUrl = requestUrl.replace("https://", "")

            if (requestUrl.contains("/")) {
                requestUrl = requestUrl.substring(requestUrl.indexOf("/"), requestUrl.length())
            }
        }

        requestUrl
    }

    String getCanonicalizedHeaders(Map<String, String> headers) {

        def canonicalizedHeaders = []

        for (String headerName : headers.keySet()) {

            if (headerName.toLowerCase().equals("date") || headerName.toLowerCase().equals("content-type") || headerName.toLowerCase().equals("content-length")) {

                String _headerName = headerName.toLowerCase()
                String _headerValue = headers.get(headerName)
                _headerValue = _headerValue.replace("\n", " ")
                _headerValue = _headerValue.replace("\r", " ")
                _headerValue = _headerValue.trim()
                canonicalizedHeaders.add(_headerName + ":" + _headerValue)
            }
        }

        canonicalizedHeaders.sort().reverse().join('\n').trim()
    }
}
