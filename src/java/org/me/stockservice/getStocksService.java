/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.stockservice;

import static SharesBrokeringService.sharesbrokeringservice.*;
import currencyservice.ConverterService;
import currencyservice.ParseException_Exception;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.ws.WebServiceRef;
import stockSheet.Stock;
import stockSheet.Stocks;

/**
 *
 * @author Jack
 */
@WebService(serviceName = "getStocksService")
public class getStocksService {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/192.168.146.1_53981/CurrencyService/ConverterService.wsdl")
    private ConverterService service;

    /**
     * Web service operation
     * @param selectedCurrency
     * @param searchString
     * @param searchType
     * @return 
     * @throws javax.xml.datatype.DatatypeConfigurationException 
     * @throws currencyservice.ParseException_Exception 
     */
    @WebMethod(operationName = "getStocks")
    public List<Stock> getStocks(String selectedCurrency, String searchString, String searchType) throws DatatypeConfigurationException, ParseException_Exception {
        Stocks myStocks = unmarshallFromFile("C:\\Users\\Jack\\Documents\\NetBeansProjects\\SharesBrokeringService\\stocks.xml");
        String[] splitStr = selectedCurrency.split("-");
        String selectedCurrencySymbol = splitStr[0].trim();
        String selectedCurrencyName = splitStr[1].trim();
        double newCurrencyRate = getConversionRate(selectedCurrencySymbol);

        for (int x = 0; x < myStocks.getStockList().size(); x++) {
            // Stock currency is USD, convert to selected currency
            if (myStocks.getStockList().get(x).getPrice().getCurrency().equals("United States Dollar")) {
                myStocks.getStockList().get(x).getPrice().setCurrency(selectedCurrencyName);
                myStocks.getStockList().get(x).getPrice().setValue(round(myStocks.getStockList().get(x).getPrice().getValue().multiply(new BigDecimal(newCurrencyRate)), 2));
            // Stock currency is NOT USD, convert to USD then to selected currency
            } else {
                List<String> currencyList = getCurrencyCodes();
               
                // Get conversion rate back to USD
                double currentCurrencyToUSDRate = getConversionRate(findBaseCurrencySymbol(currencyList, myStocks, x));
                
                // Convert current currency back to base USD currency
                BigDecimal a = myStocks.getStockList().get(x).getPrice().getValue();
                BigDecimal b = new BigDecimal(currentCurrencyToUSDRate);
                myStocks.getStockList().get(x).getPrice().setValue(a.divide(b, 2, RoundingMode.HALF_UP));
               
                // Convert from USD to new currency
                myStocks.getStockList().get(x).getPrice().setCurrency(selectedCurrencyName);
                myStocks.getStockList().get(x).getPrice().setValue(round(myStocks.getStockList().get(x).getPrice().getValue().multiply(new BigDecimal(newCurrencyRate)), 2));
            }
        }
        
        //If search criteria is filled, then we want to search
        if ((!searchString.equals("")) && (!searchType.equals(""))) {
            myStocks = searchStocks(myStocks, searchString, searchType);
        }
        
        return myStocks.getStockList();
    }

    private double getConversionRate(java.lang.String arg0) throws ParseException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        currencyservice.CurrencyService port = service.getCurrencyServicePort();
        return port.getConversionRate(arg0);
    }

    private java.util.List<java.lang.String> getCurrencyCodes() throws ParseException_Exception {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        currencyservice.CurrencyService port = service.getCurrencyServicePort();
        return port.getCurrencyCodes();
    }
}
