public class AssetQuote {

  /* You should implement relevant methods for the class. You can add additional attributes to
   * store additional information on each asset if you wish. Carefully consider the information that
   * you can retrieve from the finance API that you use and what information the user would like to
   * view or may find useful */

  /**
   * The symbol of the asset e.g. APPL, TSLA, BARC or BTC-USD
   */
  private String assetSymbol;

  /**
   * The full name of the asset e.g. Apple, Tesla, Barclays PLC, Bitcoin USD
   */
  private String assetFullName;

  /**
   * The UNIX timestamp of the asset's quoted value. Using long instead of int to avoid the year
   * 2038 problem.
   */
  private long timeStamp;

  /**
   * The value in USD of the named asset at this point in time.
   */
  private double value;

  public static String padRight(String s, int n) {
    return String.format("%-" + n + "s", s);
  }

  public static void printResult(AssetQuote element) {
    String symbol = element.getAssetSymbol();
    String name = element.getAssetFullName();
    long timeStamp = element.getTimeStamp();
    String date = new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(
        new java.util.Date(timeStamp * 1000));
    String value = Double.toString(element.getValue());

    System.out.println(
        padRight(symbol, 10) + padRight(name, 40) + padRight(date, 25) + padRight(value, 10));
  }

  /**
   * Getter for the asset symbol.
   */
  public String getAssetSymbol() {
    return assetSymbol;
  }

  // Setter for AssetSymbol
  public void setAssetSymbol(String c) {
    this.assetSymbol = c;
  }

  public String getAssetFullName() {
    return assetFullName;
  }

  // Setter for AssetFullName
  public void setAssetFullName(String c) {
    this.assetFullName = c;
  }

  public double getValue() {
    return value;
  }

  // Setter for value
  public void setValue(double c) {
    this.value = c;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  // Setter for timestamp
  public void setTimeStamp(long c) {
    this.timeStamp = c;
  }

  @Override
  public String toString() {
    return getAssetSymbol() + ", " + getAssetFullName() + ", timestamp: " + getTimeStamp()
        + ", value : " + getValue();
  }
}

