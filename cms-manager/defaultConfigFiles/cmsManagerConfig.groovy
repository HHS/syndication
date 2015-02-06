//________________________________________
// Absolute URL and Context of Server     \______________________________________________
//_______________________________________________________________________________________

grails.serverURL = "http://localhost:8080/${appName}"

//________________________________________
// Database Configuration                 \______________________________________________
//_______________________________________________________________________________________

dataSource {
    pooled = true
    dbCreate = "update"
    //-----------------------------------------------
    url = "jdbc:mysql://localhost:3306/cms_manager"
    username = "root"
    password = ""
    //-----------------------------------------------
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = org.hibernate.dialect.MySQL5InnoDBDialect
    properties {
        jmxEnabled = true
        initialSize = 5
        maxActive = 50
        minIdle = 5
        maxIdle = 25
        maxWait = 10000
        maxAge = 10 * 60000
        timeBetweenEvictionRunsMillis = 5000
        minEvictableIdleTimeMillis = 60000
        validationQuery = "SELECT 1"
        validationQueryTimeout = 3
        validationInterval = 15000
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = false
        jdbcInterceptors = "ConnectionState"
        defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
    }
}

