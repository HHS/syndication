package com.ctacorp.syndication.commons.util

import java.security.MessageDigest

class Hash{
    public static md5(String inputString){
        MessageDigest digest = MessageDigest.getInstance("MD5")
        digest.update(inputString.bytes);
        new BigInteger(1, digest.digest()).toString(16).padLeft(32, '0')
    }

    public static md5(byte[] inputBytes){
        MessageDigest digest = MessageDigest.getInstance("MD5")
        digest.update(inputBytes);
        new BigInteger(1, digest.digest()).toString(16).padLeft(32, '0')
    }
}