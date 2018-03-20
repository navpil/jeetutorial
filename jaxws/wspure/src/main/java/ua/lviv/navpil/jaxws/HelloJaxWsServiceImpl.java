package ua.lviv.navpil.jaxws;

import javax.jws.WebService;

@WebService(endpointInterface = "ua.lviv.navpil.jaxws.HelloJaxWsService")
public class HelloJaxWsServiceImpl implements HelloJaxWsService {

    public HelloJaxWsServiceImpl() {
    }

    public String sayHelloWorld() {
        return "Hello Jax Ws";
    }
}
