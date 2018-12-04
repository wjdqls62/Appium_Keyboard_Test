package Util;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class OCRManager {
    private Tesseract tesseract;
    private ArrayList<String> tesseractResult;
    private String tessDataPath = "C://Program Files (x86)/Tesseract-OCR/tessdata";

    public OCRManager(){
        tesseract = new Tesseract();
        tesseractResult = new ArrayList<String>();
        initTessract();
    }

    // Tess4J를 초기화 한다.
    private void initTessract(){
        tesseract.setDatapath(tessDataPath);
        tesseract.setOcrEngineMode(TessAPI.TessOcrEngineMode.OEM_DEFAULT);
        tesseract.setLanguage("eng");
    }

    // 스크린샷의 사각형 영역에서 텍스트를 읽어온 후 배열로 받아온다.
    public ArrayList<String> doOCR(File file){
        // 추천단어가 표시되는 영역의 사각형 좌표
        Rectangle rect = new Rectangle(10, 1375, 1420, 215);
        try {
            String[] temp = tesseract.doOCR(file, rect).split("\\s");
            setArrayList(temp);

        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return tesseractResult;
    }

    private void setArrayList(String[] result){
        for(int i=0; i < result.length; i++){
            if(!result[i].equals("_") || !result[i].equals("") || !result[i].equals(" ")){
                System.out.println("Tesseract Result[" + i +"] : " + result[i]);
                tesseractResult.add(result[i]);
            }
        }
    }
}
