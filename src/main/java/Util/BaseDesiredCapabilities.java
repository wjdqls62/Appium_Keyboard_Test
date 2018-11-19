package Util;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.net.MalformedURLException;
import java.net.URL;

public class BaseDesiredCapabilities extends DesiredCapabilities {
    private String path = "C://Users/jeongbeen.son.PHILL-IT/IdeaProjects/test/src/main/resources/appium_device/device.xml";
    private NodeList nodeList;
    private XMLParser xmlParser;
    private String Host, Port, DeviceNum, DeviceName, DevicePlatform, automationName, appPackage, appActivity, autoGrantPermissions;

    private AppiumDriver<MobileElement> mDevice;
    private WebDriverWait mWait;

    private TouchAction touchAction;
    private PointOption pointOption;

    public BaseDesiredCapabilities(){
        super();
        xmlLoad();
        setupTest();
    }

    public AppiumDriver<MobileElement> getDriver(){
        return mDevice;
    }

    private void xmlLoad(){
        xmlParser = new XMLParser(path);
        nodeList = xmlParser.getList();

        for(int i=0; i<nodeList.getLength(); i++){
            Node node = nodeList.item(i);
            Element element = (Element) node;

            Host = getTagValue("Host", element);
            Port = getTagValue("Port", element);
            DeviceNum = getTagValue("deviceNum", element);
            DeviceName = getTagValue("deviceName", element);
            DevicePlatform = getTagValue("platformName", element);
            automationName = getTagValue("automationName", element);
            appPackage = getTagValue("appPackage", element);
            appActivity = getTagValue("appActivity", element);
            appActivity = getTagValue("appActivity", element);
            autoGrantPermissions = getTagValue("autoGrantPermissions", element);
        }
    }

    private void setupTest(){
        setCapability("Host", Host);
        setCapability("Port", Port);
        setCapability("deviceNum", DeviceNum);
        setCapability("deviceName", DeviceName);
        setCapability("platformName", DevicePlatform);
        setCapability("automationName", automationName);
        setCapability("appPackage", appPackage);
        setCapability("appActivity", appActivity);
        setCapability("autoGrantPermissions", autoGrantPermissions);
        setCapability(MobileCapabilityType.TAKES_SCREENSHOT, "true");

        try {
            System.out.println("http://"+ Host + ":" + Port + "/wd/hub");
            mDevice = new AppiumDriver<MobileElement>(new URL("http://"+ Host + ":" + Port + "/wd/hub"), this);
            //mDevice.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            touchAction = new TouchAction(mDevice);
            pointOption = new PointOption();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void touchPoint(int x, int y){
        pointOption.withCoordinates(80, 1935);
        touchAction.tap(pointOption);
        mDevice.performTouchAction(touchAction);
    }

    public void userWait(long time){
        synchronized (mDevice){
            try {
                mDevice.wait(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue();
    }


}
