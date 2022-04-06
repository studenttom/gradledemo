/*Main method to initialise and run the investment portfolio management system */
public class Main {

  /**
   * Method to add the fictional asset purchases specified in the assignment to the portfolio*/
   public static void main(String[] args) {
      UserPortfolio Portfolio1 = new UserPortfolio();

    Transaction newTransaction = new Transaction("TSLA", 1633041699L, "buy", 10.0D, 775.22D);
    Transaction newTransaction2 = new Transaction("AAPL", 1625438499L, "buy", 20.0D, 139.96D);
    Transaction newTransaction3 = new Transaction("NVDA", 1618353699L, "buy", 12.0D, 152.77D);
    Transaction newTransaction4 = new Transaction("BTC-USD", 1625438499L, "buy", 0.0445881D,
        2000.0D);

    Transactions.addTransaction(newTransaction);
    Transactions.addTransaction(newTransaction2);
    Transactions.addTransaction(newTransaction3);
    Transactions.addTransaction(newTransaction4);

     Portfolio1.startUI();
  }
}




