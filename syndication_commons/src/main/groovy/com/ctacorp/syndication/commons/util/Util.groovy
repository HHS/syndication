package com.ctacorp.syndication.commons.util

import grails.web.servlet.mvc.GrailsParameterMap

class Util {
    public static int getMax(params) {
        if(!params || !params.max){
            return 20
        }
        params = new GrailsParameterMap(params, null)
        int max = Math.min(params.int('max', 20), 1000)
        return max > 0 ? max : 20
    }

    public static boolean isTrue(value, boolean defaultValue = false){
        if(value == null){
            return defaultValue
        }
        if(value == true || value.toString().toLowerCase() == "true" || value == 1 || value == "1"){
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
        // return empty string if no params, and truncate last '&' if there were
        query.size() == 1 ? "" : query[0..-2]
    }

    public static boolean reserved(String param){
        param in ["controller", "action", "format"]
    }
}
