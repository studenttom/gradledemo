import static org.testng.Assert.*;

import java.util.InputMismatchException;
import org.testng.Assert;
import org.testng.annotations.Test;

public class BasicUITest {

  //*********** testEnterFundsAmount() ********************************/
  public  boolean isNumeric(String strNum) {
    if (strNum == null) {
      return false;
    }
    try {
      double d = Double.parseDouble(strNum);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  @Test
  public void testEnterFundsAmount() {
    String[] userInputs = {"0", "-12", "w"};
    for (String d : userInputs) {
      if (isNumeric(d)) {
        assertTrue(Double.parseDouble(d) <= 0);

      } else {
        Assert.assertThrows(NumberFormatException.class, () ->
            Double.parseDouble(d));
      }
    }
  }

//**************END testEnterFundsAmount() ******************************/
}