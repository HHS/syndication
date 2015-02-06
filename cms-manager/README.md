# CMS Manager Installation and Configuration Guide

[TOC]

## Release Information

CMS Manager Version: **1.3.2**

Release Date: **TBD**

## Glossary of Terms

| Term | Definition |
| ---- | ---------- |
| Administrator | The operator(s) of CMS Manager's administrative functions. |
| CMS Manager | The web application to which this document refers. |
| Content Management System (CMS) | Any third party software application that uses CMS Manager to subscribe to and receive updates to Syndicated Content.
| Email Subscriber | Generically, an email address that is configured to receive update notifications of Syndicated Content, and optionally include the Syndicated Content as an email attachment. |
| Rhythmyx Content Editor | The operator(s) of CMS Manager's Rhythmyx subscription management functions. |
| Rhythmyx Subscriber | A Rhythmyx Content Management System configured to allow the importation (ingestion) and updating of Syndicated Content as Rhythmyx content items. |
| Subscriber | Generically, the organization or entity to which an Email Subscriber or Rhythmyx Subscriber belongs. |
| Subscription | A association between a Subscriber and a single piece of Syndicated Content, where the Subscriber receives content change notifications or (push) deliveries of Syndicated Content. |
| Syndicated Content | Any piece of syndicated web content currently managed by the Syndication Application. |
| Syndication Application | The web application which manages the connection to the content syndication network . |
| Syndication Network | The syndication network as a whole including efforts put forth by CDC and instances deployed at various agencies such as CDC, FDA, and HHS. |

## About

CMS Manager provides subscription management features for Syndicated content, and authentication services for publisgit hing content to the Syndication network via the Syndication application.

## Features

This initial release provides the following capabilities:

- An administrator UI for managing:
	- Subscriber organizations
	- Rhythmyx subscribers and subscriptions
	- Email subscribers and subscriptions
	- REST API key agreements
	- User accounts and roles
- API key authentication services (REST) for authorizing requests to secured resources.
- Debug services (REST) for use by client applications to troubleshoot authentication issues.
- A single administrator account for accessing the CMS Manager's administration functions.
- A Rhythmyx user role for the importation of syndicated content into a Rhythmyx CMS instance.

## Software Dependencies

The current version of CMS Manager has tested against the following software dependencies and versions:

| Dependency | Version  |
| ---------- | -------: |
| Tomcat 	 | 7.0.42   |
| Java   	 | 1.7.0_65 |
| Ubuntu 	 | 12.04.2  |
| Rhythmyx   | 7.0.3    |
| RabbitMQ   | 3.3.4    |

## Installation and Configuration

### Basic installation

CMS Manager is deployed as a standard Java WAR file into a Apache Tomcat web server.

To deploy CMS Manager, copy the *CmsManager.war*  (do not include the version number in the filename) into the *${TOMCAT_HOME}/webapps* directory. Optionally, you can deploy CMS Manager using Tomcat's Manager App, using */CmsManager* as the context path.

Below are the links to the relevant Apache Tomcat HOW-TO guides for further reference:

[Tomcat 7.0 Deployer HOW-TO Guide](http://tomcat.apache.org/tomcat-7.0-doc/deployer-howto.html)

[Tomcat 7.0 Manager HOW-TO Guide](http://tomcat.apache.org/tomcat-7.0-doc/manager-howto.html)

### Configuration

CMS Manager requires an external configuration to exist in the home directory of the Ubuntu system account that owns the Apache Tomcat dircetory tree. The name of this configuration file must be *cms_manager_runtime_config.groovy*.

##### Default Administrator Password

At a minimum, the following property must be set in order for the CMS Manager appication to deploy without error:

```groovy
springsecurity.cmsManager.defaultPassword='password'
```

Change the value of *password* to the desired administrator password prior to deploying the application for the first time.

##### HHS Media Services REST API (Syndication API)
