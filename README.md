# solwebsample
Tomcat servlet example using the sol-tomcat intagration for Solace into Apache Tomcat

See https://tomcat.apache.org/index.html for details about the Apache Tomcat project.

## Building

Requires maven, refers to all public maven java resources, 
builds a sample messaging servlet in a WAR package for deployment 
to Apache Tomcat app-servers.

Requires the sample Solace Tomcate JNDI resource integration available 
at https://github.com/koverton/sol-tomcat

```bash
mvn package
```

Produces `target/solwebsample.war` that can be deployed to Tomcat 
via it's application manager.

## Deploying 

After the full sol-tomcat library and all dependencies are added 
to the `$CATALINA_HOME/lib` directory, this application can be 
deployed to the server by navigating to http://<server>:8080/manager/html
and using the app-manager form for WAR file deployment.

## Configuring

This example expects the following JNDI resources to be configured:

* jms/ConnectionFactory: a JNDI connection-factory name to be looked up and used for all connections
* jms/Queue: a JNDI queue name to publish to and consume from
* jms/Topic: a JNDI topic name to subscribe to

The configurations for these examples can be found in `src/main/resources/conf/context-resources.xml`.

They should be added to the Tomcat server's `$CATALINA_HOME/conf/context.xml` configuration.

## Testing 

The sample application assumes the following entities are provisioned on the Solace broker's default msg-VPN:
* A queue named 'sample_queue', enabled with full permissions for all
* A JNDI connection-factory named 'JNDI/CF'
* A JNDI topic named 'JNDI/topic' mapped to a real Solace topic
* A JNDI queue named 'JNDI/sample_queue' mapped to the Solace queue 'sample_queue'

A script to create all of these on the default msg-VPN exists in `srd/main/resources/SEMP/SEMP_CREATE.sh`.

After deploying the WAR file and starting the servlet, navigate to the http://localhost:8080/solwebsample 
link from the Manager page, click on the 'Hello World!' link at the top of the page to issue a GET 
request to the servlet. This retrieves the latest stats on the messaging activity. If the app is not 
working you will see an error rather than the standard display.