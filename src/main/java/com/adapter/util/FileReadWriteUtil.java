package com.adapter.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.adapter.model.RequestModel;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**  
* FileReadWriteUtil.java - This utility class handles JSON and XML read write functions 
* @author  Vikas Singh
* @version 1.0 
*/ 
public class FileReadWriteUtil {
	
	 /**  
	    * Read JSON file 
	    * map to RequestModel POJO class  
	  */  
	public List<RequestModel> readJson() {
		List<RequestModel> reqList = new ArrayList<>();
        /*Object obj = null;
        JSONParser parser = new JSONParser();*/
		try {
			/*obj = parser.parse(new FileReader("C:\\Sangeetha\\XMLJob\\XMLAdapter\\XmlAdapter\\src\\main\\resources\\input.json"));
	        JSONObject jo = (JSONObject) obj;
	        System.out.println(jo.get("channel"));*/
	        
	        JsonFactory factory = new JsonFactory();
	
	        ObjectMapper mapper = new ObjectMapper(factory);
	        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	        JsonNode rootNode = mapper.readTree(new FileReader("C:\\Sangeetha\\XMLJob\\XMLAdapter\\XmlAdapter\\src\\main\\resources\\input.json"));  
	
	        Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
	        while (fieldsIterator.hasNext()) {
	            Map.Entry<String,JsonNode> field = fieldsIterator.next();
	            RequestModel newReqModel = mapper.treeToValue(field.getValue(), RequestModel.class);
	            reqList.add(newReqModel);
	            System.out.println("Key: " + field.getKey() + "\tValue:" + field.getValue());
        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return reqList;
	}
	
	/**  
	    * this metos Reads the XML file 
	    * helps in finding XML attributes  
	  */ 
	public void readXMl(List<RequestModel> reqList) {
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
