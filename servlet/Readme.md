# Annotations vs web.xml

`web.xml` configuration takes precedence over `@WebServlet` configuration, but `mvn jetty:run` fails if `urlPatterns`
are not specified in the `@WebServlet`

However `mvn jetty:run` does not fail when entry in `web.xml` is like this (note that the servlet-class is provided):

    <servlet>
        <servlet-name>showPeopleServlet2</servlet-name>
        <servlet-class>ua.lviv.navpil.ShowPeopleServlet</servlet-class>
        ...
    </servlet>



https://stackoverflow.com/questions/2311065/what-is-web-xml-file-and-what-are-all-things-can-i-do-with-it

https://stackoverflow.com/questions/3468150/using-special-auto-start-servlet-to-initialize-on-startup-and-share-application

https://docs.oracle.com/cd/E13222_01/wls/docs70/webapp/web_xml.html#1045762