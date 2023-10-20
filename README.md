# jeetutorial
Project to try different JEE technologies and alternatives

## JPA

Java persistence API 2.2, JSR 338.

Implementations:
    
 - Eclipselink (reference)
 - Hibernate
 
Alternatives:
 
 - JDBC
 - MyBatis
 - Hibernate
 - Spring Data


## Debugging jetty
`set MAVEN_DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=1042"`

or

`set MAVEN_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=1042`

`mvn jetty:run`

## CDI

Weld is RI for CDI. It can be included into the Tomcat, however it work only in listeners, servlets and filters.
It will not by default inject stuff into the JAX-WS services for example.

https://memorynotfound.com/install-and-configure-cdi-in-tomcat/
https://developer.jboss.org/thread/179870
https://www.mkyong.com/webservices/jax-ws/jax-ws-spring-integration-example/
https://bthurley.wordpress.com/2014/04/27/web-services-with-jax-ws-jaxb-and-spring/

In Payara you need to add beans.xml to the WEB-INF or annotate your bean with @ApplicationScoped the beans to be found. 
@ManagedBean does not work 

## JAX-WS

Payara won't see the wsdls when old-style web.xml is used (with DOCTYPE and stuff).
It's not required to generated wsdls with jaxws-maven-plugin. 

http://www.archive.ricston.com/blog/contract-cxf-springws/

Wspure with no Dependency Injection
 - com.sun.xml.ws:jaxws-rt - Done (DI is possible to do with some obsolete library, which was not updated for 8 years)
 - Apache CXF (DI is done with Spring, I tried only XML)
 - Spring WS 
 http://projects.spring.io/spring-ws/#quick-start
 - Spring Boot
 https://spring.io/guides/gs/producing-web-service/
 - Application Server (Payara) - Done (DI is done automatically there)
 
## Websockets

Using Jakarta (requires Tomcat 10).
Taken and adapted (simplified) from https://www.baeldung.com/java-websockets

Minimal example.
No additional libraries, like gson or slf4j included.

Build in `websockets` using

    mvn package

Then deploy resulting war file to Tomcat and open `http://localhost:8080/websockets`
in two browser tabs.
Connect with two different names and try to chat.
