/*
    *
    * @author L00149880 VaraZZ - This class gets exchange summary data upon Http
    request made to yahoo finance Api for a specified region using a key.
    */


import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * A class to retrieve Exchange Summary for a specified region.
 */

public class ExchangeInfo {

  public List<String> exchangeSymbol = new ArrayList<>();
  private List<String> exchangeName = new ArrayList<>();


  /**
   * Generates respose forthe Http request send to Yahoo Finance api.
   *
   * @param region is the region specified for the response data.
   *
   * @return the Http response data.
   */
  public String httpRegRequest(String region) {

    // APIkey 3 to be retrieved from the ServiceProvider.java file
    String apiKey3 = ServiceProvider.getApiKey3();
    String responseData;

      {
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("https://yfapi.net/v6/finance/quote/marketSummary?region=" + region))
          .header("x-api-key", apiKey3)
          .header("accept", "application/json")
          .method("GET", HttpRequest.BodyPublishers.noBody())
          .build();

      HttpResponse<String> response = null;
      try {
        response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString());
      } catch (IOException e) {
        System.out.println("Error encountered on getting information on the specified "
            + "region" + region);

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      responseData = response.body().toString();
      //System.out.println(response.body());
      exNameSymbol(region, responseData);
      }
    return responseData;
  }

  /**
   * Populates the exchange name and symbol from the jSONOObject response data.
   *
   * @param region is the region input for extracting http response data.   *
   * @param respData is the data received inturn to the http request send.
   */
  public void exNameSymbol(String region, String respData) {
    JSONObject userData = new JSONObject(respData);

    int j = 0;

    System.out.println("\nList of Exchanges operating in " + region + " region.");

    try {
      // getting a list of all markets in a specified region
      while (userData
          .getJSONObject("marketSummaryResponse")
          .getJSONArray("result")
          .getJSONObject(j)
          .get("region").equals(region)) {

        String exchangeFullName = (String) userData
            .getJSONObject("marketSummaryResponse")
            .getJSONArray("result")
            .getJSONObject(j)
            .get("fullExchangeName");
        exchangeName.add(exchangeFullName);

        String exchangeSym = (String) userData
            .getJSONObject("marketSummaryResponse")
            .getJSONArray("result")
            .getJSONObject(j)
            .get("symbol");
        exchangeSymbol.add(exchangeSym);

        int a = j + 1;
        System.out.println(a + "." + " Exchange full name: " + exchangeFullName);
        System.out.println("    Exchange Symbol: " + exchangeSym);
        j++;
      }
    } catch (Exception e) {
      System.out.println(" ");
    }
  }

  /**
   * a method to return valid exchange symbols.
   *
   * @return exchange symbols from a list
   */
  public List<String> getValidExchangeSym() {
    return exchangeSymbol;
  }

  /**
   * a method to return valid exchange names.
   *
   * @return exchange symbols from a list
   */
  public List<String> getValidExchangeName() {
    return exchangeName;
  }
}




