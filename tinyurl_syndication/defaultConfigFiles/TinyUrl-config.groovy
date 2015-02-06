grails.serverURL = "http://<host>:<port>[/context]"

dataSource {
    dbCreate = "update" //one of update, create-drop
    driverClassName = "com.mysql.jdbc.Driver"
    url = "jdbc:mysql://<dbAddress>:3306/<dbName>"
    username = '<username>'
    password = '<password>'
    properties {
        maxActive = -1
        minEvictableIdleTimeMillis=1800000
        timeBetweenEvictionRunsMillis=1800000
        numTestsPerEvictionRun=3
        testOnBorrow=true
        testWhileIdle=true
        testOnReturn=false
        validationQuery="SELECT 1"
        jdbcInterceptors="ConnectionState"
    }
}

