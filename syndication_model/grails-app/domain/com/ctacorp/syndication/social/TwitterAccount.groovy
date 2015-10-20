package com.ctacorp.syndication.social

class TwitterAccount {

    String accountName
    Long subscriberId

    static constraints = {
        accountName nullable: false, unique: true, maxSize: 255
        subscriberId nullable: false, blank: false
    }

    def beforeValidate(){
        if(accountName && accountName.startsWith('@')){
            accountName = accountName[1..-1]
        }
    }

    String toString(){
        "@${accountName}"
    }
}
