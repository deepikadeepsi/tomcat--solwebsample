# solwebsample
Tomcat servlet example using the sol-tomcat intagration for Solace into Tomcat webserver

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

After deploying the WAR file and starting the servlet, avigate to the http://localhost:8080/solwebsample 
link from the Manager page, click on the 'Hello World!' link at the top of the page to issue a GET 
request to the servlet. This retrieves the latest stats on the messaging activity. If the app is not 
working you will see an error rather than the standard display.