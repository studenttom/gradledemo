import static org.testng.Assert.*;

import java.util.Collections;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This test check if the exchange region entered is correct.
 */

public class UserPortfolioGetExchangeSummaryTest {



    @Test
    public void getExchangeSummary_WithValidRegion_ReturnsExchangeNameList() {
      //Arrange
      ExchangeInfo exInfo = new ExchangeInfo();
      //Act
      List<String> exchangeName = Collections.singletonList(exInfo.httpRegRequest("AU"));
      //Assert
      //If the region is correct the method returns details and the size is >0.
      Assert.assertTrue(exInfo.getValidExchangeName().size() > 0);
    }

    @Test
    public void getExchangeSummary_WithInvalidRegion_ReturnsExchangeNameList() {
      //Arrange
      ExchangeInfo exInfo = new ExchangeInfo();
      //Act
      List<String> exchangeName = Collections.singletonList(exInfo.httpRegRequest("uas"));
      //Assert
      //If the region is incorrect the method returns no details and it is empty.

      Assert.assertTrue(exInfo.getValidExchangeName().isEmpty());
    }
  }
