package com.ctacorp.syndication.manager.cms.rest.security

import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle;

class AuthorizationHeader {

    String senderHash
    String senderPublicKey

    @Override
    String toString() {
        ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE)
    }
}