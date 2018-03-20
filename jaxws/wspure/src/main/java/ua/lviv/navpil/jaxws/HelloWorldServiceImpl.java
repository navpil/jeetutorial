package ua.lviv.navpil.jaxws;

import javax.jws.WebService;

@WebService(endpointInterface = "ua.lviv.navpil.jaxws.HelloWorldService")
public class HelloWorldServiceImpl implements HelloWorldService {

    public HelloWorldServiceImpl(){}

    public String sayHelloWorld() {
        return "Hello world";
    }
}
