package com.ctacorp.syndication.commons.util

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

class Util {
    public static int getMax(params) {
        params = new GrailsParameterMap(params, null)
        if(params.max){
            int max = Math.min(params.int('max', 20), 1000)
            return max > 0 ? max : 20
        }
        20
    }

    public static boolean isTrue(value, boolean defaultValue = false){
        if(value == null){
            return defaultValue
        }
        if(value == true || value == "true" || value == 1 || value == "1"){
            return true
        }
        false
    }

    public static String buildQuery(params){
        String query = "?"
        params.each{ key, value ->
            if(!reserved(key)){
                query += "${key}=${value}&"
            }
        }
        query.size() == 1 ? "" : query[0..-1-1]
    }

    public static boolean reserved(String param){
        param in ["controller", "action", "format"]
    }
}
