package Util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XMLParseManager {
    private DocumentBuilder builder;
    private DocumentBuilderFactory factory;
    private Document document;
    private NodeList deviceNodeList;
    private NodeList keyboardNodeList;
    private String deviceXMLPath, keyboardXMLPath;

    public XMLParseManager(String deviceXMLPath, String keyboardXMLPath){
            this.deviceXMLPath = deviceXMLPath;
            this.keyboardXMLPath = keyboardXMLPath;
            factory = DocumentBuilderFactory.newInstance();

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }


        public void parseXML(String Mode){
            if(Mode.equals("Device")){
                try {
                    document = builder.parse(deviceXMLPath);
                    document.getDocumentElement().normalize();
                    deviceNodeList = document.getElementsByTagName("Device");
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(Mode.equals("Keyboard")){
                try {
                    document = builder.parse(keyboardXMLPath);
                    document.getDocumentElement().normalize();
                    deviceNodeList = document.getElementsByTagName("Keyboard");
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }





        }



    public NodeList getList(String Mode){
        if(Mode.equals("Device")){
            return deviceNodeList;
        }else if(Mode.equals("Keyboard")){
            return keyboardNodeList;
        }
        return null;
    }
}
