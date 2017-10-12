## Weather Data Generator
  This is an application written in java which predicts weather conditions - <b>temperature</b>, <b>pressure</b> and <b>humidity</b> for a set of predefined locations. The set of locations for which weather needs to be predicted is specified in a static file. For prediction, this application makes use of a popular time series forecasting model called <b>Autoregressive Integrated Moving Average (ARIMA) </b>. The ARIMA model predicts future conditions based on the input values supplied to the model; historical weather data is being supplied to the model as input values. The historical weather details are downloaded from <b>Bureau of Meteorology, Australia</b> website. 
  
## How it Works
  In order to predict future condition(s), this program make use the popular time series forecasting model called <b>Autoregressive Integrated Moving Average (ARIMA)</b>. The accuracy of result provided by the model depends on the input values supplied. The historical weather conditions will be supplied to the model as input variables for prediction. The historical values will be read from <b> Bureau of Meteorology(BOM), Australia</b> website. The BOM site contain weather details for different locations across Australia.
  
  For forecasting, one fort nights data - before the forecasting date and after the forecasting date will be supplied as input values. For example, if we are doing forecasting for 15th of November, data from 1st November to 29th November will be supplied.  
  
  The model make use of the input values supplied to predict a future condition. 
  
  The Java API provided by ARIMA is being used for this program. The detail of the API can be found [here](https://github.com/Workday/timeseries-forecast). 
  
  The usage of API is as below. 
  ```
  ForecastResult forecastResult = Arima.forecast_arima(histValues, forecastSize, params);
  ```
   histValues - historial values supplied to the model for forecasting
  
   forecastSize - the number of days to do the forecasting
  
  For each of the weather condition (for example, temperature), the above method will be called to get the future data. 
  
  Once the forecasted results are availble, it will be written to an output file in a format as below.
  
  ```
  Forecast_Date|City|Latitude,Longitude,Elevation|Local_Time|Condition|Temperature|Pressure|Relative_Humidity
  
  ```
  
  Sample Output for Sydney location will be as given below:-
  
  ```
  2017-12-12|SYDNEY|-33.86,151.12,58|2017-10-11T04:02:23+1100|SUNNY|24.44|1014.89|57.0
  ```
  
## Inputs to Application
  Following static files are being used as input to the program.
  - [locations.txt](https://github.com/AkhilBaby123/WeatherDataGenerator/blob/develop/resources/locations.txt) - contains the location information for different cities in Australia
  - [positions.txt](https://github.com/AkhilBaby123/WeatherDataGenerator/blob/develop/resources/positions.txt) - contains positions information for different cities in Australia
  - [zoneids.txt](https://github.com/AkhilBaby123/WeatherDataGenerator/blob/develop/resources/zoneids.txt) - contains zone ids for different cities in Australia
  
  The input arguments to the application is as follows:- 
  
  - <b>Forecast Start Date</b> - the date from which forecasting needs to be done
  - <b>Output File Name</b> - the name of the output file to where the forecasted results need to be written
  - <b>NumDays (Optional)</b> - the number of days (starting from forecast start date) to which forecasting needs to be done

## How to Use
   See <b>'How to Build'</b> section below to know the steps to build the application. Once the project is built, the executable jar file will be available in the target directory of the project. Use the executable jar file for running the application.
   
   To run:-
   ```
    java -cp WeatherForcast-0.1-jar-with-dependencies.jar com.toy.main.Launcher 2017-11-11 output 1
   ```
   
## How to Build
  This application uses Maven as the build tool. Use below maven command to build the application.
  ```
  #clean target directory and generate the package 
  mvn clean package
  ```

## Dependencies
  This application has following dependencies:-
  
    timeseries-forecast 1.1.1
    Junit 4.10
    log4j 1.2.17
 
## Authors
   Akhil Baby
     
## License
   Copyright (c) 2017 Akhil Baby




  

  
