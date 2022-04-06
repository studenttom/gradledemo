/**
 * Represents a transaction that the user have made. After all the required parameters are stored
 * the transaction will be compared with the elements in the list of assets objects, if asset exists
 * the quantity will be updated, otherwise will be added. You won't be able to sell more units than
 * you have.
 */
public class Transaction {

  private long transTimeStamp;
  private String transType;
  private String transSetSymbol;
  private double transQty;
  private double transCost;
  private double averagePurchasePrice;
  private double profit;

  /**
   * Class Constructor
   *
   * @param transSetSymbol assetName
   * @param transTimeStamp   is the time received from API or fictional transactions
   * @param transType        this is purchase or sale
   * @param transQty         the quantity transacted
   * @param transCost        total cost of the quantity transacted
   */
  public Transaction(
      String transSetSymbol,
      long transTimeStamp,
      String transType,
      double transQty,
      double transCost) {
    this.transSetSymbol = transSetSymbol;
    this.transTimeStamp = transTimeStamp;
    this.transType = transType;
    this.transQty = transQty;
    this.transCost = transCost;
  }

  /**
   * Gets the transaction timestamp.
   */
  public long getTransTimeStamp() {
    return transTimeStamp;
  }

  public void setTransTimeStamp(long transTimeStamp) {
    this.transTimeStamp = transTimeStamp;
  }

  /**
   * Gets the transaction type , buy or sell.
   */
  public String getTransType() {
    return transType;
  }

  public void setTransType(String transType) {
    this.transType = transType;
  }

  /**
   * Gets the transaction asset symbol.
   */
  public String getTransSetSymbol() {
    return transSetSymbol;
  }

  /**
   * Setters for the above
   */
  public void setTransSetSymbol(String transSetSymbol) {
    this.transSetSymbol = transSetSymbol;
  }

  /**
   * Gets the transaction asset quantity.
   */
  public double getTransQty() {
    return transQty;
  }

  public void setTransQty(double transQty) {
    this.transQty = transQty;
  }

  /**
   * Gets the transaction cost for the assets bought or sold.
   */
  public double getTransCost() {
    return transCost;
  }

  public void setTransCost(double transCost) {
    this.transCost = transCost;
  }

  public double getAveragePurchasePrice() {
    return averagePurchasePrice;
  }

  public void setAveragePurchasePrice(double averagePurchasePrice) {
    this.averagePurchasePrice = averagePurchasePrice;
  }

  public double getProfit() {
    return profit;
  }

  public void setProfit(double profit) {
    this.profit = profit;
  }
}
