package Util;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;

public class BaseDesiredCapabilities extends DesiredCapabilities {
    private String appiumXmlPath = "C://Users/jeongbeen.son.PHILL-IT/IdeaProjects/test/src/main/resources/appium_device/device.xml";
    private String keyboardLayoutXmlPath = "C://Users/jeongbeen.son.PHILL-IT/IdeaProjects/test/src/main/resources/device_cordinate/akeyboard.xml";
    private String capturePath = "D:TEST/_IMG/img.png";
    private boolean autoSpacing = true;
    private NodeList deviceNodeList, keyboardNodeList;
    private ArrayList<Keyboard_Layout> keyboardLayout;
    private XMLParseManager xmlParser;
    private ScreenCaptureManager captureManager;
    private String Host, Port, DeviceNum, DeviceName, DevicePlatform, automationName, appPackage, appActivity, autoGrantPermissions, noReset;

    private AppiumDriver<MobileElement> mDevice;
    private WebDriverWait mWait;

    private TouchAction touchAction;
    private LongPressOptions longPressOptions;
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
            noReset = getTagValue("noReset", deviceInfo);
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
            longPressOptions = new LongPressOptions();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        captureManager = new ScreenCaptureManager(this, capturePath);

    }

    // x, y좌표를 터치한다.
    public void touchPoint(int x, int y){
        pointOption.withCoordinates(x, y);
        touchAction.tap(pointOption);
        mDevice.performTouchAction(touchAction);
    }

    public void longTouchPoint(int x, int y){
        pointOption.withCoordinates(x, y);
        longPressOptions.withPosition(pointOption);
        longPressOptions.withDuration(Duration.ofMillis(2000));
        touchAction.longPress(longPressOptions);
        mDevice.performTouchAction(touchAction);
    }

    // DelayTime을 준다.
    public void userWait(long time){
        synchronized (mDevice){
            try {
                mDevice.wait(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 스크린샷을 찍어 설정된 영역의 String을 인식 후 반환한다.
    public ArrayList<String> takeScreenShot(){
        userWait(1000);
        return captureManager.takeScreenShot();
    }

    public void inputMethod(String inputWord){
        char[] temp = inputWord.toCharArray();
        int index, symbolKeyIndex = 0;

        for(int i=0; i<temp.length; i++){
            for(int j=0; j<keyboardLayout.size(); j++){
                if(keyboardLayout.get(j).keyValue.equals(String.valueOf(temp[i]))){
                    if(!keyboardLayout.get(j).symbol){
                        System.out.print("일반문자");
                        touchPoint(keyboardLayout.get(j).x, keyboardLayout.get(j).y);
                    }else{
                        System.out.print("특수문자다");
                        index = searchIndex(keyboardLayout, keyboardLayout.get(j).keyValue);
                        symbolKeyIndex = searchIndex(keyboardLayout, "123");

                        touchPoint(keyboardLayout.get(symbolKeyIndex).x, keyboardLayout.get(symbolKeyIndex).y);
                        touchPoint(keyboardLayout.get(index).x, keyboardLayout.get(index).y);
                        touchPoint(keyboardLayout.get(symbolKeyIndex).x, keyboardLayout.get(symbolKeyIndex).y);
                    }
                    userWait(1000);
                    break;
                }
            }
        }
    }

    // 파라미터의 단어를 한글자 단위로 입력하면서 추천단어를 읽고 지정된 영역에 존재하는지 확인한다.
    // 추천단어에 존재할 경우 해당 추천단어 영역을 터치한다.
    public void sensingPredictionWord(String inputWord){
        char[] temp = inputWord.toCharArray();
        ArrayList<String> predictResult = takeScreenShot();
        String symbol = null;

        // 글자를 누르지 않은 상태에서 해당 추천단어가 바로 표시되어 있을경우 추천단어를 선택한다.
        if(isPredictionWordCheck(predictResult, inputWord)){
            // 입력글자 마지막에 끝기호가 붙었을 경우
            symbol = isSymbolCheck(inputWord);
            if(symbol != null){
                inputMethod(symbol);
            }
            return;
        }

        predictResult.clear();

        // 총 입력해야될 단어
        for(int i=0; i<temp.length; i++){
            // 입력단어를 한글자단위로 쪼개 입력한다.
            for(int j=0; j<keyboardLayout.size(); j++){
                String op = keyboardLayout.get(j).keyValue;
                if(op.equals(String.valueOf(temp[i])) || op.toUpperCase().equals(String.valueOf(temp[i]).toUpperCase())){
                    System.out.println("Input Char : " + temp[i]);
                    if(!keyboardLayout.get(j).symbol){
                        touchPoint(keyboardLayout.get(j).x, keyboardLayout.get(j).y);
                    }else{
                        // 특수문자의 경우 123키의 좌표를 갖고온다.
                        int symbolKeyIndex = searchIndex(keyboardLayout, "123");
                        touchPoint(keyboardLayout.get(symbolKeyIndex).x, keyboardLayout.get(symbolKeyIndex).y);
                        userWait(500);
                        touchPoint(keyboardLayout.get(j).x, keyboardLayout.get(j).y);
                        userWait(500);
                        touchPoint(keyboardLayout.get(symbolKeyIndex).x, keyboardLayout.get(symbolKeyIndex).y);
                    }
                    break;
                }
            }

            userWait(500);

            // 한글자 입력 후 추천단어가 표시되었을 경우
            predictResult = takeScreenShot();
            if(isPredictionWordCheck(predictResult, inputWord)){
                // 추천단어를 선택 후 마침표등의 끝기호가 존재할 경우 해당 기호를 누른 후 스페이스바를 누른다.
                symbol = isSymbolCheck(inputWord);
                if(symbol != null) {
                    inputMethod(symbol);
                }
                break;
            }else{
                if(i == temp.length-1){
                    // 추천단어를 찾지 못하고 수동입력했을 경우 다음단어 입력을 위해 한칸 띄운다.
                    int spacebarIndex = searchIndex(keyboardLayout, " ");
                    touchPoint(keyboardLayout.get(spacebarIndex).x, keyboardLayout.get(spacebarIndex).y);
                }
            }
            predictResult.clear();
        }
    }

    // 추천단어 영역에 해당 추천단어가 있는지 찾는다.
    // 추천단어가 존재하면 해당 영역을 터치한다.
    // 조건 : 문장의 끝일경우 '를 제외한 특수문자가 들어갈경우 끝문장으로 간주하고 contains로 대조한다.
    //        문장의 중간일 경우 equals로 대조한다.
    private boolean isPredictionWordCheck(ArrayList<String> inputWord, String orgWord){
        String symbol = isSymbolCheck(orgWord);
        System.out.println(symbol);
        boolean isEnd, compare = false;
        if(inputWord != null){
            // 특수문자가 없는 일반 문자일 경우
            if(symbol == null){
                for(int i=0; i<inputWord.size(); i++){
                    if(inputWord.get(i).equals(orgWord)){
                        touchPredictionWord(i);
                        inputWord.clear();
                        return true;
                    }
                }
            }
            // 특수문자가 포함되었을 경우
            else{
                if(symbol.charAt(0) == 39){
                    System.out.println("어퍼스트로피 포함");
                    for(int i=0; i<inputWord.size(); i++){
                        if(inputWord.get(i).equals(orgWord)){
                            System.out.println("inputWord Count : " + i);
                            touchPredictionWord(i);
                            inputWord.clear();
                            return true;
                        }
                    }
                }else {
                    for(int i=0; i<inputWord.size(); i++){
                        if(inputWord.get(i).contains(orgWord)){
                            touchPredictionWord(i);
                            inputWord.clear();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // 3개의 추천단어의 좌표를 터치한다.
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

        System.out.println("Prediction Count : " + predictionCount);
    }

    private String isSymbolCheck(String inputWord){
        char[] temp = inputWord.toCharArray();
        if(String.valueOf(temp[temp.length-1]).contains(".")){
            return ".";
        }else if(String.valueOf(temp[temp.length-1]).contains(",")){
            return ",";
        }else if(String.valueOf(temp[temp.length-1]).contains("!")){
            return "!";
        }else if(String.valueOf(temp[temp.length-1]).contains("?")){
            return "?";
        }else if(String.valueOf(temp[temp.length-1]).contains("~")){
            return "~";
        }else if(String.valueOf(temp).contains("'")){
            return "'";
        }
        return null;
    }

    private int searchIndex(ArrayList<Keyboard_Layout> list, String searchStr){
        for(int i=0; i<list.size(); i++){
            if(list.get(i).keyValue.equals(searchStr)){
                return i;
            }
        }
        return -1;
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

        return Boolean.parseBoolean(nValue.getNodeValue());
    }
}
