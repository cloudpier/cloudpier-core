Cloud Pier Core modules
==========================

This Repository includes all modules that compose Cloud Pier core application

# Current modules:
## adapter
Contains Centralized Adapter code  
## adapter-REST
Rest services used for communication with Remote Adapters
## api
Cloud Pier Interfaces 
## governance
Governance module encapsulates submodules that enable the governing of applications on PaaS providers
### governance-accounting
Accounting submodule  
### governance-monitoring
Monitoring submodule  
### governance-sla-decisor
SLA decisor submodule
### governance-sla-negotiator
SLA negotiator submodule  
### governance-ems
Execution and Management Services Submodule   
### governance-sla-client
SLA client submodule
### governance-sla-enforcement
SLA enforcement submodule  
## parent
Parent directory. It is used in order to build all the core modules and submodules with their required order  
## repository
Definition of repository objects and their supporting methods  
## semantic
Semantic ontologies models definitions
## soa
Service Oriented Architecture part of Cloud Pier Core
### soa-commons
Common Services
### soa-REST
RESt Service
### Misc
Various stuff. Mysql initial dump can be found here 

# Usage

## Dependencies
In order to build Cloud Pier core module a database setup is needed. A mysql dump can be found in Cloud Pier Misc folder. See wiki for more details and technical instructions.

## Deployment
Place your command line in directory called parent.
Compile:

    mvn clean install

When producing the war file for deploying in a servlet container you won't include the MySQL JDBC Connector in the classpath.
Instead, you will manually add the MySQL JDBC Connector in the servlet container.


