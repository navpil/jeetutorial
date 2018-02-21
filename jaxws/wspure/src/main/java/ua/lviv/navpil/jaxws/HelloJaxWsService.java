package ua.lviv.navpil.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface HelloJaxWsService {

    @WebMethod
    String sayHelloWorld();
}
