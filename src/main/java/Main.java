import Util.BaseDesiredCapabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
    private static AppiumDriver<MobileElement> driver;
    private WebDriverWait wait;

    public static void main(String args[]){
        BaseDesiredCapabilities baseDesiredCapabilities = new BaseDesiredCapabilities();
        driver = baseDesiredCapabilities.getDriver();

        baseDesiredCapabilities.userWait(3000);
        baseDesiredCapabilities.takeScreenShot();

        }
    }



