public class ServiceProvider {

  private static final String APIKey2 = "WCMO7A7k73z9uonOvoTu8HPuuz5An0e2gSIjrJBh";
  // This is hardcoded at the moment, will change once we make the user interface and allow the user to add its own key.
  // You should change that with your own private key for testing purposes.
  private static final String APIKey = "O4oBtlJTHM5qSpGocF8DA3HRbQV3qDxv2tQj4qxL";
  private static final String APIKey3 = "Mh69FtXhlZ4nWR44B0dmR65DnKFjTCdH49yU3mRI";

  public static String getApiKey3() {
    return APIKey3;
  }
  public static String getAPIKey() {
    return APIKey;
  }

  public static String getAPIKey2() {
    return APIKey2;
  }
}
