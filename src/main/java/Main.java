import Util.BaseDesiredCapabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {
    private static AppiumDriver<MobileElement> driver;
    private WebDriverWait wait;

    public static void main(String args[]){
        BaseDesiredCapabilities mDevice = new BaseDesiredCapabilities();
        driver = mDevice.getDriver();

        mDevice.userWait(3000);

        // Y
        mDevice.touchPoint(780, 1945);
        mDevice.userWait(1000);

        // o
        mDevice.touchPoint(1210, 1945);
        mDevice.userWait(1000);

        // u
        mDevice.touchPoint(935, 1945);
        mDevice.userWait(1000);

        mDevice.takeScreenShot();


        }
    }



