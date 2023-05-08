# SharesBrokeringService

Uni third year coursework

The Shares Brokering Service stores a list of stocks in an .XML file which it then serves to the client via the SOAP protocol.

The stocks are held in an .XSD schema, each stock instance has the following attributes:  
  CompanyName  
  CompanySymbol
  NoAvailableShares  
  Currency  
  Value  
  LastUpdateDate
 
The service connects to the CurrencyService to retreive currency codes and conversion rates
