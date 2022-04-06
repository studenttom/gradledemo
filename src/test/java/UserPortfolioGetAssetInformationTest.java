
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class UserPortfolioGetAssetInformationTest {

    @Test
    public void getGetAssetInformation_WithValidSymbol_ReturnsPopulatedList() {
        //Arrange
        UserPortfolio portfolio = new UserPortfolio();
        //Act
        // added two valid asset symbols to the list for testing
        List<String> validAssets = Arrays.asList("TEF", "FB");
        List<AssetQuote>  assetInformation = portfolio.getAssetInformation( validAssets);
        //Assert
        Assert.assertTrue(assetInformation.size() == 2);
    }

    @Test
    public void getGetAssetInformation_WithValidSymbol_WithListGreaterThanTen_ReturnsPopulatedList() {
        //Arrange
        UserPortfolio portfolio = new UserPortfolio();
        //Act
        // added 11 valid asset symbols to the list for testing so that a second request is tested too
        // this is needed as an edge case because the API only supports 10 Symbols per call.
        List<String> validAssets = Arrays.asList("TEF", "FB", "BTC-USD","AAPL","EURUSD=X","SPOT","MRK","MP","TMUS","AMZN","SNAP");
        List<AssetQuote>  assetInformation = portfolio.getAssetInformation( validAssets);
        //Assert
        Assert.assertTrue(assetInformation.size() == 11);
    }

    @Test
    public void getGetAssetInformation_WithInvalidSymbol_ReturnsEmptyList() {
        //Arrange
        UserPortfolio portfolio = new UserPortfolio();
        //Act
        // added two invalid asset symbols to the list for testing purpose
        List<String> invalidAssets = Arrays.asList("aa", "bb");
        List<AssetQuote>  assetInformation = portfolio.getAssetInformation(invalidAssets);
        //Assert
        Assert.assertTrue(assetInformation.size() > 0);
    }
}
