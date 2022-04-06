
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
public class UserPortfolioGetTrendingStocksTest {

    @Test
    public void getTrendingStocks_WithValidRegion_ReturnsPopulatedList() {
        //Arrange
        UserPortfolio portfolio = new UserPortfolio();
        //Act
        List<String> trendingStocks = portfolio.getTrendingStocks("US");
        //Assert
        Assert.assertTrue(trendingStocks.size() > 0);
    }

    @Test
    public void getTrendingStocks_WithInvalidRegion_ReturnsEmptyList() {
        //Arrange
        UserPortfolio portfolio = new UserPortfolio();
        //Act
        List<String> trendingStocks = portfolio.getTrendingStocks("UK");
        //Assert
        Assert.assertTrue(trendingStocks.size() == 0);
    }
}
