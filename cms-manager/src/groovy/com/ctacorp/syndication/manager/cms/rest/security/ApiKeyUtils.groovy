/*
Copyright (c) 2014, Health and Human Services - Web Communications (ASPA)
 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

  */
package com.ctacorp.syndication.manager.cms.rest.security

import grails.util.Holders
import org.apache.commons.lang.RandomStringUtils
import org.apache.commons.logging.LogFactory
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Base64

import javax.crypto.KeyAgreement
import javax.crypto.Mac
import javax.crypto.interfaces.DHPrivateKey
import javax.crypto.interfaces.DHPublicKey
import javax.crypto.spec.SecretKeySpec
import java.security.KeyPairGenerator
import java.security.Security

class ApiKeyUtils {

    static def log = LogFactory.getLog(this)
    static final HMAC_MD5_ALGORITHM = "HMac-MD5"
    static String keyName
    static String headerName

    static {
        Security.addProvider(new BouncyCastleProvider())
        def apiKey = Holders.config?.apiKey

        if (apiKey) {
            keyName = apiKey.keyName
            headerName = apiKey.headerName
        }
    }

    static def newKeyAgreement(entity1, entity2) {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH", "BC")
        keyGen.initialize(512)

        def entity1KeyPair = keyGen.generateKeyPair()
        def entity1PublicKey = entity1KeyPair.public as DHPublicKey
        def entity1PrivateKey = entity1KeyPair.private as DHPrivateKey

        def entity2KeyPair = keyGen.generateKeyPair()
        def entity2PublicKey = entity2KeyPair.public as DHPublicKey
        def entity2PrivateKey = entity2KeyPair.private as DHPrivateKey

        byte[] secret = generateSecret(entity1PrivateKey, entity2PublicKey)

        [
                entity1          : entity1,
                entity2          : entity2,
                entity1PublicKey : new String(Base64.encode(entity1PublicKey.y.toByteArray())),
                entity1PrivateKey: new String(Base64.encode(entity1PrivateKey.x.toByteArray())),
                entity2PublicKey : new String(Base64.encode(entity2PublicKey.y.toByteArray())),
                entity2PrivateKey: new String(Base64.encode(entity2PrivateKey.x.toByteArray())),
                prime            : new String(Base64.encode(entity1PublicKey.getParams().p.toByteArray())),
                generator        : new String(Base64.encode(entity1PublicKey.getParams().g.toByteArray())),
                secret           : new String(Base64.encode(secret)),
        ]
    }

    static byte[] generateSecret(DHPrivateKey entity1PrivateKey, DHPublicKey entity2PublicKey) {

        def keyAgreement = KeyAgreement.getInstance("DiffieHellman")
        keyAgreement.init(entity1PrivateKey)
        keyAgreement.doPhase(entity2PublicKey, true)
        keyAgreement.generateSecret()
    }

    static String canonicalizeHeaders(AuthorizationRequest authorizationRequest) {

        def normalize = { name, value ->
            if (value) {
                return name + ":" + value.replaceAll('\n', '').replaceAll('\r', '').trim() + '\n'
            }
            return ''
        }

        def canonicalizedHeaders = normalize('date', authorizationRequest.dateHeader)

        def contentTypeHeader = authorizationRequest.contentTypeHeader
        if(contentTypeHeader) {
            canonicalizedHeaders += normalize('content-type', contentTypeHeader)
        }

        def contentLengthHeader = authorizationRequest.contentLengthHeader
        if(contentLengthHeader) {
            canonicalizedHeaders += normalize('content-length', contentLengthHeader)
        }

        return canonicalizedHeaders.trim()
    }

    static String canonicalizeResource(String url) {

        if (url.indexOf('?') > 0) {
            url = url.substring(0, url.indexOf('?'))
        }

        if (url.startsWith("http")) {

            url = url.replace("http://", "")
            url = url.replace("https://", "")

            if (url.contains("/")) {
                url = url.substring(url.indexOf("/"), url.length())
            }
        }
        url
    }

    static def createSigningString(AuthorizationResult authorizationResult) {
        "${authorizationResult.requestMethod}\n${authorizationResult.dataMd5}\n${authorizationResult.canonicalizedHeaders}\n${authorizationResult.canonicalizedResource}".toString()
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    static def signString(AuthorizationResult authorizationResult) {

        if (!authorizationResult.keyAgreement) {
            return null
        }

        Mac mac = Mac.getInstance(HMAC_MD5_ALGORITHM, "BC")
        def secretKey = authorizationResult.keyAgreement.secret.bytes
        SecretKeySpec signingKey = new SecretKeySpec(secretKey, HMAC_MD5_ALGORITHM)
        mac.init(signingKey)
        def hash = mac.doFinal(authorizationResult.signingString.bytes)
        new String(Base64.encode(hash))
    }

    static AuthorizationHeader getAuthHeader(AuthorizationResult authorizationResult) {

        def logFileId = authorizationResult.logFileId

        log.info("authenticating request (${logFileId}) via api key")

        return getAuthHeader(authorizationResult.rawAuthHeader, logFileId)
    }

    static AuthorizationHeader getAuthHeader(String rawAuthHeader, logFileId) {
        if (!rawAuthHeader || rawAuthHeader.indexOf(keyName) != 0) {
            log.error("(${logFileId}) api key is missing")
            return null
        }

        String[] apiKey = rawAuthHeader.substring(keyName.length()).split(':')
        def apiKeyLength = apiKey.length

        if (apiKeyLength != 2) {
            log.error("(${logFileId}) api key is malformed")
            return null
        }

        def senderPublicKey = apiKey[0] as String
        def senderHash = apiKey[1] as String

        new AuthorizationHeader(senderHash: senderHash, senderPublicKey: senderPublicKey.trim())
    }

    static def getKeyAgreement(AuthorizationHeader authorizationHeader) {
        def keyAgreement = com.ctacorp.syndication.manager.cms.KeyAgreement.findByEntity2PublicKey(authorizationHeader.senderPublicKey)
        if(!keyAgreement){
            log.error "Could not find a matching key agreement - the sender's key isn't authorized."
        }
        keyAgreement
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    def AuthorizationResult buildAuthorizationResult(AuthorizationRequest authorizationRequest) {

        def builder = new AuthorizationResultBuilder()

        try {
            builder.setLogFileId(System.currentTimeMillis())
                    .setRawAuthHeader(authorizationRequest.authorizationHeader)
                    .setAuthHeader(getAuthHeader(builder.authorizationResult))
                    .setKeyAgreement(getKeyAgreement(builder.authorizationResult.authHeader))
                    .setRequestMethod(authorizationRequest.httpMethod)
                    .setCanonicalizedHeaders(canonicalizeHeaders(authorizationRequest))
                    .setCanonicalizedResource(canonicalizeResource(authorizationRequest.url))
                    .setDataMd5(authorizationRequest.dataMd5)
                    .setSigningString(createSigningString(builder.authorizationResult))
                    .setCalculatedHash(signString(builder.authorizationResult))

        } catch (AuthorizationServiceException e) {
            log.error("Could not build a complete authorization result due to the following error: \n${e}")
        }

        return processAuthorization(builder)
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    AuthorizationResult processAuthorization(AuthorizationResultBuilder builder) {

        def isAuthorized = builder.authorizationResult.isAuthorized

        if (!isAuthorized) {
            processAuthorizationFailure(builder)
        } else {
            builder.authorizationResult.setHttpStatus(204)
        }

        builder.authorizationResult
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    private AuthorizationResult processAuthorizationFailure(AuthorizationResultBuilder builder) {

        def authorizationResult = builder.authorizationResult

        //noinspection GroovyUnusedAssignment
        String errorMessage = null

        def logFileId = authorizationResult?.logFileId
        def authHeader = authorizationResult?.authHeader
        def senderHash = authHeader?.senderHash

        if (!authorizationResult?.rawAuthHeader) {
            errorMessage = logError(logFileId, "Bad Request: the 'Authorization' header is missing from the request (${logFileId})")
            builder.setHttpStatus(400)
        } else if (!senderHash) {
            errorMessage = logError(logFileId, "Bad Request: the 'Authorization' header is malformed (${logFileId})")
            builder.setHttpStatus(400)
        } else if (!authorizationResult?.keyAgreement) {
            errorMessage = logError(logFileId, "Unauthorized: the sender's public key is not valid (${logFileId})")
            builder.setHttpStatus(401)
        } else if (authorizationResult?.calculatedHash != senderHash) {
            errorMessage = logError(logFileId, "Forbidden: the sender's hash is different from the calculated hash (${logFileId})")
            builder.setHttpStatus(403)
        } else {

            def errorCode = RandomStringUtils.randomAlphabetic(10).toUpperCase()
            errorMessage = "Application Error: Please contact an administrator and reference this error code: ${errorCode}"
            log.error(errorMessage)

            def message = "authorization failed, but could not determine the cause"
            log.error(message)

            builder.setHttpStatus(500)
        }

        builder.setErrorMessage(errorMessage)

        logError(logFileId, "authorization result was ${authorizationResult?.toString()}")

        builder.authorizationResult
    }

    static String logError(logId, message) {
        log.error("(${logId}) ${message}")
        message
    }
}
