package config

import groovy.json.JsonSlurper
import groovy.sql.Sql

/**
 * Created by sgates on 10/1/14.
 */
class ConfigLoader {
    def sql

    ConfigLoader(){
        sql = Sql.newInstance( 'jdbc:h2:data/localDatabase', 'sa', '', 'org.h2.Driver' )
        initTables()
    }

    Map<String, String> getConfig(){
        def config = [:]
        sql.eachRow("SELECT * FROM config"){ row ->
            config.privateKey = row.private_key
            config.publicKey = row.public_key
            config.secret = row.secret
            config.apiUrl = row.api_url
        }
        config
    }

    String saveConfig(String publicKey, String privateKey, String secret, String apiUrl){
        if(apiUrl.endsWith("/")){
            apiUrl = apiUrl[0..-2]
        }
        try {
            if (sql.firstRow("SELECT * FROM config")) {
                sql.executeUpdate("update config set public_key=${publicKey}, private_key=${privateKey}, secret=${secret}, api_url=${apiUrl} where id='1'")
            } else {
                def config = sql.dataSet("config")
                config.add(public_key: publicKey, private_key: privateKey, secret: secret, api_url:apiUrl)
            }
            return "success"
        } catch(e){
            println "Error Saving"
            return "There was an error saving!"
        }
    }

    String importKeys(String keyText) {
        try {
            keyText = keyText.trim()
            JsonSlurper slurp = new JsonSlurper()
            def json = slurp.parseText(keyText)

            def config = getConfig()
            return saveConfig(json."Public Key", json."Private Key", json."Secret Key", config.apiUrl)
        } catch (e){
            println "Error Parsing JSON"
            return "Error parsing JSON!"
        }
    }


    private initTables(){
        sql.execute('''
        CREATE TABLE IF NOT EXISTS config(
            id MEDIUMINT NOT NULL AUTO_INCREMENT,
            public_key varchar(255),
            private_key varchar(255),
            secret varchar(255),
            api_url varchar(255),
            PRIMARY KEY (id))
        '''
        )
    }
}
