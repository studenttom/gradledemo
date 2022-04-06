import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserPortfolioWithdrawFundsTest {

    @BeforeClass
    public void addSomeFunds(){

    }

    @Test
    public void UserPortfolioWithdrawFundsTest_EnoughFundsTest(){

        //Arrange
        UserPortfolio Portfolio1 = new UserPortfolio();
        boolean expectedResult = true;
        boolean actualResult;

        //Act
        Portfolio1.addFunds(1000.00);
        actualResult = Portfolio1.withdrawFunds(1000.00);

        //Assert
        Assert.assertEquals(actualResult, expectedResult);

    }

    @Test
    public void UserPortfolioWithdrawFundsTest_NotEnoughFundsTest(){

        //Arrange
        UserPortfolio Portfolio1 = new UserPortfolio();
        boolean expectedResult = false;
        boolean actualResult;

        //Act
        Portfolio1.addFunds(1000.00);
        actualResult = Portfolio1.withdrawFunds(1000.01);

        //Assert
        Assert.assertEquals(actualResult, expectedResult);

    }

    @AfterClass
    public void removeSomeFunds(){

    }

}
