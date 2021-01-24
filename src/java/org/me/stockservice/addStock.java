/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.stockservice;

import javax.jws.WebService;
import javax.jws.WebMethod;
import static SharesBrokeringService.sharesbrokeringservice.*;
import java.math.BigDecimal;
import javax.xml.datatype.DatatypeConfigurationException;

/**
 *
 * @author Jack
 */
@WebService(serviceName = "addStock")
public class addStock {

    /**
     * Web service operation
     * @param companyName
     * @param companySymbol
     * @param noAvailableShares
     * @param currency
     * @param value
     * @return 
     * @throws javax.xml.datatype.DatatypeConfigurationException 
     */
    @WebMethod(operationName = "addStock")
    public String addStock(String companyName, String companySymbol, int noAvailableShares, String currency, BigDecimal value) throws DatatypeConfigurationException {
        String result = createNewStock(companyName, companySymbol, noAvailableShares, currency, value);
      
        return result;
    }
}
