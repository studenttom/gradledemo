import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a list where the assets are stored and their parameter quantity updated when
 * required.
 */
public class Assets {

  public static List<Asset> assets = new ArrayList<>();

  /**
   * Class constructor.
   */
  public Assets(List<Asset> assets) {
    Assets.assets = assets;
  }

  /**
   * Gets the list of asset objects
   */
  public static List<Asset> getAssets() {
    return assets;
  }

  /**
   * We use streams here on the list to filter by assetSymbol and get a true or false if the asset
   * is already in the list of asset objects or not.
   *
   * @param list   The list where the asset is stored
   * @param symbol The asset symbol
   */
  public static boolean containsSymbol(final List<Asset> list, final String symbol) {
    return list.stream().filter(o -> o.getAssetSymbol().equals(symbol)).findFirst().isPresent();
  }
  /**
   * Return the asset quantity currently available for an asset symbol
   *
   * @param list   The list where the asset is stored
   * @param symbol The asset symbol
   */
  public static double getAssetQty(final List<Asset> list, final String symbol) {
    List<Asset> res = list.stream().filter(o -> o.getAssetSymbol().equals(symbol)).collect(Collectors.toList());
    return res.get(0).getAssetGty();
  }
  /**
   * Updates the quantity attribute of an existing asset in a list
   */
  public static double updateAssetQty(Transaction newTransaction, Asset asset) {
    for (Asset as : Assets.assets) {
      if (asset != null && asset.getAssetSymbol().equals(as.getAssetSymbol())) {
        if (newTransaction.getTransType().equals("buy")) {
          as.setAssetGty(as.getAssetGty() + newTransaction.getTransQty());
          Batch transactionBatch = new Batch(newTransaction.getTransQty(), newTransaction.getTransCost() / newTransaction.getTransQty());
          as.addAssetBatch(transactionBatch);
        }
        else {
          // decrease quantity from batches until the total amount has been sold.
          double amount = newTransaction.getTransQty();
          double profit = 0.0;
          double averagePrice = 0.0;

          while (amount > 0) {
            Batch currentBatch = as.getFirstBatch();
            // decrease the amount by the batch amount if not enough quantity in the lowest batch sorted by price.
            if (currentBatch.getBatchQty() <= amount){
              averagePrice += currentBatch.getBatchQty() * currentBatch.getBatchUnitCost();
              profit += currentBatch.getBatchQty() * currentBatch.getBatchUnitCost();
              amount -= currentBatch.getBatchQty();
              as.removeAssetBatch();
              continue;
            }
            averagePrice += currentBatch.getBatchUnitCost() * amount;
            profit += currentBatch.getBatchUnitCost() * amount;
            // current batch has a greated quantity then the remaining amount
            // decrese the currentBatch quantity
            currentBatch.amendBatchQty(amount);
            // update the batch element to have the new quantity
            as.updateFirstElement(currentBatch);
            amount = 0;
          }
          averagePrice /=  newTransaction.getTransQty();
          profit = newTransaction.getTransCost() / profit;
          profit *= 100;
          profit -= 100;
          newTransaction.setProfit(profit);
          newTransaction.setAveragePurchasePrice(averagePrice);
          // decrease the total quantity for the asset
          as.setAssetGty(as.getAssetGty() - newTransaction.getTransQty());
          if (as.getAssetGty() < 0) {
            throw new RuntimeException("You don't have enough units to sell !");
          }
        }}}
    return 0;
  }
  /**
   * Parses the list of assets and prints the content
   */
  public static void printAssets() {
    System.out.println("\n LIST OF ASSETS \r");
    System.out.print(BasicUI.strFiller("SYMBOL", 12));
    System.out.print(BasicUI.strFiller("QTY", 12));
    System.out.println("\n");

    for (Asset as : assets) {
      System.out.print(BasicUI.strFiller(as.getAssetSymbol(), 12));
      String typeString = String.valueOf(as.getAssetGty());
      System.out.print(BasicUI.strFiller(typeString, 12));
      System.out.println(" \n");
    }
  }
}
