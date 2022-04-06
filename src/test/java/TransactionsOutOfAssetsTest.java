import org.testng.annotations.Test;

/**
 * This unit test simulates that we are adding a transaction of type sell which throws and exception
 * when finds out the quantity of an asset the user has is less than the amount he wants to sell We
 * check the exception is correctly thrown
 */
public class TransactionsOutOfAssetsTest {
  final String message = "You don't have enough units to sell !";

  @Test(expectedExceptionsMessageRegExp = message)
  public void outofassetstest() throws RuntimeException {

    Transaction newtransaction5 =
        new Transaction("BTC-USD", 1625438499L, "buy", 0.0445881D, 2000.0D);
    Transaction newtransaction6 =
        new Transaction("BTC-USD", 1625438499L, "sell", 0.0945881D, 2000.0D);

    Transactions.addTransaction(newtransaction5);
    Transactions.addTransaction(newtransaction6);
  }
}
