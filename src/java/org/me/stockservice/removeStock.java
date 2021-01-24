/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.stockservice;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import static SharesBrokeringService.sharesbrokeringservice.*;

/**
 *
 * @author Jack
 */
@WebService(serviceName = "removeStock")
public class removeStock {

    /**
     * Web service operation
     * @param companyName
     * @return 
     */
    @WebMethod(operationName = "removeStock")
    public String removeStock(@WebParam(name = "companyName") String companyName) {
        String result = deleteStock(companyName);
        
        return result;
    }
}
