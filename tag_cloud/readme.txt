Tag Cloud Build Instructions

Configuration Instructions
In order to run it on your local box put these  lines in your tagCloud-config.groovy (in your home directory)

syndication.serverUrl='http://localhost:8080/Syndication'
syndication.apiPath='/api/v2'
tagcloud.auth.adminUsername = "admin"
tagcloud.auth.initialAdminPassword = "ct@csupersecretp@ssword"

In order to run it on ctacdev

syndication.serverUrl = "http://ctacdev.com:8090/Syndication"
syndication.apiPath = "/api/v2"
grails.serverURL = "http://ctacdev.com:8090/TagCloud"

tagcloud.auth.adminUsername	= "admin"
tagcloud.auth.initialAdminPassword = "password"
 dataSource {
            dbCreate = "create-drop"
            driverClassName = "com.mysql.jdbc.Driver"
            url = "jdbc:mysql://localhost:3306/tagcloud"
            username = 'root'
            password = ''
            properties {
                maxActive = -1
                minEvictableIdleTimeMillis = 1800000
                timeBetweenEvictionRunsMillis = 1800000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = false
                validationQuery = "SELECT 1"
                jdbcInterceptors = "ConnectionState"
            }
        }
In order to run it on production change the urls to the correct values and change dbCreate to “update”
Also change whatever other values you need to change in the dataSource such as username and password

How to do a build and run in Intellij

Edit the configuration for TagCloud so that the command line input is 
	run-app -Dserver.port=8084

Use the run command in Intellij.  This will compile your code and run it in your localhost on port 8084.
TagCloud will not work correctly unless you have a Syndication instance running that it can see.  This instance is the one specified by 
	syndication.serverUrl in your tagCloud-config.groovy configuration file

How to build a war and deploy it to a server

You build a war by using the grails command war This commands puts a war file in your target directory. 

ssh into your server and then 
	sudo su - tomcatsynd.  
	cd to ~/tomcat/webapps
shutdown the server with the command 
	~/tomcat/bin/shutdown.sh
 Copy the war to the server your best way.  I do this by putting the war file in my dropbox folder, getting the dropbox link and then wget the link with an argument -O TagCloud.war.
For example
	rm -rf TagCloud
	wget <dropbox link> -O TagCloud.war

then start your server with the command
	~/tomcat/bin/startup.sh
	
Watch your logs at startup to make sure that everything is going ok.  The command to watch the logs is
	tail -f ~/tomcat/logs/catalina.out
