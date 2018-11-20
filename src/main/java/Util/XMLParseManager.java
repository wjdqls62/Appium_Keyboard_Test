package Util;

import org.w3c.dom.Document;
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
    private NodeList nodeList;
    private String xmlPath;

    public XMLParseManager(String xmlPath){
        try {
            this.xmlPath = xmlPath;
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            document = builder.parse(xmlPath);
            document.getDocumentElement().normalize();
            nodeList = document.getElementsByTagName("Device");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NodeList getList(){
        return nodeList;
    }
}
