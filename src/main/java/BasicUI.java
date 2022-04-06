import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Class supporting text User interface for Stock Trending Portfolio.
 * Menu text and user input methods.
 */
public class BasicUI {

  // displays the available funds
  static String menuFunds = "    Funds = ";
  // date and time string formatter
  static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
  // string to show current date and time
  public static String currentDT = "    Date & Time: " + java.time.LocalDateTime.now().format(format);
  // text blocks to build the text of first menu
  private static String title =
      "\n ___ S T O C K ___ T R E N D I N G ___ P O R T F O L I O _________________v0.0";
  // private static final String userFunds =
  private static final String trending = " 1. Show Trending Stock by Region ";
  private static final String portfolioValue = " 2. Portfolio Value ";
  private static final String historical = " 3. Historical Transactions  ";
  private static final String availableFunds = " 4. Show Available Funds ";
  private static final String addFunds = " 5. Add Funds  ";
  private static final String assetInfo = " 6. See Assets Information ";
  private static final String exchangeSummary = " 7. See Exchange Summary ";
  private static final String withdrawFunds = " 8. Withdraw Funds ";
  private static final String purchaseAsset = "  9. Purchase Asset ";
  private static final String historyAssets = " 10. Retrieve history of assets ";
  private static final String sellAsset = " 11. Sell Asset";
  private static final String portfolioByName = " 12. Portfolio Info by Asset Name";
  private static final String portfolioPurchasesTime = " 13. Retrieve purchases in a range of time ";
  private static final String portfolioByType = " 14. Retrieve portfolio by type of asset ";
  private static final String listOfInvestments = " 15. View list Of all investments";
  private static final String portfolioSaleTime = " 16. Retrieve sales in a range of time";

  private static final String exit = "  0. Exit ";

  /**
   * Gets the current time
   * @return currentDT - day and time from system
   */
  public static String getCurrentDT() {
    return currentDT;
  }

  /**
   *  Build and prints out in two columns the menu from text blocks
   *
   */
  public static void startUI() {

    int colSize = 46;

    System.out.println(title);
    //first menu line
    System.out.print(strFiller(trending, colSize));
    System.out.println(strFiller(purchaseAsset, colSize));
    //second menu line
    System.out.print(strFiller(portfolioValue, colSize));
    System.out.println(strFiller(historyAssets, colSize));
    //third menu line
    System.out.print(strFiller(historical, colSize));
    System.out.println(strFiller(sellAsset, colSize));
    //fourth menu line
    System.out.print(strFiller(availableFunds, colSize));
    System.out.println(strFiller(portfolioByName, colSize));
    //fifth menu line
    System.out.print(strFiller(addFunds, colSize));
    System.out.println(strFiller(portfolioPurchasesTime, colSize));
    //sixth menu line
    System.out.print(strFiller(assetInfo, colSize));
    System.out.println(strFiller(portfolioByType, colSize));
    //seventh menu line
    System.out.print(strFiller(exchangeSummary, colSize));
    System.out.println(strFiller(listOfInvestments, colSize));
    //eighth menu line
    System.out.print(strFiller(withdrawFunds, colSize));
    System.out.println(strFiller(portfolioSaleTime, colSize));
    //ninth menu line
    System.out.println(strFiller(exit, colSize));
  }

  /**
   * Fills the string with preset character to defined length, (can improve and add the filling
   * character as method argument)
   *
   * @param str the string to be filled with filler character
   * @param fLength filled length of string
   * @return filledString - string with adjusted length to allow row/column display in text UI
   */
  public static String strFiller(String str, int fLength) {
    String stringFiller = ".";
    int maxStringLength = fLength;
    int fill = maxStringLength - str.length();
    String filledString = str + stringFiller.repeat(fill);
    return filledString;
  }

  /**
   *
   *  Method to input valid region.
   * @return valid input region returned.
   */
  public static String enterRegion() {

    List<String> validRegions1 = Arrays.asList("AU-Australia", "CA-Canada", "DE-Denmark",
        "FR-France", "HK-Hong Kong", "US-United States", "IT-Italy", "ES-Spain",
        "GB-Great Britain", "IN-India");
    List<String> validRegions = Arrays.asList("AU", "CA", "DE", "FR", "HK", "US", "IT", "ES",
        "GB", "IN");

    boolean isCorrectNumber = false;

    String regionInput = null;
    while (!isCorrectNumber) {
      try {
        System.out.println("\n Enter the two letter valid region code (AU, CA, DE, FR, HK,"
            + " US, IT, ES, GB, IN) you want to view.");
        System.out.println(validRegions1 + "\n");
        Scanner s = new Scanner(System.in);
        regionInput = s.nextLine().toUpperCase();

        if (!validRegions.contains(regionInput)) {
          System.out.println("\n Invalid region provided");
          isCorrectNumber = false;
        } else {
          isCorrectNumber = true;
        }
      } catch (InputMismatchException e) {
        System.out.println("Please provide only digit !");
        isCorrectNumber = false;
      }
    }
    return regionInput;
  }

  /**
   * Displays the list of asset symbols in portfolio
   * and asks user to enter the symbols string
   *
   * @return userInput string split into Array
   */
  public static List<String> enterSymbolList() {
    System.out.println("\nList of available Assets\n-----------------------");
    Assets.getAssets().forEach(t -> System.out.print(t.getAssetSymbol()+" "));
    System.out.println("\n-----------------------");

    System.out.print("USER Enter Symbol list separated by spaces: ");
    String userInput;

    Scanner s = new Scanner(System.in);
    userInput = s.nextLine().toUpperCase(Locale.ROOT);

    return Arrays.asList(userInput.split(" "));
  }

  public static String enterType() {

    System.out.print("USER Enter Type of asset \"crypto\" or \"stock\" : \n");
    String userInput;

    Scanner s = new Scanner(System.in);
    userInput = s.nextLine();

    if (!userInput.matches("^crypto$|^stock$")) {
      System.out.println("\n Invalid input " + userInput + " is not crypto or stock \n");
      enterType();
    }
    return userInput;
  }
  // Asks the user to enter an interval for the asset/crypto history of datapoints
  public static String enterInterval() {

    System.out.print("USER Enter interval of time (1m 5m 15m 30m 60m 1h 1d 5d 1wk 1mo): ");
    String userInputInterval;
    Scanner s = new Scanner(System.in);
    userInputInterval = s.nextLine();
    if (!userInputInterval.matches(
        "^1m$|^2m$|^5m$|^15m$||^30m$|^60m$|^90m$|^1h$|^1d$|^5d$|^1wk$|^1mo$|^3mo$")) {
      System.out.println(
          "\n Invalid input "
              + userInputInterval
              + " is not supported. Valid intervals: [1m, 2m, 5m, 15m, 30m, 60m, 90m, 1h, 1d, 5d, 1wk, 1mo, 3mo]\n");
      enterInterval();
    }

    return userInputInterval;
  }

  public static String enterRange() {

    System.out.print("USER Enter range of time for (1d 5d 1mo): ");
    String userInputRange;
    Scanner s = new Scanner(System.in);
    userInputRange = s.nextLine();
    if (!userInputRange.matches("^[1-9][d]|[1-5][0-9][d]|[1][m][o]")) {
      System.out.println("\n The requested range must be within the last 60 days. \n");
      enterRange();
    }
    return userInputRange;
  }

  // after displaying results system pause and waits for user to hit ENTER button
  public static String continuePressKey() {
    System.out.println("\n [-- to continue hit <Enter> key ................................ --]");
    Scanner s = new Scanner(System.in);
    String anyKey = s.nextLine();
    return anyKey + "\n";
  }

  public static double enterFundsAmount() {

    boolean isCorrectNumber = false;

    double userInput = 0;

    while (!isCorrectNumber) {
      try {
        Scanner s = new Scanner(System.in);
        System.out.print("USER Enter amount [ max 1000000 USD ]: ");
        userInput = s.nextDouble();

        if (userInput >= 0.01 && userInput <= 1000000.0) {
          return userInput;
        } else {
          if (userInput == 0) {
            return 0.0;
          } else {
            System.out.println("*** Incorrect!!! Try again [ 0 to ESC ] ");
          }
        }
      } catch (InputMismatchException e) {
        System.out.println("Please provide only digit !");
        isCorrectNumber = false;
      }
    }
    return userInput;
  }

  public static double enterWithdrawAmount() {

    boolean isCorrectNumber = false;
    double userInput = 0;

    while (!isCorrectNumber) {
      try {
        Scanner s = new Scanner(System.in);
        System.out.print("USER Enter amount to be withdrawn in USD: ");
        userInput = s.nextDouble();

        if (userInput < 0 || userInput == 0) {
          System.out.println("Value of Amount can not be smaller or equal to 0 ");
          break;
        }
        else {
          System.out.println("\nRequested amount of " + userInput + " to be withdrawn from the portfolio");
          isCorrectNumber = true;
        }
      } catch (InputMismatchException e) {
        System.out.println("Please provide only digit !");
        break;
      }
    }
    return userInput;
  }
  // asks user to enter the asset to be purchased
  public static String enterPurchaseSymbol() {

    String userInput = "";
    boolean checkLoop = true;
    Scanner s = new Scanner(System.in);
    while (checkLoop) {
      System.out.println("\nList of available Assets\n-----------------------");
      // take list of asset symbol from Assets
      Assets.getAssets().forEach(t -> System.out.print(t.getAssetSymbol()+" "));

      System.out.println("\n-----------------------");
      checkLoop = false;
      System.out.print("USER Enter Symbol for Purchase: ");
      userInput = s.nextLine();
      userInput = userInput.toUpperCase(Locale.ROOT).strip();
      List<String> assetSymbol = new ArrayList<>();
      assetSymbol.add(userInput);
      List<AssetQuote> result = new UserPortfolio().getAssetInformation(assetSymbol);
      if (result.size() == 0) {
        System.out.println("Provided Asset Symbol " + userInput + " unavailable for purchase.");
        checkLoop = true;
        continue;
      }
      AssetQuote asset = result.get(0);
      System.out.println("Asset latest information are:");
      System.out.println(
          "Symbol    Name                                    Timestamp                Trading Value");
      AssetQuote.printResult(asset);
    }
    return userInput;
  }

  // asks user if they want to continue the transaction
  public static boolean continueTrading() {
    String userInput = "";
    Scanner s = new Scanner(System.in);
    System.out.println("");
    System.out.print("USER continue with this trading? type [Yes / No]: ");
    userInput = s.nextLine();
    userInput = userInput.strip();
    userInput = userInput.toLowerCase();
    System.out.println("");
    if (userInput.equals("yes")) {
      return true;
    }
    return false;
  }

  // asks user to enter the amount to be purchased
  public static double enterPurchaseAmount() {
    boolean isCorrectNumber = false;

    double userInput = 0;

    while (!isCorrectNumber) {
      try {
        Scanner s = new Scanner(System.in);
        System.out.print("USER Enter Amount for Purchase [or 0 to Quit]: ");
        userInput = s.nextDouble();

        if (userInput < 0) {
          System.out.println("USER enter valid amount [or 0 to Quit]: ");

        } else {
          isCorrectNumber = true;
        }
      } catch (InputMismatchException e) {
        System.out.println("Please provide only digit !");
        isCorrectNumber = false;
      }
    }
    return userInput;
  }

//We use this method to get the input of the date , validate it and then convert it to unix timestamp
  //which will be passed to the method in the UserPortFolio to show the matches in the list
  public static long enterStartTimeStamp() throws ParseException {
    long timeInSeconds=0;
    Scanner s = new Scanner(System.in);
    System.out.print(
        "USER Enter the timestamp from where you want to start filtering  yyyy-MM-dd HH:mm : ");
    String userInput = s.nextLine();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    try{
    LocalDateTime localDateTime = LocalDateTime.parse(userInput,formatter);
    timeInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC);


    }catch (DateTimeParseException e) {
     System.out.println("Invalid date, please type a correct date yyyy-MM-dd HH:mm \n ");
      enterStartTimeStamp();
    }
    return timeInSeconds;
  }
  public static long enterEndTimeStamp() throws ParseException {
    long timeInSeconds=0;
    Scanner s = new Scanner(System.in);
    System.out.print(
        "USER Enter the timestamp from where you want to end filtering yyyy-MM-dd HH:mm : ");
    String userInput = s.nextLine();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    try{
      LocalDateTime localDateTime = LocalDateTime.parse(userInput,formatter);
      timeInSeconds = localDateTime.toEpochSecond(ZoneOffset.UTC);
    }catch (DateTimeParseException e) {
      System.out.println("Invalid date, please type a correct date yyyy-MM-dd HH:mm \n ");
      enterEndTimeStamp();
    }
    return timeInSeconds;
    }


  /**
   * L00149880 - VaraZZ
   * Provides validation to the exchange option number input by user to view exchange details
   * in a specified region.
   */
    public static int inputExResult() {
    boolean isCorrectNumber = false;

    int exchangeInput = 0;

    while (!isCorrectNumber) {
      try {
        Scanner s = new Scanner(System.in);
        System.out.print("Please enter Exchange option number: ");
        exchangeInput = s.nextInt();

        if (exchangeInput < 0 || exchangeInput == 0) {
          System.out.println("Exchange option number can not be lesser then or equal to 0 ");

        } else {
          isCorrectNumber = true;
        }
      } catch (InputMismatchException e) {
        System.out.println("Please enter numbers only.");
        isCorrectNumber = false;
      }
    }
    return exchangeInput;
  }

  // asks user to enter the asset Symbol to be sold
  public static String enterSellSymbol() {
    String userInput = "";
    boolean checkLoop = true;
    Scanner s = new Scanner(System.in);
    List<String> availableAssets = new ArrayList<>();
    Assets.assets.forEach(e -> availableAssets.add(e.getAssetSymbol()));

    while (checkLoop) {
      System.out.println("\nList of available Assets\n-----------------------");
      Assets.assets.forEach(e -> {
        System.out.print(e.getAssetSymbol() + ": ");
        int fill = e.getAssetSymbol().length() + 2;
        String space = " ";
        boolean first = true;
        for (int i = 0; i < e.assetBatches.size(); i++) {
          Batch batch = e.assetBatches.get(i);
          if (first) {
            first = false;
            System.out.println(batch.toString());
          } else {
            System.out.println(space.repeat(fill) + batch.toString());
          }
        }

        System.out.println("");
      });
      System.out.println("\n-----------------------");
      checkLoop = false;
      System.out.print("USER Enter Symbol for Sale: ");
      userInput = s.nextLine();
      userInput = userInput.toUpperCase(Locale.ROOT).strip();
      List<String> assetSymbol = new ArrayList<>();
      assetSymbol.add(userInput);
      List<AssetQuote> result = new UserPortfolio().getAssetInformation(assetSymbol);
      if (!availableAssets.contains(userInput)) {
        System.out.println("Provided Asset Symbol " + userInput + " not found in the portfolio");
        checkLoop = true;
        continue;
      }
      AssetQuote asset = result.get(0);
      System.out.println("Asset latest information are:");
      System.out.println(
              "Symbol    Name                                    Timestamp                Trading Value");
      AssetQuote.printResult(asset);
    }
    return userInput;
  }

  // asks user to enter the amount of the asset to be sold
  public static double enterSellAmount() {
    boolean isCorrectNumber = false;

    double userInput = 0;

    while (!isCorrectNumber) {
      try {
        Scanner s = new Scanner(System.in);
        System.out.print("USER Enter Amount for Sale [or 0 to Quit]: ");
        userInput = s.nextDouble();

        if (userInput < 0) {
          System.out.println("USER enter valid amount [or 0 to Quit]: ");

        }else
        {
          isCorrectNumber = true;
        }
      } catch (InputMismatchException e) {
        System.out.println("Please provide only digit !");
        isCorrectNumber = false;
      }
    }
    return userInput;
  }
}
