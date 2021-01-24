/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.stockservice;

import static SharesBrokeringService.sharesbrokeringservice.*;
import javax.jws.WebService;
import javax.jws.WebMethod;
import stockSheet.Stocks;

/**
 *
 * @author Jack
 */
@WebService(serviceName = "BuyStocks")
public class BuyStocks {

    /**
     * Web service operation
     * @param buyQuantity
     * @param buyStockName
     * @return 
     */
    @WebMethod(operationName = "buyStocks")
    public int buyStocks(int buyQuantity, String buyStockName) {
        //String result = "Sorry, there are not enough available shares to make this transaction";
        int result = 0;
        Stocks myStocks = unmarshallFromFile("C:\\Users\\Jack\\Documents\\NetBeansProjects\\SharesBrokeringService\\stocks.xml");
                
        for (int x = 0; x < myStocks.getStockList().size(); x++) {
            if ((myStocks.getStockList().get(x).getCompanyName().equals(buyStockName)) && (myStocks.getStockList().get(x).getNoAvailableShares() >= buyQuantity)) {
                myStocks.getStockList().get(x).setNoAvailableShares(myStocks.getStockList().get(x).getNoAvailableShares() - buyQuantity);
                marshallToFile("C:\\Users\\Jack\\Documents\\NetBeansProjects\\SharesBrokeringService\\stocks.xml", myStocks);
                //result = "Successful transaction! You have bought " + buyQuantity + " shares.";
                result = 1;
            } 
        }
       
        return result;
    }
}
