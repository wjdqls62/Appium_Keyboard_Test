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
    private String keyboardLayoutXmlPath = "C://Users/jeongbeen.son.PHILL-IT/IdeaProjects/test/src/main/resources/device_cordinate/akeyboard.xml";
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
        xmlParser.parseXML("Device");
        deviceNodeList = xmlParser.getList("Device");

        for(int i=0; i<deviceNodeList.getLength(); i++){
            Node node = deviceNodeList.item(i);
            Element deviceInfo = (Element) node;

            Host = getTagValue("Host", deviceInfo);
            Port = getTagValue("Port", deviceInfo);
            DeviceNum = getTagValue("deviceNum", deviceInfo);
            DeviceName = getTagValue("deviceName", deviceInfo);
            DevicePlatform = getTagValue("platformName", deviceInfo);
            automationName = getTagValue("automationName", deviceInfo);
            appPackage = getTagValue("appPackage", deviceInfo);
            appActivity = getTagValue("appActivity", deviceInfo);
            appActivity = getTagValue("appActivity", deviceInfo);
            autoGrantPermissions = getTagValue("autoGrantPermissions", deviceInfo);
        }


        keyboardLayout = new ArrayList<Keyboard_Layout>();
        xmlParser.parseXML("Key");
        keyboardNodeList = xmlParser.getList("Key");
        for(int i=0; i<keyboardNodeList.getLength(); i++){
            Keyboard_Layout inputData = new Keyboard_Layout();
            Node node = keyboardNodeList.item(i);
            Element keyInfo = (Element) node;
            inputData.keyValue = getTagValue("value", keyInfo);
            inputData.symbol = getTagValueToBoolean("symbol", keyInfo);
            inputData.x = getTagValueToInt("x", keyInfo);
            inputData.y = getTagValueToInt("y", keyInfo);
            inputData.x1 = getTagValueToInt("x1", keyInfo);
            inputData.y1 = getTagValueToInt("y1", keyInfo);
            inputData.count = getTagValueToInt("count", keyInfo);
            keyboardLayout.add(inputData);
            System.out.println("Value : " + inputData.keyValue);
            System.out.println("symbol : " + inputData.symbol);
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
        System.out.print("TouchEvent-x:"+x+" y:"+y);
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

    public String[] takeScreenShot(){
        return captureManager.takeScreenShot();
    }

    public void inputMethod(String inputWord){
        char[] temp = inputWord.toCharArray();

        for(int i=0; i<temp.length; i++){
            for(int j=0; j<keyboardLayout.size(); j++){
                if(keyboardLayout.get(j).keyValue.equals(String.valueOf(temp[i]))){
                    touchPoint(keyboardLayout.get(j).x, keyboardLayout.get(j).y);
                    userWait(1000);
                    break;
                }
            }
        }
    }

    public void sensingPredictionWord(String inputWord){
        char[] temp = inputWord.toCharArray();

        for(int i=0; i<temp.length; i++){
            for(int j=0; j<keyboardLayout.size(); j++){
                if(keyboardLayout.get(j).keyValue.equals(String.valueOf(temp[i]))){
                    touchPoint(keyboardLayout.get(j).x, keyboardLayout.get(j).y);
                    userWait(500);
                    break;
                }
            }
            if(isPredictionWordCheck(takeScreenShot(), inputWord)){
                touchPredictionWord(i);
                break;
            }
        }
    }

    private boolean isPredictionWordCheck(String[] inputWord, String orgWord){
        if(inputWord != null){
            for(int i=0; i<inputWord.length; i++){
                if(orgWord.equals(inputWord[i])){
                    touchPredictionWord(i);
                    return true;
                }
            }
        }
        return false;
    }

    private void touchPredictionWord(int predictionCount){
        switch (predictionCount){
            case 0 :
                touchPoint(230, 1585);
                break;
            case 1 :
                touchPoint(650, 1585);
                break;
            case 2:
                touchPoint(1100, 1585);
                break;
        }
    }

    private String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return nValue.getNodeValue();
    }

    private int getTagValueToInt(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return Integer.parseInt(nValue.getNodeValue());
    }

    private boolean getTagValueToBoolean(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

        Node nValue = (Node) nlList.item(0);

        return Boolean.getBoolean(nValue.getNodeValue());
    }






}
