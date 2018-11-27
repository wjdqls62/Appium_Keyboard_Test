package Util;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class OCRManager {
    private Tesseract tesseract;
    private String[] tesseractResult;
    private String tessDataPath = "C://Program Files (x86)/Tesseract-OCR/tessdata";

    public OCRManager(){
        tesseract = new Tesseract();
        initTessract();
    }

    // Tess4J를 초기화 한다.
    private void initTessract(){
        tesseract.setDatapath(tessDataPath);
        tesseract.setOcrEngineMode(TessAPI.TessOcrEngineMode.OEM_DEFAULT);
        tesseract.setLanguage("eng");
    }

    // 스크린샷의 사각형 영역에서 텍스트를 읽어온 후 배열로 받아온다.
    public String[] doOCR(File file){
        // 추천단어가 표시되는 영역의 사각형 좌표
        Rectangle rect = new Rectangle(23, 1525, 1404, 115);
        try {
            tesseractResult = tesseract.doOCR(file, rect).split("\\s");
            for(int i=0; i<tesseractResult.length; i++){
                if(tesseractResult[i].equals("") || tesseractResult[i].equals("_")){
                    System.out.println("Null!!!!!!");
                }
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return tesseractResult;
    }
}
