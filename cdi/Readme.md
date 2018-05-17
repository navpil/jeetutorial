# Weld CDI and Jetty

There may be issues with Jetty and Weld. This is the minimal working setup.

There is no special configuration in `web.xml` but `jetty-web.xml` is required.

Because `jetty-web.xml` may be needed only for development purposes, but not for production
(we might want to deploy it to Tomcat for example)
I have created a special `dev` folder on the same level as `main` and `test`.
To pickup this directory `maven-war-plugin` configuration was altered in `pom.xml`
To run jetty, you need to activate dev profile and `jetty:run-war` instead of `jetty:run`,
because the latter does not pick up `maven-war-plugin` configuration:

    > mvn -Pdev jetty:run-war 
    
## "But dev profile complicates this minimal setup" 

In case you don't want all these problems with profiles:  
  1. move `jetty-web.xml` from `src/dev/webapp/WEB-INF` into `src/main/webapp/WEB-INF`, 
  2. remove `profiles` from the `pom.xml` 
  3. delete `src/dev` folder.

Then setup will really be minimal.

## "What about Tomcat?"

    mvn verify
    
And deploy the `.war` file to Tomcat

 > `verify` is the stage after `package` and before `install`. We do not always need to install the `.war` file
 to the repository. 
