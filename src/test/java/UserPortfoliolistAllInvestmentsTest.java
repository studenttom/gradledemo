import static org.testng.Assert.*;
//Class to check the UserPortfolio listAllInvestments() method.

import org.testng.Assert;
import org.testng.annotations.Test;

public class UserPortfoliolistAllInvestmentsTest {

  @Test
  public void getUserPortfoliolistAllInvestments_ReturnsEqualList() {

    //Arrange
    UserPortfolio portfolio = new UserPortfolio();

    //Act
    int aListsize = Assets.assets.size();
    int listofInvestmentsize = portfolio.listAllInvestments().length();

    //Assert
    //If the list of Investments size equals to the number of assets in the portfolio it is true.
    Assert.assertTrue( listofInvestmentsize== aListsize);
  }

  @Test
  public void getUserPortfoliolistAllInvestments_ReturnsNotEqualList() {

    //Arrange
    UserPortfolio portfolio = new UserPortfolio();

    //Act
    int aListsize = Assets.assets.size();
    int listofInvestmentsize = portfolio.listAllInvestments().length();

    //Assert
    //If the list of Investments size is not equal to the number of assets in the portfolio
    // it is false.
    Assert.assertFalse( listofInvestmentsize != aListsize);
  }

}