package com.adapter.util;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class FileReadWriteUtil {
	
	public JSONObject readJson() {
        Object obj = null;
        JSONParser parser = new JSONParser();
		try {
			obj = parser.parse(new FileReader("C:\\Sangeetha\\XMLJob\\XMLAdapter\\XmlAdapter\\src\\main\\resources\\input.json"));
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        JSONObject jo = (JSONObject) obj;
        System.out.println(jo.get("channel"));
        return jo;
	}
	
	public void readXMl() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document;
		try {
			builder = docFactory.newDocumentBuilder();
//			document = builder.parse(new URL("https://www.ft.com/?format=rss").openStream());
			document = builder.parse(new File("C:\\Sangeetha\\XMLJob\\XMLAdapter\\XmlAdapter\\src\\main\\resources\\sampleinput.xml"));
			
			/*Schema schema = null;
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);
			schema = factory.newSchema(new File("C:\\Sangeetha\\XMLJob\\XMLAdapter\\XmlAdapter\\src\\main\\resources\\sampleinput.xml"));
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));*/
			Element root = document.getDocumentElement();
			NodeList nodeList = document.getElementsByTagName("title");
			 for (int count = 0; count < nodeList.getLength(); count++) {
				 Node tempNode = nodeList.item(count);
				 System.out.println(tempNode.getParentNode());
				System.out.println(tempNode.getNodeName()+"---"+tempNode.getTextContent());
				System.out.println(tempNode.getNodeType());
			}
			System.out.println(document.getElementsByTagName("title"));
			System.out.println(root);
		} catch (ParserConfigurationException | SAXException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
