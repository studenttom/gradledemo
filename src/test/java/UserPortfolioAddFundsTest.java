import org.testng.Assert;
import org.testng.annotations.Test;

public class UserPortfolioAddFundsTest {

    @Test
    public void testAddFunds_FundsAddedCorrectly(){

        //Arrange
        System.out.println("Test 1 <adding correct value> Start: funds = 0.0");
        UserPortfolio Portfolio1 = new UserPortfolio();
        UserPortfolio.funds = 0.0;
        double amount = 1000;
        double actualResult;

        //Act
        System.out.println("Test: the amount added to funds = "+amount);
        Portfolio1.addFunds(amount);
        actualResult = Portfolio1.getFunds();

        //Assert
        System.out.println("Result: funds = "+actualResult);
        Assert.assertEquals(actualResult, amount);


    }
    @Test
    public void testAddFunds_FundsAddedIncorrectly() {

        //Arrange
        System.out.println("\nTest 2 <adding incorrect value> Start: funds = 0.0");
        UserPortfolio Portfolio1 = new UserPortfolio();
        UserPortfolio.funds = 0.0;
        double amount = -1000;
        double actualResult;

        //Act
        System.out.println("Test: the amount added to funds = "+amount);
        Portfolio1.addFunds(amount);
        actualResult = UserPortfolio.funds;

        //Assert
        System.out.println("Result: funds = "+actualResult);
        Assert.assertNotEquals (actualResult, amount);
    }
}
