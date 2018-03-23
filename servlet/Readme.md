# Annotations vs web.xml

`web.xml` configuration takes precedence over `@WebServlet` configuration, but `mvn jetty:run` fails if `urlPatterns`
are not specified in the `@WebServlet`

This is the correct way:

 1. Name the servlet in annotation, set the urlPatterns and you may specify some init parameters
 2. Refer to the servlet under the same name and the class name in web.xml
 3. You may override the urlPatterns in web.xml with servlet-mapping, which will override the annotations
 4. You may override init parameters in web.xml
 

https://stackoverflow.com/questions/2311065/what-is-web-xml-file-and-what-are-all-things-can-i-do-with-it

https://stackoverflow.com/questions/3468150/using-special-auto-start-servlet-to-initialize-on-startup-and-share-application

https://docs.oracle.com/cd/E13222_01/wls/docs70/webapp/web_xml.html#1045762