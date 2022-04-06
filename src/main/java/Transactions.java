import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list where the transactions are stored and ready to operate on them later in order
 * to create or update asset objects, calculate profit regarding the history etc...
 */
class Transactions {

  /**
   * This transactions can not be final is using many times
   */
  private static List<Transaction> transactions = new ArrayList<>();

  /** Class constructor. */
  public Transactions(List<Transaction> transactions) {
    transactions = transactions;
  }

  /** Gets the list of transaction objects */
  public static List<Transaction> getTransactions() {
    return transactions;
  }

  /**
   * Class Constructor
   *
   * @param newTransaction a Transaction that has been made and will be added to the list of
   *     transactions. Furtherlogic is added to this method, each time a transaction is added, a new
   *     asset is created, then we invoke another Asset boolean method to proceed if the asset is
   *     already there with the maths related to updatethe quantity parameter of the asset object
   *     which shows how many units you have. Moreover, if you don't have enough assets to sell, and
   *     exception will be thrown.
   */
  public static void addTransaction(Transaction newTransaction) {

    Asset asset = new Asset(newTransaction.getTransSetSymbol(), newTransaction.getTransQty());
    if (Assets.containsSymbol(Assets.assets, asset.getAssetSymbol())) {
      Assets.updateAssetQty(newTransaction, asset);
    } else {
      Assets.assets.add(asset);
      Batch transactionBatch = new Batch(newTransaction.getTransQty(), newTransaction.getTransCost()/newTransaction.getTransQty());
      asset.addAssetBatch(transactionBatch);

    }
    transactions.add(newTransaction);
  }

  /**
   * returns average price of all the purchases for a given symbol
   *
   * @param symbol
   */
  public static double avgpurchaseprice(String symbol) {
    double TotalPurchase = 0.0;
    double counter = 0.0;
    for (Transaction transaction : Transactions.transactions) {
      if (transaction.getTransSetSymbol().equals(symbol)
          && transaction.getTransType().equals("buy")) {
        TotalPurchase = TotalPurchase + transaction.getTransCost() / transaction.getTransQty();
        counter++;
      }
    }
    return TotalPurchase / counter;
  }

  /** Prints objects from the list of objects of type transactions */
  public static void printTransactions() {
    System.out.print("LIST OF TRANSACTIONS\n");
    System.out.print(BasicUI.strFiller("SYMBOL", 12));
    System.out.print(BasicUI.strFiller("TIME", 40));
    System.out.print(BasicUI.strFiller("TYPE", 12));
    System.out.print(BasicUI.strFiller("QTY", 12));
    System.out.print(BasicUI.strFiller("COST", 12));
    System.out.println("\n");

    SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");

    for (Transaction tran : transactions) {

      System.out.print(BasicUI.strFiller(tran.getTransSetSymbol(), 12));
      String date = sdf.format(tran.getTransTimeStamp() * 1000);
      System.out.print(BasicUI.strFiller(date, 40));
      System.out.print(BasicUI.strFiller(tran.getTransType(), 12));
      String typestring = String.valueOf(tran.getTransQty());
      System.out.print(BasicUI.strFiller(typestring, 12));
      // converted costString to have only 2 decimals
      DecimalFormat numberFormat = new DecimalFormat("#.00");
      String coststring = numberFormat.format(tran.getTransCost());
      System.out.print(BasicUI.strFiller(coststring, 12));
      System.out.println(" \n");
    }
  }
}
