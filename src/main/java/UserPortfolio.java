/*
 *
 * @authors 1. L00169709@student.lyit.ie - Vlad Stefan Munteanu (GitHub ID: L00169709Vlad),
 * 2. l00149880@student.lyit.ie Vara Zehra (GitHub ID: VaraZZ)
 * 3. l00169699@student.lyit.ie Tomasz Sobkow (GitHub ID: studenttom),
 * 4. l00169923@student.lyit.ie = Pablo Perez Fernandez (GitHub ID: pabloperfer)
 * 5. l00170043@student.lyit.ie - Jacek Pechalski (GitHub ID: JacekBro-).
 *
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

public class UserPortfolio implements PortfolioSystem {

  public static double funds = 0.0;
  private final List<String> validRegions =
      Arrays.asList("AU", "CA", "DE", "FR", "HK", "US", "IT", "ES", "GB", "IN");

  /** User portfolio for manipulating yahoo finance Api Http request data. */
  public UserPortfolio() {}

  /**
   * Gets the funds in USD available within the portfolio system.
   *
   * @return the funds of money in USD to add to the system.
   */
  public double getFunds() {
    return funds;
  }

  @Override
  public void addFunds(double amount) {
    if (amount >= 0) {
      System.out.println("\nAmount of " + amount + " USD Has been added to your Funds");
      funds = funds + amount;
    } else {
      System.out.println("Negative value is not accepted");
    }
  }


  @Override
  public boolean withdrawFunds(double amount) {
    if (amount > funds ){
      System.out.println("You have not enough founds for this request");
      return false;}
    if(amount == 0 || amount < 0){
      return false;
    }
    funds -= amount;
    return true;
  }

  @Override
  public boolean purchaseAsset(String assetSymbol, double amount) {
    List<String> assetList = new ArrayList<>();
    // get the asset information based on the symbol
    assetList.add(assetSymbol);
    List<AssetQuote> assetQuotes = this.getAssetInformation(assetList);
    // verify if the asset has been found
    if (assetQuotes.size() == 0) {
      System.out.println("Asset Symbol not found ");
      return false;
    }
    // verify that the amount requested to be purchased is available
    AssetQuote asset = assetQuotes.get(0);
    double price = asset.getValue() * amount;
    System.out.println("Price for the requested amount is " + price);
    if (price > funds) {
      System.out.println("Insufficient funds available to complete the purchase");
      System.out.println("You may consider adding additional funds or requesting a lower amount");
      return false;
    }
    // add a new transaction with the current asset, price and timestamp.
    System.out.println("Purchased " + amount + " " + asset.getAssetFullName());
    Transaction purchaseTransaction =
        new Transaction(assetSymbol, asset.getTimeStamp(), "buy", amount, price);
    Transactions.addTransaction(purchaseTransaction);
    // decrease the funds with the price paid
    funds -= price;
    return true;
  }

  @Override
  public boolean sellAsset(String assetSymbol, double amount) {
    // verify if the asset is available in the portfolio
    if (!Assets.containsSymbol(Assets.assets, assetSymbol)) {
      System.out.println("Asset Symbol not present in the portfolio");
      return false;
    }
    // verify if the quantity for sale is preset in the portfolio
    double qty = Assets.getAssetQty(Assets.assets, assetSymbol);
    if (qty < amount) {
      System.out.println("The selected amount is greater then the amount owned for " + assetSymbol);
      System.out.println(
          "Currently in the portfolio there are " + qty + " owned assets of " + assetSymbol);
      return false;
    }
    List<String> assetList = new ArrayList<>();
    assetList.add(assetSymbol);
    // get the latest information of the asset
    List<AssetQuote> assetQuotes = this.getAssetInformation(assetList);

    AssetQuote asset = assetQuotes.get(0);
    double salePrice = asset.getValue() * amount;
    System.out.println("The amount current value is at " + asset.getValue());

    // add a new transaction with the current asset, price and timestamp.
    System.out.println(
        "Sold " + amount + " of " + asset.getAssetFullName() + " for " + salePrice + "USD");
    Transaction purchaseTransaction =
        new Transaction(assetSymbol, asset.getTimeStamp(), "sell", amount, salePrice);
    Transactions.addTransaction(purchaseTransaction);
    // increase the funds with the sellPrice
    funds += salePrice;
    return true;
  }

  @Override
  public List<String> getTrendingStocks(String region) {
    List<String> trendingStocks = new ArrayList<String>();
    if (!this.validRegions.contains(region)) {
      return trendingStocks;
    }
    String stringResult = null;
    String APIKey = ServiceProvider.getAPIKey();
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create("https://yfapi.net/v1/finance/trending/" + region))
            .header("x-api-key", APIKey)
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
    try {
      HttpResponse<String> response =
          HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
      stringResult = response.body();
    } catch (Exception e) {

    }
    String[] splitString = stringResult.split("\"symbol\":\"");
    for (int i = 1; i < splitString.length; i++) {
      String[] stock = splitString[i].split("\"");
      trendingStocks.add(stock[0]);
    }
    return trendingStocks;
  }

  @Override
  public List<AssetQuote> getHistoricalData(
      List<String> assetSymbols, String interval, String range) {
    // I set another key , 20000 daily limit you can use it
    String APIKey = ServiceProvider.getAPIKey2();
    String assetSymbol;
    String assetDisplayName;
    List<AssetQuote> AssetList = new ArrayList<>();
    List<Long> timestamps = new ArrayList<>();
    List<Double> values = new ArrayList<>();

    try {
      for (int i = 0; i < assetSymbols.size(); i++) {
        // I'm making two request, one will give me the full name of the asset and the rest the
        // values needed for a Assetquote object
        HttpRequest request2 =
            HttpRequest.newBuilder()
                .uri(URI.create("https://yfapi.net/v7/finance/options/" + assetSymbols.get(i)))
                .header("x-api-key", APIKey)
                .header("accept", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response2 =
            HttpClient.newHttpClient().send(request2, HttpResponse.BodyHandlers.ofString());
        JSONObject tempJSON = new JSONObject(response2.body());
        if (tempJSON
            .getJSONObject("optionChain")
            .getJSONArray("result")
            .getJSONObject(0)
            .getJSONObject("quote")
            .has("displayName"))
        // above we check if it's a share or a crypto to obtain the name as it's vary in the api key
        {
          assetDisplayName =
              tempJSON
                  .getJSONObject("optionChain")
                  .getJSONArray("result")
                  .getJSONObject(0)
                  .getJSONObject("quote")
                  .getString("displayName");
        } else {
          assetDisplayName =
              tempJSON
                  .getJSONObject("optionChain")
                  .getJSONArray("result")
                  .getJSONObject(0)
                  .getJSONObject("quote")
                  .getString("shortName");
        }

        HttpRequest request =
            HttpRequest.newBuilder()
                .uri(
                    URI.create(
                        "https://yfapi.net/v8/finance/spark?"
                            + "interval="
                            + interval
                            + "&range="
                            + range
                            + "&symbols="
                            + assetSymbols.get(i)))
                .header("x-api-key", APIKey)
                .header("accept", "application/json")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response =
            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject assetJson = new JSONObject(response.body());
        JSONObject tmpJsonAsset;

        // getting the symbol from the API
        tmpJsonAsset = assetJson.getJSONObject(assetSymbols.get(i));
        assetSymbol = tmpJsonAsset.getString("symbol");

        // Converting the timestamps JSON Array to an array of long
        JSONArray ja;
        ja = tmpJsonAsset.getJSONArray("timestamp");
        for (int x = 0; x < ja.length(); x++) {
          timestamps.add(ja.getLong(x));
        }

        // Converting the value JSON Array to an array of double
        JSONArray ja2;
        ja2 = tmpJsonAsset.getJSONArray("close");
        for (int x = 0; x < ja2.length(); x++) {
          values.add(ja2.getDouble(x));
        }
        // Now we will create an object for each timestamp
        for (int z = 0; z < timestamps.size(); z++) {
          AssetQuote newOne = new AssetQuote();
          newOne.setTimeStamp(timestamps.get(z));
          newOne.setValue(values.get(z));
          newOne.setAssetFullName(assetDisplayName);
          newOne.setAssetSymbol(assetSymbol);
          AssetList.add(newOne);
        }
        // We clear the lists to not use the old values again
        timestamps.clear();
        values.clear();
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with the historical data retrieval");
    }
    return AssetList;
  }

  @Override
  public String getExchangeSummary(String region, String exchange) {

    ExchangeInfo exchInfo1 = new ExchangeInfo();
    new ArrayList();

    List<String> inputExchangeList2 = exchInfo1.getValidExchangeSym();

    JSONObject userData = new JSONObject(exchInfo1.httpRegRequest(region));
    // int j = false;
    System.out.println("\nDETAILS OF REQUESTED MARKET " + exchange);
    String currentDT = " Date: " + LocalDate.now();
    System.out.println(currentDT);

    // loop to extrat exchange summary from JSONObject for a apecified region.
    for (int i = 0; i < inputExchangeList2.size(); ++i) {
      if (((String) inputExchangeList2.get(i)).equals(exchange)) {
        String region1 =
            (String)
                userData
                    .getJSONObject("marketSummaryResponse")
                    .getJSONArray("result")
                    .getJSONObject(i)
                    .get("region");
        System.out.println("\n Region: " + region1);

        String fullExName =
            (String)
                userData
                    .getJSONObject("marketSummaryResponse")
                    .getJSONArray("result")
                    .getJSONObject(i)
                    .get("fullExchangeName");
        System.out.println(" Exchange Fullname: " + fullExName);

        String symbol =
            (String)
                userData
                    .getJSONObject("marketSummaryResponse")
                    .getJSONArray("result")
                    .getJSONObject(i)
                    .get("symbol");
        System.out.println(" Symbol: " + symbol);

        String regularMarketTime =
            (String)
                userData
                    .getJSONObject("marketSummaryResponse")
                    .getJSONArray("result")
                    .getJSONObject(i)
                    .getJSONObject("regularMarketTime")
                    .get("fmt");
        System.out.println(" Market Time: " + regularMarketTime);

        String regularMarketChangePercent =
            (String)
                userData
                    .getJSONObject("marketSummaryResponse")
                    .getJSONArray("result")
                    .getJSONObject(i)
                    .getJSONObject("regularMarketChangePercent")
                    .get("fmt");
        System.out.println(" Market Change (Gain/Loss) in Percent: " + regularMarketChangePercent);

        String regularMarketPrice =
            (String)
                userData
                    .getJSONObject("marketSummaryResponse")
                    .getJSONArray("result")
                    .getJSONObject(i)
                    .getJSONObject("regularMarketPrice")
                    .get("fmt");
        System.out.println(" Regular Market Price: " + regularMarketPrice);

        String marketState =
            (String)
                userData
                    .getJSONObject("marketSummaryResponse")
                    .getJSONArray("result")
                    .getJSONObject(i)
                    .get("marketState");
        System.out.println(" Market State: " + marketState);

        String market =
            (String)
                userData
                    .getJSONObject("marketSummaryResponse")
                    .getJSONArray("result")
                    .getJSONObject(i)
                    .get("market");
        System.out.println(" Market: " + market);

        Boolean tradeable =
            (Boolean)
                userData
                    .getJSONObject("marketSummaryResponse")
                    .getJSONArray("result")
                    .getJSONObject(i)
                    .get("tradeable");
        System.out.println(" Tradeable: " + tradeable);

        String regularMarketPreviousClose =
            (String)
                userData
                    .getJSONObject("marketSummaryResponse")
                    .getJSONArray("result")
                    .getJSONObject(i)
                    .getJSONObject("regularMarketPreviousClose")
                    .get("fmt");
        System.out.println(" Market Previous Close: " + regularMarketPreviousClose);
      }
    }
    return null;
  }

  @Override
  public List<AssetQuote> getAssetInformation(List<String> assetNames) {
    // The API is limited to 10 assetNames per request.
    // We will do multiple request if we are exceeding the limit.
    List<AssetQuote> result = new ArrayList<>();
    int numberOfRequests = assetNames.size() / 10;
    if (assetNames.size() % 10 != 0) {
      numberOfRequests++;
    }
    String APIKey = ServiceProvider.getAPIKey2();
    for (int i = 0; i < numberOfRequests; i++) {
      String requestQuery = assetNames.get(i * 10);
      for (int j = (i * 10) + 1; j < (i + 1) * 10 && j < assetNames.size(); j++) {
        requestQuery += "%2C" + assetNames.get(j);
      }

      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create("https://yfapi.net/v6/finance/quote?symbols=" + requestQuery))
              .header("x-api-key", APIKey)
              .header("accept", "application/json")
              .method("GET", HttpRequest.BodyPublishers.noBody())
              .build();
      try {
        HttpResponse<String> response =
            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject assetJson = new JSONObject(response.body());
        JSONArray responseArray = assetJson.getJSONObject("quoteResponse")
            .getJSONArray("result");
        for (int k = 0; k < responseArray.length(); k++) {
          JSONObject currentObject = responseArray.getJSONObject(k);
          AssetQuote currentAsset = new AssetQuote();
          currentAsset.setAssetSymbol(currentObject.get("symbol").toString());
          currentAsset.setAssetFullName(currentObject.get("shortName").toString());
          currentAsset.setTimeStamp(
              Long.parseLong(currentObject.get("regularMarketTime").toString()));
          currentAsset.setValue(
              Double.parseDouble(currentObject.get("regularMarketPrice").toString()));
          result.add(currentAsset);
        }
      } catch (Exception e) {
        System.out.println("Error encountered on getting Live Asset Information");
      }
    }
    return result;
  }

  @Override
  public double getPortfolioValue() {
    double totalSum = 0.0;
    String aSymbol;
    List<String> names = new ArrayList<>();
    List<AssetQuote> aq = new ArrayList<>();

    // get list of all assets in portfolio
    for (Asset a : Assets.assets) {
      aSymbol = a.getAssetSymbol();
      names.add(aSymbol);
    }
    // check each asset current value
    aq = (getAssetInformation(names));
    System.out.println("\n___Symbol__________Qty____________Value_________");
    // calculate value for each asset
    for (int i = 0; i < Assets.assets.size(); i++) {
      String listName = Assets.assets.get(i).getAssetSymbol();
      double listQty = Assets.assets.get(i).getAssetGty();
      double liveValue = Math.round(aq.get(i).getValue() * 100.0) / 100.0;
      double assetLiveValue = listQty * liveValue;
      System.out.println(
          "   "
              + BasicUI.strFiller(listName, 16)
              + BasicUI.strFiller(String.valueOf(listQty), 15)
              + Math.round(assetLiveValue * 100.0) / 100.0
              + " USD");

      // update sum value
      totalSum += assetLiveValue;
    }
    return totalSum;
  }

  @Override
  public String listAllInvestments() {
    String assetName;
    Double curPrice;
    Double avgPrice;
    double diffPriceUSD;
    double diffPricePer;

    String empty = "";

    List<String> list = new ArrayList<>();
    String delim = " ";

    //Display headings of the asset fields.
    System.out.println("\n");
    list.add(
        """
            ============================================== LIST OF ALL INVESTMENTS IN PORTFOLIO =========================================================

            """);

    list.add(BasicUI.strFiller("TICKER", 10));
    list.add(BasicUI.strFiller("NAME", 15));
    list.add(BasicUI.strFiller("CHG", 20));
    list.add(BasicUI.strFiller("CHG", 20));
    list.add(BasicUI.strFiller("CURRENT PRICE", 30));
    list.add(BasicUI.strFiller("QTY", 20));
    list.add(BasicUI.strFiller("AVG PURCHASE PRICE", 20));
    list.add("\n\n");

    // The condition checks if there are assets present in the portfolio. If no assets found returns
    // nothing.
    if (Assets.assets.isEmpty()) {
      return empty;
    }

    // Iterates through the assets list for assets present in the portfolio.
    for (Asset a : Assets.assets) {
      Double assQuantity = a.getAssetGty();
      String assetSym = a.getAssetSymbol();

      // Http request send to Yahoo Finance API using the specified key.
      String response = getAssetSymbolHttpRequest(assetSym);

      // Response received from the Yahoo Finance API.
      JSONObject tempJSON = new JSONObject(response);

      // Live current price using JSON retrieved from the http request for the assets input.
      curPrice =
          tempJSON
              .getJSONObject("optionChain")
              .getJSONArray("result")
              .getJSONObject(0)
              .getJSONObject("quote")
              .getDouble("regularMarketPrice");

      // Checks and retrieves for details for both stock and cryptocurrency.
      if (tempJSON
          .getJSONObject("optionChain")
          .getJSONArray("result")
          .getJSONObject(0)
          .getJSONObject("quote")
          .has("displayName")) {
        assetName =
            tempJSON
                .getJSONObject("optionChain")
                .getJSONArray("result")
                .getJSONObject(0)
                .getJSONObject("quote")
                .getString("displayName");
      } else {
        assetName =
            tempJSON
                .getJSONObject("optionChain")
                .getJSONArray("result")
                .getJSONObject(0)
                .getJSONObject("quote")
                .getString("shortName");

        curPrice =
            tempJSON
                .getJSONObject("optionChain")
                .getJSONArray("result")
                .getJSONObject(0)
                .getJSONObject("quote")
                .getDouble("regularMarketPrice");
      }

      // Calculate the average prices, difference in prices in percentage and USD for the
      // requested assets.
      avgPrice = Transactions.avgpurchaseprice(a.getAssetSymbol());
      if (curPrice > avgPrice) {
        diffPricePer = ((curPrice - avgPrice) / avgPrice) * 100;
        diffPriceUSD = curPrice - avgPrice;
      } else {
        diffPricePer = ((curPrice - avgPrice) / avgPrice) * 100;
        diffPriceUSD = curPrice - avgPrice;
      }

      //Displays the values of the details on assets calculated and retrieved.
      list.add(BasicUI.strFiller(assetSym, 10));
      list.add(BasicUI.strFiller(assetName, 15));
      String diffTwoDecimals = (String.format("%.2f", diffPricePer) + "% ");
      String diffTwoDecimals2 = (String.format("%.2f", diffPriceUSD) + " USD");
      if (diffPricePer > 0){
        list.add("+" + BasicUI.strFiller((diffTwoDecimals), 20));
      }else {
        list.add(BasicUI.strFiller((diffTwoDecimals), 20));
      }
      if(diffPriceUSD > 0){
        list.add("+" + BasicUI.strFiller((diffTwoDecimals2), 20));
      }else {
        list.add(BasicUI.strFiller((diffTwoDecimals2), 20));
      }
      list.add(BasicUI.strFiller(String.valueOf(curPrice + " USD"), 30));
      list.add(BasicUI.strFiller(String.valueOf(assQuantity), 20));
      list.add(BasicUI.strFiller(String.format("%.2f", avgPrice), 20));
      list.add("\n");
    }

    String res = String.join(delim, list);
    res.replaceAll(",?", " ");
    return res;
  }

  /**
   * A method that sends request to Yahoo Finance API using the asset's symbol entered.
   *
   * @param assetSym
   * @return dta received in response to the request send.
   */
  private String getAssetSymbolHttpRequest(String assetSym) {
    String APIKey = ServiceProvider.getAPIKey2();

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create("https://yfapi.net/v7/finance/options/" + assetSym))
            .header("x-api-key", APIKey)
            .header("accept", "application/json")
            .method("GET", BodyPublishers.noBody())
            .build();

    HttpResponse<String> response = null;
    try {
      response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    String responseData = response.body();
    return responseData;
  }

  @Override
  public String listPortfolioAssetsByType(String assetType) {

    // We will query the symbols and only take into account the ones that belong for each option
    // checking if jso object has specific key and invoking Transactions.avgpurchaseprice() and
    // doing the rest of the maths
    String assetDisplayName;
    Double assetCurrentPrice;
    Double assetPurchaseAvg;
    double diffPrice;
    String APIKey = ServiceProvider.getAPIKey2();
    String empty = "";
    double diffPriceUSD;

    List<String> list = new ArrayList<>();
    String delim = " ";
    list.add(
        "================================ Summary of your portfolio of type "
            + assetType
            + "================================"
            + "\n\n");

    list.add(BasicUI.strFiller("NAME", 25));
    list.add(BasicUI.strFiller("SYMBOL", 25));
    list.add(BasicUI.strFiller("INCREASE", 36));
    list.add(BasicUI.strFiller("CURRENT PRICE", 25));
    list.add(BasicUI.strFiller("QTY", 25));
    list.add(BasicUI.strFiller("AVG PURCHASE PRICE", 25));
    list.add("\n\n");

    if (Assets.assets.isEmpty()) {
      return empty;
    }

    for (Asset asset : Assets.assets) {
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create("https://yfapi.net/v7/finance/options/" + asset.getAssetSymbol()))
              .header("x-api-key", APIKey)
              .header("accept", "application/json")
              .method("GET", BodyPublishers.noBody())
              .build();

      HttpResponse<String> response = null;
      try {
        response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
      JSONObject tempJSON = new JSONObject(response.body());

      if (assetType.equals("stock")) {
        if (tempJSON
            .getJSONObject("optionChain")
            .getJSONArray("result")
            .getJSONObject(0)
            .getJSONObject("quote")
            .has("displayName")) {
          assetDisplayName =
              tempJSON
                  .getJSONObject("optionChain")
                  .getJSONArray("result")
                  .getJSONObject(0)
                  .getJSONObject("quote")
                  .getString("displayName");
          assetCurrentPrice =
              tempJSON
                  .getJSONObject("optionChain")
                  .getJSONArray("result")
                  .getJSONObject(0)
                  .getJSONObject("quote")
                  .getDouble("regularMarketPrice");

          assetPurchaseAvg = Transactions.avgpurchaseprice(asset.getAssetSymbol());
          if (assetCurrentPrice > assetPurchaseAvg) {
            diffPrice = ((assetCurrentPrice - assetPurchaseAvg) / assetPurchaseAvg) * 100;
            diffPriceUSD = assetCurrentPrice - assetPurchaseAvg;
          } else {
            diffPrice = ((assetCurrentPrice - assetPurchaseAvg) / assetPurchaseAvg) * 100;
            diffPriceUSD = assetCurrentPrice - assetPurchaseAvg;
          }
          list.add(BasicUI.strFiller(assetDisplayName, 25));
          list.add(BasicUI.strFiller(asset.getAssetSymbol(), 25));
          String difftwodecimals =
              (String.format("%.2f", diffPrice)
                  + "% "
                  + String.format("%.2f", diffPriceUSD)
                  + " USD");
          list.add(BasicUI.strFiller((difftwodecimals), 36));
          list.add(BasicUI.strFiller(String.valueOf(assetCurrentPrice + " USD"), 25));
          list.add(BasicUI.strFiller(String.valueOf(asset.getAssetGty()), 25));
          list.add(BasicUI.strFiller(String.format("%.2f", assetPurchaseAvg), 25));
          list.add("\n");

        } else {
          continue;
        }
      }
      if (assetType.equals("crypto")) {
        if (tempJSON
            .getJSONObject("optionChain")
            .getJSONArray("result")
            .getJSONObject(0)
            .getJSONObject("quote")
            .has("displayName")) {

          continue;
        } else {

          assetDisplayName =
              tempJSON
                  .getJSONObject("optionChain")
                  .getJSONArray("result")
                  .getJSONObject(0)
                  .getJSONObject("quote")
                  .getString("shortName");

          assetCurrentPrice =
              tempJSON
                  .getJSONObject("optionChain")
                  .getJSONArray("result")
                  .getJSONObject(0)
                  .getJSONObject("quote")
                  .getDouble("regularMarketPrice");

          assetPurchaseAvg = Transactions.avgpurchaseprice(asset.getAssetSymbol());
          if (assetCurrentPrice > assetPurchaseAvg) {
            diffPrice = ((assetCurrentPrice - assetPurchaseAvg) / assetPurchaseAvg) * 100;
            diffPriceUSD = assetCurrentPrice - assetPurchaseAvg;

          } else {
            diffPrice = ((assetCurrentPrice - assetPurchaseAvg) / assetPurchaseAvg) * 100;
            diffPriceUSD = assetCurrentPrice - assetPurchaseAvg;
          }
          list.add(BasicUI.strFiller(assetDisplayName, 25));
          list.add(BasicUI.strFiller(asset.getAssetSymbol(), 25));
          String difftwodecimals =
              (String.format("%.2f", diffPrice)
                  + "% "
                  + String.format("%.2f", diffPriceUSD)
                  + " USD");
          list.add(BasicUI.strFiller((difftwodecimals), 36));
          list.add(BasicUI.strFiller(String.valueOf(assetCurrentPrice + " USD"), 25));
          list.add(BasicUI.strFiller(String.valueOf(asset.getAssetGty()), 25));
          list.add(BasicUI.strFiller(String.format("%.2f", assetPurchaseAvg), 25));
          list.add("\n");
        }
      }
    }
    String res = String.join(delim, list);
    res.replaceAll(",?", " ");
    return res;
  }

  @Override
  public String listPortfolioAssetsByName(List<String> assetNames) {

    String assetName;
    Double curPrice;
    Double avgPrice;
    double diffPriceUSD;
    double diffPrice;

    String empty = "";

    List<String> list = new ArrayList<>();
    String delim = " ";

    //Display headings of the assets fields.
    System.out.println("\n");
    list.add(
        """
            ============================================== USER PORTFOLIO ASSETS BY NAME =============================================================

            """);

    list.add(BasicUI.strFiller("TICKER", 10));
    list.add(BasicUI.strFiller("NAME", 15));
    list.add(BasicUI.strFiller("CHG", 20));
    list.add(BasicUI.strFiller("CHG", 20));
    list.add(BasicUI.strFiller("CURRENT PRICE", 30));
    list.add(BasicUI.strFiller("QTY", 20));
    list.add(BasicUI.strFiller("AVG PURCHASE PRICE", 20));
    list.add("\n\n");

    // The condition checks if there are assets present in the portfolio. If no assets found returns
    // nothing
    if (Assets.assets.isEmpty()) {
      return empty;
    }

    // Checks for the assets if present in the portfolio with the assets input requesting details.
    for (Asset a : Assets.assets) {
      for (int i = 0; i < assetNames.size(); i++) {
        String n = assetNames.get(i);

        if (a.getAssetSymbol().equals(n)) {
          Double assQuantity = a.getAssetGty();
          String assetSym = a.getAssetSymbol();

          // Http request send to Yahoo Finance API using the specified key.
          String response = getAssetSymbolHttpRequest(assetSym);

          // Response received from the Yahoo Finance API.
          JSONObject tempJSON = new JSONObject(response);

          // Live current price using JSON retrieved from the http request for the assets input.
          curPrice =
              tempJSON
                  .getJSONObject("optionChain")
                  .getJSONArray("result")
                  .getJSONObject(0)
                  .getJSONObject("quote")
                  .getDouble("regularMarketPrice");

          // Checks for details for both stock and cryptocurrency.
          if (tempJSON
              .getJSONObject("optionChain")
              .getJSONArray("result")
              .getJSONObject(0)
              .getJSONObject("quote")
              .has("displayName")) {
            assetName =
                tempJSON
                    .getJSONObject("optionChain")
                    .getJSONArray("result")
                    .getJSONObject(0)
                    .getJSONObject("quote")
                    .getString("displayName");
          } else {
            assetName =
                tempJSON
                    .getJSONObject("optionChain")
                    .getJSONArray("result")
                    .getJSONObject(0)
                    .getJSONObject("quote")
                    .getString("shortName");

            curPrice =
                tempJSON
                    .getJSONObject("optionChain")
                    .getJSONArray("result")
                    .getJSONObject(0)
                    .getJSONObject("quote")
                    .getDouble("regularMarketPrice");
          }

          // Calculate the average prices, difference in prices in percentage and USD for the
          // requested assets.
          avgPrice = Transactions.avgpurchaseprice(assetNames.get(i));
          if (curPrice > avgPrice) {
            diffPrice = ((curPrice - avgPrice) / avgPrice) * 100;
            diffPriceUSD = curPrice - avgPrice;
          } else {
            diffPrice = ((curPrice - avgPrice) / avgPrice) * 100;
            diffPriceUSD = curPrice - avgPrice;
          }

          //Displays the values of the details on assets calculated and retrieved.
          list.add(BasicUI.strFiller(assetSym, 10));
          list.add(BasicUI.strFiller(assetName, 15));
          String diffTwoDecimals = (String.format("%.2f", diffPrice) + "% ");
          String diffTwoDecimals2 = (String.format("%.2f", diffPriceUSD) + " USD");
          if (diffPrice > 0){
            list.add("+" + BasicUI.strFiller((diffTwoDecimals), 20));
          }else{
            list.add(BasicUI.strFiller((diffTwoDecimals), 20));
          }
          if (diffPriceUSD > 0 ){
            list.add("+" + BasicUI.strFiller((diffTwoDecimals2), 20));
          }else {
            list.add(BasicUI.strFiller((diffTwoDecimals2), 20));
          }
          list.add(BasicUI.strFiller(String.valueOf(curPrice + " USD"), 30));
          list.add(BasicUI.strFiller(String.valueOf(assQuantity), 20));
          list.add(BasicUI.strFiller(String.format("%.2f", avgPrice), 20));
          list.add("\n");
        }
      }
    }
    String res = String.join(delim, list);
    res.replaceAll(",?", " ");
    return res;
  }

  @Override
  public String listPortfolioPurchasesInRange(long startTimeStamp, long endTimeStamp) {
    List<Transaction> tempTransactions = Transactions.getTransactions();
    List<String> displayList = new ArrayList<>();
    double diffPrice;
    double diffPriceUSD;
    String delim = " ";
    displayList.add(
        """
            ================================ Summary of your portfolio of purchases withing
             the time range entered===================================

            """);

    displayList.add(BasicUI.strFiller("SYMBOL", 15));
    displayList.add(BasicUI.strFiller("QTY", 15));
    displayList.add(BasicUI.strFiller("CURRENT PRICE", 15));
    displayList.add(BasicUI.strFiller("AVG PURCHASE PRICE", 25));
    displayList.add(BasicUI.strFiller("INCREASE", 36));
    displayList.add(BasicUI.strFiller("DATE", 30));

    displayList.add("\n\n");
    SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");

    for (Transaction purchase : tempTransactions) {
      if (purchase.getTransType().equals("buy")) {
        if (purchase.getTransTimeStamp() > startTimeStamp
            && purchase.getTransTimeStamp() < endTimeStamp) {
          Double AveragePurchasePrice = purchase.getTransCost() / purchase.getTransQty();
          Double currentPrice = getCurrentPrice(purchase.getTransSetSymbol());

          if (currentPrice > AveragePurchasePrice) {
            diffPrice = ((currentPrice - AveragePurchasePrice) / AveragePurchasePrice) * 100;
            diffPriceUSD = currentPrice - AveragePurchasePrice;
          } else {
            diffPrice = ((currentPrice - AveragePurchasePrice) / AveragePurchasePrice) * 100;
            diffPriceUSD = currentPrice - AveragePurchasePrice;
          }
          String diffTwoDecimals =
              (String.format("%.2f", diffPrice)
                  + "% "
                  + String.format("%.2f", diffPriceUSD)
                  + " USD");
          String date = sdf.format(purchase.getTransTimeStamp() * 1000);

          displayList.add(BasicUI.strFiller((purchase.getTransSetSymbol()), 15));
          displayList.add(BasicUI.strFiller(String.valueOf(purchase.getTransQty()), 15));
          displayList.add(
              BasicUI.strFiller(String.valueOf(String.format("%.2f", currentPrice)), 15));
          displayList.add(
              BasicUI.strFiller(String.valueOf(String.format("%.2f", AveragePurchasePrice)), 25));
          displayList.add(BasicUI.strFiller((diffTwoDecimals), 36));
          displayList.add(BasicUI.strFiller(date, 40));
          displayList.add("\n");
        }
      }
    }
    String res = String.join(delim, displayList);
    res.replaceAll(",?", " ");
    return res;
  }

  @Override
  public String listPortfolioSalesInRange(long startTimeStamp, long endTimeStamp) {
    String empty = "";
    if (Transactions.getTransactions().isEmpty()) {
      return empty;
    }
    List<Transaction> temptransactions = Transactions.getTransactions();
    List<String> displaylist = new ArrayList<>();

    String delim = " ";
    displaylist.add(
        """
            ================================ Summary of your portfolio of sales within the time
             range entered===================================

            """);

    displaylist.add(BasicUI.strFiller("SYMBOL", 15));
    displaylist.add(BasicUI.strFiller("QTY", 15));
    displaylist.add(BasicUI.strFiller("AVG PURCHASE PRICE", 25));
    displaylist.add(BasicUI.strFiller("SALE PRICE", 25));
    displaylist.add(BasicUI.strFiller("PROFIT", 36));
    displaylist.add(BasicUI.strFiller("DATE", 30));

    displaylist.add("\n\n");
    SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");

    for (Transaction sale : temptransactions) {
      String currentSymbol = sale.getTransSetSymbol();
      if (sale.getTransType().equals("sell")) {
        double saleprice = 0.0;
        double saleprofit = 0.0;
        double averagePurchasePrice = 0.0;
        double diffPriceUSD=0.0;
        if (sale.getTransTimeStamp() > startTimeStamp && sale.getTransTimeStamp() < endTimeStamp) {
          saleprice = sale.getTransCost() / sale.getTransQty();
          saleprofit = sale.getProfit();
          for (Transaction purchase : temptransactions) {
            if (purchase.getTransSetSymbol().equals(currentSymbol)) {
              averagePurchasePrice = Transactions.avgpurchaseprice(currentSymbol);
              diffPriceUSD = saleprice - averagePurchasePrice;
              }
          }

        String date = sdf.format(sale.getTransTimeStamp() * 1000);
          String diffTwoDecimals =
              (String.format("%.2f", saleprofit)
                  + "% "
                  + String.format("%.2f", diffPriceUSD)
                  + " USD");

        // Values to add in the resulting list
        displaylist.add(BasicUI.strFiller((sale.getTransSetSymbol()), 15));
        displaylist.add(BasicUI.strFiller(String.valueOf(sale.getTransQty()), 15));
        displaylist.add(
            BasicUI.strFiller(String.valueOf(String.format("%.2f", averagePurchasePrice)), 25));
        displaylist.add(BasicUI.strFiller(String.valueOf(String.format("%.2f", saleprice)), 25));
        displaylist.add(BasicUI.strFiller(diffTwoDecimals,36));
        displaylist.add(BasicUI.strFiller(date, 40));
        displaylist.add("\n");
        }
      }
    }
    String res = String.join(delim, displaylist);
    res.replaceAll(",?", " ");
    return res;
  }

  public static Double getCurrentPrice(String symbol) {

    String APIKey = ServiceProvider.getAPIKey2();
    Double assetCurrentPrice;

    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(URI.create("https://yfapi.net/v7/finance/options/" + symbol))
            .header("x-api-key", APIKey)
            .header("accept", "application/json")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();

    HttpResponse<String> response = null;
    try {
      response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    JSONObject tempJSON = new JSONObject(response.body());

    assetCurrentPrice =
        tempJSON
            .getJSONObject("optionChain")
            .getJSONArray("result")
            .getJSONObject(0)
            .getJSONObject("quote")
            .getDouble("regularMarketPrice");

    return assetCurrentPrice;
  }
  /**
   * Validation method to check if the correct Asset symbol has been entered.
   *
   * @param historySymbols
   * @return
   * @throws IOException
   * @throws InterruptedException
   */
  public boolean checkSymbolExists(List<String> historySymbols)
      throws IOException, InterruptedException {
    String APIKey = ServiceProvider.getAPIKey2();

    for (String symbol : historySymbols) {
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(URI.create("https://yfapi.net/v6/finance/recommendationsbysymbol/" + symbol))
              .header("x-api-key", APIKey)
              .header("accept", "application/json")
              .method("GET", BodyPublishers.noBody())
              .build();

      HttpResponse<String> response =
          HttpClient.newHttpClient().send(request, BodyHandlers.ofString());

      JSONObject assetJson = new JSONObject(response.body());

      System.out.println(assetJson.getJSONObject("finance").getJSONArray("result"));
      if (assetJson.getJSONObject("finance").getJSONArray("result").isEmpty()) {
        System.out.println(symbol + " is an incorrect symbol please enter a correct one");

        return false;
      }
    }
    return true;
  }

  /**
   * A method to validate a user input exchange option number used to retrieve exchange symbol.
   *
   * @param region
   * @return exchange symbol option number.
   */
  private String validExchangeInput(String region) {
    // User input for Exchange option number
    ExchangeInfo exchangeInfo = new ExchangeInfo();

    exchangeInfo.httpRegRequest(region);

    boolean isCorrectNumber = false;

    int exchangeInput = 0;
    String inputExResult = null;

    while (!isCorrectNumber) {
      exchangeInput = BasicUI.inputExResult();

      if (exchangeInput >= 0 && exchangeInput <= exchangeInfo.exchangeSymbol.size()) {
        List<String> inputExchangeList = exchangeInfo.getValidExchangeSym();
        --exchangeInput;
        inputExResult = inputExchangeList.get(exchangeInput);

        isCorrectNumber = true;

      } else {
        System.out.println(
            "\n Invalid Exchange option number provided. Number must be between 1 and "
                + exchangeInfo.getValidExchangeSym().size());
        isCorrectNumber = false;
      }
    }
    return inputExResult;
  }
  /**
   * Invoke the menu with all functions available at start loop the user input as long as exit
   * condition user input = 0
   */
  public void startUI() {

    do {
      userChoice();
    } while (userChoice() != 0);
    System.exit(0);
  }

  /**
   * User input to select the function from menu Each function must have specified methods and
   * prompt text
   *
   * @return option - selected by user input
   */
  public int userChoice() {

    String userInput = "";
    int option = 0;
    boolean checkLoop = false;
    Scanner s = new Scanner(System.in);

    // data validation for a user input it has to able to parse to a number value
    do {
      // display options
      BasicUI.startUI();
      // display available funds and date/time
      String userFund = BasicUI.menuFunds + funds + " USD";
      System.out.print(BasicUI.strFiller(userFund, 40));
      System.out.println(BasicUI.strFiller(BasicUI.getCurrentDT(), 40));

      try {
        System.out.print("\nUSER Select Option: ");
        userInput = s.nextLine();
        option = Integer.parseInt(userInput);

        switch (option) {
          case 1:
            {
              System.out.println("\n [-- Showing Trending Stock be Region --]" + validRegions);

              String region = BasicUI.enterRegion();
              // System.out.println(" Trending in region :");
              System.out.println(this.getTrendingStocks(region));
              System.out.println();
              // system stops and await user to decide to continue

              BasicUI.continuePressKey();

              checkLoop = true;
              break;
            }
          case 2:
            {
              double portfolioValue = getPortfolioValue();
              double roundValue = Math.round(portfolioValue * 100.0) / 100.0;
              System.out.println("\n2: USER PORTFOLIO TOTAL VALUE = " + roundValue + " USD");
              BasicUI.continuePressKey();

              checkLoop = true;
              break;
            }
          case 3:
            {
              // provide your functionality here
              System.out.println("Historical Transactions here");
              Transactions.printTransactions();

              BasicUI.continuePressKey();
              checkLoop = true;
              break;
            }
            // Edited BY VaraZZ - L00149880
          case 4:
            {
              // Display the amount available for purchase of assets
              System.out.println(
                  "\n The current amount available for purchase of assets:  " + funds);
              System.out.println();

              BasicUI.continuePressKey();

              checkLoop = true;
              break;
            }
          case 5:
            {
              System.out.println(" ");
              addFunds(BasicUI.enterFundsAmount());
              BasicUI.continuePressKey();

              checkLoop = true;
              break;
            }
          case 6:
            {
              List<String> symbols = BasicUI.enterSymbolList();
              List<AssetQuote> results = this.getAssetInformation(symbols);
              System.out.println(
                  "Symbol    Name                                    Timestamp                Trading Value");
              for (int i = 0; i < results.size(); i++) {
                AssetQuote.printResult(results.get(i));
              }
              System.out.println();
              BasicUI.continuePressKey();
              checkLoop = true;
              break;
            }
            // UI for entering Exchange Summary Region edited by VaraZZ - L00149880
          case 7:
            // User Input for region requested
            String regionInput = BasicUI.enterRegion();

            String validExchange = validExchangeInput(regionInput);
            this.getExchangeSummary(regionInput, validExchange);

            System.out.println(" ");
            BasicUI.continuePressKey();
            checkLoop = true;
            break;

          case 8:
            {
              System.out.println(" ");
              boolean result = withdrawFunds(BasicUI.enterWithdrawAmount());

              if (result ) {
                System.out.println("Withdrawn completed ");
                System.out.println("Available funds remaining = " + funds);
              } else {
                System.out.println("Available funds remaining = " + funds);
                System.out.println("Withdrawn failed, check you  amount requested ");
              }

              BasicUI.continuePressKey();
              checkLoop = true;
              break;
            }
          case 9:
            {
              String symbol = BasicUI.enterPurchaseSymbol();
              boolean continueTransaction = BasicUI.continueTrading();

              if (!continueTransaction) {
                System.out.println("Trading stopped");
                BasicUI.continuePressKey();
                checkLoop = true;
                break;
              }

              double amount = BasicUI.enterPurchaseAmount();
              this.purchaseAsset(symbol, amount);

              BasicUI.continuePressKey();

              checkLoop = true;
              break;
            }
          case 10:
            {
              List<String> historySymbols = BasicUI.enterSymbolList();
              if (!checkSymbolExists(historySymbols)) {
                BasicUI.enterSymbolList();
              }
              ;
              String interval = BasicUI.enterInterval();
              String range = BasicUI.enterRange();
              List<AssetQuote> results = this.getHistoricalData(historySymbols, interval, range);
              System.out.println(
                  "Symbol    Name                                    Timestamp                Trading Value");
              for (int i = 0; i < results.size(); i++) {
                AssetQuote.printResult(results.get(i));
              }
              System.out.println();
              BasicUI.continuePressKey();

              checkLoop = true;
              break;
            }
          case 11:
            {
              System.out.println(" ");

              String symbol = BasicUI.enterSellSymbol();
              boolean continueTransaction = BasicUI.continueTrading();
              if (!continueTransaction) {
                System.out.println("Trading stopped");
                BasicUI.continuePressKey();
                checkLoop = true;
                break;
              }
              double amount = BasicUI.enterSellAmount();
              this.sellAsset(symbol, amount);

              BasicUI.continuePressKey();
              checkLoop = true;
              break;
            }
          case 12:
            {
              List<String> nam = BasicUI.enterSymbolList();


              String result = this.listPortfolioAssetsByName(nam);
              System.out.println(result);
              BasicUI.continuePressKey();
              checkLoop = true;
              break;
            }
          case 13:
            {
              long st = BasicUI.enterStartTimeStamp();
              long en = BasicUI.enterEndTimeStamp();

              String result = this.listPortfolioPurchasesInRange(st, en);
              System.out.println(result);
              BasicUI.continuePressKey();
              checkLoop = true;
              break;
            }

          case 14:
            {
              String Typ = BasicUI.enterType();
              String result = this.listPortfolioAssetsByType(Typ);
              System.out.println(result);
              BasicUI.continuePressKey();
              checkLoop = true;
              break;
            }
          case 15:
            {
              String result = this.listAllInvestments();
              System.out.println(result);
              BasicUI.continuePressKey();
              checkLoop = true;
              break;
            }

          case 16:
            {
              long st = BasicUI.enterStartTimeStamp();
              long en = BasicUI.enterEndTimeStamp();

              String result = this.listPortfolioSalesInRange(st, en);
              System.out.println(result);
              BasicUI.continuePressKey();
              checkLoop = true;
              break;
            }
          case 0:
            {
              System.out.println(userInput + " [ -- Thank you!! Bye!! --]");
              System.exit(0);
              break;
            }
          default:
            {
              System.out.println(userInput + " Not exists in a menu !!!");
            }
        }
      } catch (NumberFormatException e) {
        System.out.println("Please provide only digit !");
      } catch (IOException | InterruptedException | ParseException e) {
        e.printStackTrace();
      }
    } while (!checkLoop);
    return option;
  }
}
