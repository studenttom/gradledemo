
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;
import java.util.ArrayList;



//with this unit test we test API response content is expected within the list of assetquote object

public class UserPortfolioRetrieveSharesHistoricalTest {

    @Test
    public void RetrieveSharesHistorical_SharesRetrievedCorrectly(){

        //Arrange

        UserPortfolio Portfolio1 = new UserPortfolio();



        String expectedresult1 = "Telefónica";
        String expectedresult2 = "Bitcoin USD";
        long TokenValue = 909108;
        List<String> SymbolsList = new ArrayList<String>();
        SymbolsList.add("TEF");
        SymbolsList.add("BTC-USD");
        double testvalue=0.00001;

        //Act
        List<AssetQuote> AssetsList = Portfolio1.getHistoricalData(SymbolsList, "1d", "5d");
        String asset1fullname=AssetsList.get(0).getAssetFullName();
        String asset2fullname=AssetsList.get(6).getAssetFullName();
        long assettimestamp=AssetsList.get(0).getTimeStamp();
        double assetvalue=AssetsList.get(6).getValue();

        //Assert
        Assert.assertEquals(asset1fullname, expectedresult1);
        Assert.assertEquals(asset2fullname, expectedresult2);
        Assert.assertTrue(assettimestamp  > TokenValue);
        Assert.assertTrue(assetvalue  > testvalue );

    }
}


