package ua.lviv.navpil.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


@WebService
//@SOAPBinding
//        (
//        style = javax.jws.soap.SOAPBinding.Style.RPC,
//        use = SOAPBinding.Use.ENCODED
////        parameterStyle = SOAPBinding.ParameterStyle.BARE
//
//)
public interface HelloWorldService {

    @WebMethod
    String sayHelloWorld();
}
