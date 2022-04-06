import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * This class checks if the Asset details beeded are returned by checking the size.
 */
public class UserPortfoliolistPortfolioAssetsByNameTest {

  @Test
  public void getPortfolioAssetsByName_WithValidNames_ReturnsPortfolioAssetsByNameSize() {
    //Arrange
    UserPortfolio userPortfolio1 = new UserPortfolio();
    //Act
    List<String> validAssets = Arrays.asList("AAPL", "TESLA", "gfg");
    List<String> assetDetails = Collections.singletonList(
        userPortfolio1.listPortfolioAssetsByName(validAssets));

    //Assert
    // If the size equals to the list of valid assets names the test passes.

    Assert.assertTrue(assetDetails.size()==1);
  }

}


