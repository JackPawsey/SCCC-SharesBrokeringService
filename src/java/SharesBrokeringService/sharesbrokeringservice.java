package SharesBrokeringService;

import stockSheet.Stocks;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jack
 */
public class sharesbrokeringservice {
    /**
     * @param args the command line arguments
     * @throws javax.xml.datatype.DatatypeConfigurationException
     */
    public static void main(String[] args) throws DatatypeConfigurationException {
    }
    
    // Read stock data from file
    public static Stocks unmarshallFromFile(String fileName) {
        Stocks myStocks = new Stocks();
        try {
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(myStocks.getClass().getPackage().getName());
            javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
            myStocks = (Stocks) unmarshaller.unmarshal(new java.io.File(fileName)); //NOI18N
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
        return myStocks;
    }
    
    // Write stock data to file
    public static void marshallToFile(String fileName, Stocks myStocks) {
        try {        
            javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(myStocks.getClass().getPackage().getName());
            javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File file = new File(fileName);
            marshaller.marshal(myStocks, file);
        } catch (javax.xml.bind.JAXBException ex) {
            // XXXTODO Handle exception
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE, null, ex); //NOI18N
        }
    }
    
    // Add valid search results to new stock list
    public static Stocks searchStocks(Stocks myStocks, String searchString, String searchType) {
        Stocks searchResults = new Stocks();

        switch (searchType.toLowerCase()) {
            case "company:":
                for (int x = 0; x < myStocks.getStockList().size(); x++) {
                    if (myStocks.getStockList().get(x).getCompanyName().toLowerCase().contains(searchString.toLowerCase())) {
                        searchResults.getStockList().add(myStocks.getStockList().get(x));
                    }
                }   break;
            case "symbol:":
                for (int x = 0; x < myStocks.getStockList().size(); x++) {
                    if (myStocks.getStockList().get(x).getCompanySymbol().toLowerCase().contains(searchString.toLowerCase())) {
                        searchResults.getStockList().add(myStocks.getStockList().get(x));
                    }
                }   break;
            case "available shares greater than:":
                for (int x = 0; x < myStocks.getStockList().size(); x++) {
                    if (myStocks.getStockList().get(x).getNoAvailableShares() > Integer.parseInt(searchString)) {
                        searchResults.getStockList().add(myStocks.getStockList().get(x));
                    }
                }   break;
            case "available shares less than:":
                for (int x = 0; x < myStocks.getStockList().size(); x++) {
                    if (myStocks.getStockList().get(x).getNoAvailableShares() < Integer.parseInt(searchString)) {
                        searchResults.getStockList().add(myStocks.getStockList().get(x));
                    }
                }   break;
            case "price higher than:":
                for (int x = 0; x < myStocks.getStockList().size(); x++) {
                    if (myStocks.getStockList().get(x).getPrice().getValue().intValue() > Integer.parseInt(searchString)) {
                        searchResults.getStockList().add(myStocks.getStockList().get(x));
                    }
                }   break;
            case "price lower than:":
                for (int x = 0; x < myStocks.getStockList().size(); x++) {
                    if (myStocks.getStockList().get(x).getPrice().getValue().intValue() < Integer.parseInt(searchString)) {
                        searchResults.getStockList().add(myStocks.getStockList().get(x));
                    }
                }   break;
            default:
                break;
        }
        
        return searchResults;
    }
    
    // Get stock original currency symbol
    public static String findBaseCurrencySymbol(List<String> currencyList, Stocks myStocks, int index) {
        String currentCurrencySymbol = null;
                
        for (int currency = 0; currency < currencyList.size(); currency++) {
                    String[] splitCurrency = currencyList.get(currency).split("-");
                    String current_currency = splitCurrency[1].trim();
                    if (myStocks.getStockList().get(index).getPrice().getCurrency().equals(current_currency)) {
                       currentCurrencySymbol = splitCurrency[0].trim();
                    }
                }
        
        return currentCurrencySymbol;
    }
    
    // Round big decimal value to 2 decimal places
    public static BigDecimal round(BigDecimal value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        value = value.setScale(places, RoundingMode.HALF_UP);
        return value;
    }
    
    // Create new stock and write to file
    public static String createNewStock(String companyName, String companySymbol, int noAvailableShares, String currency, BigDecimal value) throws DatatypeConfigurationException {
        Stocks myStocks = unmarshallFromFile("C:\\Users\\Jack\\Documents\\NetBeansProjects\\SharesBrokeringService\\stocks.xml");
        stockSheet.Stock newStock = new stockSheet.Stock();
        stockSheet.SharePrice newStockPrice  = new stockSheet.SharePrice();
        
        String result = "This stock already exists!";
        boolean uniqueStock = true;
        
        for (int x = 0; x < myStocks.getStockList().size(); x++) {
            if (myStocks.getStockList().get(x).getCompanyName().equals(companyName)) {
                uniqueStock = false;
            } 
        }
        
        if (uniqueStock) {
            newStock.setCompanyName(companyName);
            newStock.setCompanySymbol(companySymbol);
            newStock.setNoAvailableShares(noAvailableShares);
            newStockPrice.setCurrency(currency);
            newStockPrice.setValue(value);
            
            DatatypeFactory df = DatatypeFactory.newInstance();
            GregorianCalendar calendar = new GregorianCalendar();
            XMLGregorianCalendar lastUpdateDateTime = df.newXMLGregorianCalendar(calendar);
            
            newStockPrice.setLastUpdateDate(lastUpdateDateTime);
            
            newStock.setPrice(newStockPrice);

            myStocks.getStockList().add(newStock);

            marshallToFile("C:\\Users\\Jack\\Documents\\NetBeansProjects\\SharesBrokeringService\\stocks.xml", myStocks);

            result = "Stock created successfully!";
        }
        return result;
    }
    
    // Delete existing stock and remove it from file
    public static String deleteStock(String companyName) {
        Stocks myStocks = unmarshallFromFile("C:\\Users\\Jack\\Documents\\NetBeansProjects\\SharesBrokeringService\\stocks.xml");
        String result = "Error, stock not found!";
        
        for (int x = 0; x < myStocks.getStockList().size(); x++) {
            if (myStocks.getStockList().get(x).getCompanyName().equals(companyName)) {
                myStocks.getStockList().remove(x);
                marshallToFile("C:\\Users\\Jack\\Documents\\NetBeansProjects\\SharesBrokeringService\\stocks.xml", myStocks);
                result = "Stock deleted successfully!";
            }
        }
        
        return result;
    }
    
    // Print all stocks to console
    public static void printStockInfo(Stocks myStocks) throws DatatypeConfigurationException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        DatatypeFactory df2 = DatatypeFactory.newInstance();
        
        for (int x = 0; x < myStocks.getStockList().size(); x++) {
            XMLGregorianCalendar xmlgc = df2.newXMLGregorianCalendar(myStocks.getStockList().get(x).getPrice().getLastUpdateDate().toGregorianCalendar());
            GregorianCalendar gc = xmlgc.toGregorianCalendar();
            String dateTime = sdf.format(gc.getTime());
            
            System.out.println(myStocks.getStockList().get(x).getCompanyName() + ", " + myStocks.getStockList().get(x).getCompanySymbol() + ", " + myStocks.getStockList().get(x).getNoAvailableShares() + ", " + myStocks.getStockList().get(x).getPrice().getCurrency() + ", " + myStocks.getStockList().get(x).getPrice().getValue() + ", " + dateTime);
        }
    }
    
//    public static void createStocks() throws DatatypeConfigurationException {
//        Stocks myStocks = new Stocks();
//        DatatypeFactory df = DatatypeFactory.newInstance();
//        GregorianCalendar calendar = new GregorianCalendar();
//        
//        
//        //stock1
//        stockSheet.Stock stock1 = new stockSheet.Stock();
//        stockSheet.SharePrice stock1Price  = new stockSheet.SharePrice(); 
//        
//        stock1.setCompanyName("Apple");
//        stock1.setCompanySymbol("APPL");
//        stock1.setNoAvailableShares(100);
//        
//        stock1Price.setCurrency("United States Dollar");
//        BigDecimal bd1 = new BigDecimal("12.50"); 
//        stock1Price.setValue(bd1);
//        calendar.set(1997, 9, 24, 3, 15, 15);
//        XMLGregorianCalendar dateTime1 = df.newXMLGregorianCalendar(calendar);
//        stock1Price.setLastUpdateDate(dateTime1);
//        
//        stock1.setPrice(stock1Price);
//        myStocks.getStockList().add(stock1);
//        
//        //stock2
//        stockSheet.Stock stock2 = new stockSheet.Stock();
//        stockSheet.SharePrice stock2Price  = new stockSheet.SharePrice(); 
//        
//        stock2.setCompanyName("Microsoft");
//        stock2.setCompanySymbol("MICR");
//        stock2.setNoAvailableShares(200);
//        
//        stock2Price.setCurrency("United States Dollar");
//        BigDecimal bd2 = new BigDecimal("20.00"); 
//        stock2Price.setValue(bd1);
//        calendar.set(1998, 5, 45, 2, 2, 54);
//        XMLGregorianCalendar dateTime2 = df.newXMLGregorianCalendar(calendar);
//        stock2Price.setLastUpdateDate(dateTime2);
//        
//        stock2.setPrice(stock2Price);
//        myStocks.getStockList().add(stock2);
//        
//        //stock3
//        stockSheet.Stock stock3 = new stockSheet.Stock();
//        stockSheet.SharePrice stock3Price  = new stockSheet.SharePrice(); 
//        
//        stock3.setCompanyName("Intel");
//        stock3.setCompanySymbol("INTL");
//        stock3.setNoAvailableShares(100);
//        
//        stock3Price.setCurrency("United States Dollar");
//        BigDecimal bd3 = new BigDecimal("15.00"); 
//        stock3Price.setValue(bd3);
//        calendar.set(1845, 1, 23, 8, 5, 60);
//        XMLGregorianCalendar dateTime3 = df.newXMLGregorianCalendar(calendar);
//        stock3Price.setLastUpdateDate(dateTime3);
//        
//        stock3.setPrice(stock3Price);
//        myStocks.getStockList().add(stock3);
//        
//        //stock4
//        stockSheet.Stock stock4 = new stockSheet.Stock();
//        stockSheet.SharePrice stock4Price  = new stockSheet.SharePrice(); 
//        
//        stock4.setCompanyName("Tesla");
//        stock4.setCompanySymbol("TSLA");
//        stock4.setNoAvailableShares(100);
//        
//        stock4Price.setCurrency("United States Dollar");
//        BigDecimal bd4 = new BigDecimal("10.00"); 
//        stock4Price.setValue(bd4);
//        calendar.set(1991, 10, 18, 3, 14, 12);
//        XMLGregorianCalendar dateTime4 = df.newXMLGregorianCalendar(calendar);
//        stock4Price.setLastUpdateDate(dateTime4);
//        
//        stock4.setPrice(stock4Price);
//        myStocks.getStockList().add(stock4);
//        
//        //stock5
//        stockSheet.Stock stock5 = new stockSheet.Stock();
//        stockSheet.SharePrice stock5Price  = new stockSheet.SharePrice(); 
//        
//        stock5.setCompanyName("Nvidia");
//        stock5.setCompanySymbol("NVDA");
//        stock5.setNoAvailableShares(100);
//        
//        stock5Price.setCurrency("United States Dollar");
//        BigDecimal bd5 = new BigDecimal("20.00"); 
//        stock5Price.setValue(bd5);
//        calendar.set(2077, 6, 21, 9, 32, 43);
//        XMLGregorianCalendar dateTime5 = df.newXMLGregorianCalendar(calendar);
//        stock5Price.setLastUpdateDate(dateTime5);
//        
//        stock5.setPrice(stock5Price);
//        myStocks.getStockList().add(stock5);
//        
//        //stock6
//        stockSheet.Stock stock6 = new stockSheet.Stock();
//        stockSheet.SharePrice stock6Price  = new stockSheet.SharePrice(); 
//        
//        stock6.setCompanyName("Advanced Micro Devices");
//        stock6.setCompanySymbol("AMD");
//        stock6.setNoAvailableShares(100);
//        
//        stock6Price.setCurrency("United States Dollar");
//        BigDecimal bd6 = new BigDecimal("18.25"); 
//        stock6Price.setValue(bd6);
//        calendar.set(2001, 3, 29, 12, 12, 12);
//        XMLGregorianCalendar dateTime6 = df.newXMLGregorianCalendar(calendar);
//        stock6Price.setLastUpdateDate(dateTime6);
//        
//        stock6.setPrice(stock6Price);
//        myStocks.getStockList().add(stock6);
//        
//        //stock7
//        stockSheet.Stock stock7 = new stockSheet.Stock();
//        stockSheet.SharePrice stock7Price  = new stockSheet.SharePrice(); 
//        
//        stock7.setCompanyName("SpaceX");
//        stock7.setCompanySymbol("SPEX");
//        stock7.setNoAvailableShares(100);
//        
//        stock7Price.setCurrency("United States Dollar");
//        BigDecimal bd7 = new BigDecimal("420"); 
//        stock7Price.setValue(bd7);
//        calendar.set(2020, 2, 20, 2, 20, 20);
//        XMLGregorianCalendar dateTime7 = df.newXMLGregorianCalendar(calendar);
//        stock7Price.setLastUpdateDate(dateTime7);
//        
//        stock7.setPrice(stock7Price);
//        myStocks.getStockList().add(stock7);
//        
//        //stock8
//        stockSheet.Stock stock8 = new stockSheet.Stock();
//        stockSheet.SharePrice stock8Price  = new stockSheet.SharePrice(); 
//        
//        stock8.setCompanyName("Google");
//        stock8.setCompanySymbol("GGLE");
//        stock8.setNoAvailableShares(100);
//        
//        stock8Price.setCurrency("United States Dollar");
//        BigDecimal bd8 = new BigDecimal("34.67"); 
//        stock8Price.setValue(bd8);
//        calendar.set(2015, 8, 27, 3, 30, 30);
//        XMLGregorianCalendar dateTime8 = df.newXMLGregorianCalendar(calendar);
//        stock8Price.setLastUpdateDate(dateTime8);
//        
//        stock8.setPrice(stock8Price);
//        myStocks.getStockList().add(stock8);
//        
//        //stock9
//        stockSheet.Stock stock9 = new stockSheet.Stock();
//        stockSheet.SharePrice stock9Price  = new stockSheet.SharePrice(); 
//        
//        stock9.setCompanyName("Amazon");
//        stock9.setCompanySymbol("AMZN");
//        stock9.setNoAvailableShares(100);
//        
//        stock9Price.setCurrency("United States Dollar");
//        BigDecimal bd9 = new BigDecimal("28.37"); 
//        stock9Price.setValue(bd9);
//        calendar.set(2019, 9, 9, 9, 18, 18);
//        XMLGregorianCalendar dateTime9 = df.newXMLGregorianCalendar(calendar);
//        stock9Price.setLastUpdateDate(dateTime9);
//        
//        stock9.setPrice(stock9Price);
//        myStocks.getStockList().add(stock9);
//        
//        //stock9
//        stockSheet.Stock stock10 = new stockSheet.Stock();
//        stockSheet.SharePrice stock10Price  = new stockSheet.SharePrice(); 
//        
//        stock10.setCompanyName("Samsung");
//        stock10.setCompanySymbol("SMSG");
//        stock10.setNoAvailableShares(100);
//        
//        stock10Price.setCurrency("United States Dollar");
//        BigDecimal bd10 = new BigDecimal("21.11"); 
//        stock10Price.setValue(bd10);
//        calendar.set(2011, 1, 13, 3, 13, 13);
//        XMLGregorianCalendar dateTime10 = df.newXMLGregorianCalendar(calendar);
//        stock10Price.setLastUpdateDate(dateTime10);
//        
//        stock10.setPrice(stock10Price);
//        myStocks.getStockList().add(stock10);
//        
//        marshallToFile("C:\\Users\\Jack\\Documents\\NetBeansProjects\\SharesBrokeringService\\stocks.xml", myStocks);
//    }
}