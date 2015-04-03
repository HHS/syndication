# CMS Manager Installation and Configuration Guide


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