package Util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ScreenCaptureManager {
    private File file, temp;
    private String capturePath;
    private BufferedImage cutImage;
    private BaseDesiredCapabilities mDevice;
    private OCRManager ocrManager;

    public ScreenCaptureManager(BaseDesiredCapabilities mDevice, String capturePath){
        this.mDevice = mDevice;
        this.capturePath = capturePath;
        ocrManager = new OCRManager();
    }

    // 디바이스를 캡쳐하여 경로에 저장 후 인식된 문자를 Return한다.
    public String[] takeScreenShot(){
        String result[];
        try {
            temp = mDevice.getDriver().getScreenshotAs(OutputType.FILE);
            file = new File("D:TEST/_IMG/img.png");
            FileUtils.copyFile(temp, file);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            temp = null;
        }

        result = ocrManager.doOCR(file);

        return result;
    }
}
