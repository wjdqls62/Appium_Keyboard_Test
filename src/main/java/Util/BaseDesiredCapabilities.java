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
import java.util.ArrayList;

public class BaseDesiredCapabilities extends DesiredCapabilities {
    private String appiumXmlPath = "C://Users/jeongbeen.son.PHILL-IT/IdeaProjects/test/src/main/resources/appium_device/device.xml";
    private String keyboardLayoutXmlPath = "C://Users/jeongbeen.son.PHILL-IT/IdeaProjects/test/src/main/resources/appium_device/akeyboard.xml";
    private String capturePath = "D:TEST/_IMG/img.png";
    private NodeList deviceNodeList, keyboardNodeList;
    private ArrayList<Keyboard_Layout> keyboardLayout;
    private XMLParseManager xmlParser;
    private ScreenCaptureManager captureManager;
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
        xmlParser = new XMLParseManager(appiumXmlPath, keyboardLayoutXmlPath);
        deviceNodeList = xmlParser.getList("Device");

        for(int i=0; i<deviceNodeList.getLength(); i++){
            Node node = deviceNodeList.item(i);
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


        keyboardNodeList = xmlParser.getList("Keyboard");
        for(int i=0; i<keyboardNodeList.getLength(); i++){
            Node node = keyboardNodeList.item(i);
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

        captureManager = new ScreenCaptureManager(this, capturePath);

    }

    public void touchPoint(int x, int y){
        pointOption.withCoordinates(x, y);
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

    public String takeScreenShot(){
        return captureManager.takeScreenShot();
    }

    // 추천단어를 1문자 단위로 입력 후 추천단어를 인식한다.
    public void inputRecommandWord(String word){

    }

    private static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue();
    }




}
