import Util.BaseDesiredCapabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

public class Main {
    private static AppiumDriver<MobileElement> driver;
    private WebDriverWait wait;

    public static void main(String args[]){
        BaseDesiredCapabilities baseDesiredCapabilities = new BaseDesiredCapabilities();
        driver = baseDesiredCapabilities.getDriver();

        baseDesiredCapabilities.userWait(3000);

        try {
            File file = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(file, new File("Appium_Capture_Test.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
