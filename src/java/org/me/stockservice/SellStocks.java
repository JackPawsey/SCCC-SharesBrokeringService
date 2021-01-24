/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.stockservice;

import static SharesBrokeringService.sharesbrokeringservice.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import stockSheet.Stocks;

/**
 *
 * @author Jack
 */
@WebService(serviceName = "SellStocks")
public class SellStocks {
    
    /**
     * Web service operation
     * @param sellQuantity
     * @param sellStockName
     * @return 
     */
    
    @WebMethod(operationName = "SellStocks")
    public int SellStocks(int sellQuantity, String sellStockName) {
        int result = 0;
        Stocks myStocks = unmarshallFromFile("C:\\Users\\Jack\\Documents\\NetBeansProjects\\SharesBrokeringService\\stocks.xml");

        for (int x = 0; x < myStocks.getStockList().size(); x++) {
            if (myStocks.getStockList().get(x).getCompanyName().equals(sellStockName)) {
                myStocks.getStockList().get(x).setNoAvailableShares(myStocks.getStockList().get(x).getNoAvailableShares() + sellQuantity);
                marshallToFile("C:\\Users\\Jack\\Documents\\NetBeansProjects\\SharesBrokeringService\\stocks.xml", myStocks);

                result = 1;
            }
        }

        return result;
    }
}
