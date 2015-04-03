# Installation and Configuration

## Software Dependencies

The current version of CMS Manager has tested against the following software dependencies and versions:

| Dependency | Version  |
| ---------- | -------: |
| Tomcat 	 | 7.0.42   |
| Java   	 | 1.7.0_65 |
| Ubuntu 	 | 12.04.2  |
| Rhythmyx   | 7.0.3    |
| RabbitMQ   | 3.3.4    |

## Basic installation

CMS Manager is deployed as a standard Java WAR file into a Apache Tomcat web server.

To deploy CMS Manager, copy the *CmsManager.war*  (do not include the version number in the filename) into the *${TOMCAT_HOME}/webapps* directory. Optionally, you can deploy CMS Manager using Tomcat's Manager App, using */CmsManager* as the context path.

Below are the links to the relevant Apache Tomcat HOW-TO guides for further reference:

[Tomcat 7.0 Deployer HOW-TO Guide](http://tomcat.apache.org/tomcat-7.0-doc/deployer-howto.html)

[Tomcat 7.0 Manager HOW-TO Guide](http://tomcat.apache.org/tomcat-7.0-doc/manager-howto.html)

## Configuration

CMS Manager requires an external configuration to exist in the home directory of the Ubuntu system account that owns the Apache Tomcat dircetory tree. The name of this configuration file must be *cms_manager_runtime_config.groovy*.

#### Default Administrator Password

At a minimum, the following property must be set in order for the CMS Manager appication to deploy without error:

```groovy
springsecurity.cmsManager.defaultPassword='password'
```

Change the value of *password* to the desired administrator password prior to deploying the application for the first time.

#### HHS Media Services REST API (Syndication API)
