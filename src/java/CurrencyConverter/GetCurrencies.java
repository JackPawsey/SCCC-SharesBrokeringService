/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CurrencyConverter;

import currencyservice.ConverterService;
import currencyservice.ParseException_Exception;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Jack
 */
@WebService(serviceName = "GetCurrencies")
public class GetCurrencies {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/192.168.146.1_53981/CurrencyService/ConverterService.wsdl")
    private ConverterService service;

 
    /**
     * Web service operation
     * @return 
     * @throws currencyservice.ParseException_Exception 
     */
    @WebMethod(operationName = "GetCurrencyCodes")
    public List<String> GetCurrencyCodes() throws ParseException_Exception {
        List<String> currencyList = getCurrencyCodes();
        
        java.util.Collections.sort(currencyList);
        
        return currencyList;
    }

    private java.util.List<java.lang.String> getCurrencyCodes() throws ParseException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        currencyservice.CurrencyService port = service.getCurrencyServicePort();
        return port.getCurrencyCodes();
    }
}
