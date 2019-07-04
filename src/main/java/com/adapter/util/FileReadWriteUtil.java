package com.adapter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.*;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.adapter.model.Attributes;
import com.adapter.model.RequestModel;
import com.adapter.model.ResponseTagModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


/**  
* FileReadWriteUtil.java - This utility class handles JSON and XML read write functions 
* @author  Vikas Singh
* @version 1.0 
*/ 
public class FileReadWriteUtil {
	private JSONObject jo = new JSONObject();
	private JSONObject mainJo = new JSONObject();
	private JSONArray joArray;	
	 /**  
	    * Read JSON file 
	    * map to RequestModel POJO class  
	  */  
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
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
	        JsonNode rootNode = mapper.readTree(new FileReader("C:\\XML Job\\Workspace\\XmlAdapter\\src\\main\\resources\\input1.json"));  
	
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
	public void readXMl(List<RequestModel> reqList) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document;
		try {
			builder = docFactory.newDocumentBuilder();
//			document = builder.parse(new URL("https://www.ft.com/?format=rss").openStream());
			document = builder.parse(new File("C:\\Sangeetha\\XMLJob\\XMLAdapter\\XmlAdapter\\src\\main\\resources\\sampleInput1.xml"));
			int i =0;
			Element root = document.getDocumentElement();
			for(RequestModel requestModel:reqList) {
				String originalName = requestModel.getOriginalName();
				int index = 0;

			NodeList nodeList = document.getElementsByTagName(originalName);
			 for (int count = 0; count < nodeList.getLength(); count++) {
				 //System.out.println("Nodelist Length : "+ nodeList.getLength());
				 Node tempNode = nodeList.item(count);
				 String xmlParentName = tempNode.getParentNode().getNodeName();
				 String configParentName = null;
				if(xmlParentName.equalsIgnoreCase(configParentName))
				 {
					 if(index >0)
					 {
						i = i + 1;
						tempNode =  getTempNodeForIndexElement(i,tempNode,index);
					 }
					 if(tempNode != null) {
				System.out.println("nodeName "+tempNode.getNodeName() + "   ParentNode :" + tempNode.getParentNode().getNodeName());
				System.out.println("nodeValue "+tempNode.getTextContent());
					 }
				 }
				 if (tempNode.hasAttributes()) {
						// get attributes names and values
						NamedNodeMap nodeMap = tempNode.getAttributes();
						for (int p = 0; p < nodeMap.getLength(); p++) {
							Node node = nodeMap.item(p);
							System.out.println("attr name : " + node.getNodeName());
							System.out.println("attr value : " + node.getNodeValue());
						}
					}
			}
			
		}
		}catch (ParserConfigurationException | SAXException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void readAndExtractXML(List<RequestModel> reqList) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document;
		try {
			builder = docFactory.newDocumentBuilder();
//			document = builder.parse(new URL("https://www.ft.com/?format=rss").openStream());
			document = builder.parse(new File("C:\\XML Job\\Workspace\\XmlAdapter\\src\\main\\resources\\sampleInput1.xml"));
			int i =0;
			Element root = document.getDocumentElement();
			String inputXpath = "";
			String singleValue = "";
			String key = "";
			String newAttrValue = "";
			for(RequestModel requestModel:reqList) {
				joArray  = new JSONArray();
				String proposedName = requestModel.getProposedName();
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				NodeList nodeList = null;
				int nodeListCount = 0;
				inputXpath = requestModel.getxPath();
				if(!StringUtils.isEmpty(inputXpath)) {
					XPathExpression expr = xpath.compile(inputXpath);
					if(inputXpath.endsWith("text()")) {
						singleValue = (String) expr.evaluate(document, XPathConstants.STRING);
						if(StringUtils.isEmpty(proposedName))
							key = requestModel.getOriginalName();
						else
							key = proposedName;
						mapToJson(key,singleValue);
						continue;
					}
					nodeList =  (NodeList)expr.evaluate(document, XPathConstants.NODE);
				}
				List<ResponseTagModel> tagModelList = new ArrayList<>();
				List<Attributes> attributesList = null;
				if(requestModel.getNoOfChilds().equalsIgnoreCase("all"))
					nodeListCount = nodeList.getLength();
				else {
					nodeListCount = Integer.parseInt(requestModel.getNoOfChilds());
				}
				if(nodeList != null && nodeList.getLength()>0) {
					for (int count = 0; count <nodeListCount;  count++) {
						 attributesList = new ArrayList<>();
						 ResponseTagModel tagModel = new ResponseTagModel();
						 Attributes attributes = null;
						 Node node = nodeList.item(count);
						 if(node.hasAttributes()) {
							 tagModel.setTagName(node.getNodeName());
							 tagModel.setTextValue(node.getTextContent());
							System.out.println("Node name : " + node.getNodeName());
							System.out.println("Node value : " + node.getTextContent());
							NamedNodeMap nodeMap = node.getAttributes();
							for (int p = 0; p < nodeMap.getLength(); p++) {
								Node attrNode = nodeMap.item(p);
								if(node.hasAttributes()) {
									attributes = new Attributes();
									newAttrValue = attrNode.getNodeName();
									if(!CollectionUtils.isEmpty(requestModel.getAttributes()) && !StringUtils.isEmpty(requestModel.getAttributes().get(p).getAttrNewValue()))
										newAttrValue = requestModel.getAttributes().get(p).getAttrNewValue();
									attributes.setAttrName(newAttrValue);
									attributes.setAttrNewValue(attrNode.getNodeValue());
									System.out.println("attr name : " + attrNode.getNodeName());
									System.out.println("attr value : " + attrNode.getNodeValue());
									attributesList.add(attributes);
								}
								tagModel.setAttributes(attributesList);
								tagModelList.add(tagModel);
//								jo.put(attrNode.getNodeName(),attrNode.getNodeValue());		
//								joArray.add(jo);								
							}
							
						 }
					 }
				}
				 
				 if(StringUtils.isEmpty(proposedName))
						key = requestModel.getOriginalName();
					else
						key = proposedName;
					mapToJson(key,tagModelList);
//					joArray.add(jo);
//					mainJo.put(key,joArray);
			}
			writeToJson();
		}catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	private void writeToJson() {
		PrintWriter pw;
		try {
			pw = new PrintWriter("JSONExample.json");
			pw.write(jo.toJSONString()); 
	          
	        pw.flush(); 
	        pw.close(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	}
	}

	@SuppressWarnings("unchecked")
	private void mapToJson(String key,String value) {
		//mainJo.put(key, value);
		Gson gson = new Gson();
		jo.put(key, gson.toJson(value));
	}
	@SuppressWarnings("unchecked")
	private void mapToJson(String key,List<ResponseTagModel> value) {
		Gson gson = new Gson();
		jo.put(key, gson.toJson(value));
//		joArray.add(jo);
		
	}

	private Node getTempNodeForIndexElement(int i, Node tempNode, int index) {
		if(i != index) {
			//System.out.println("This is not Index Element");
			tempNode = null;
		}
			
		return tempNode;
		
	}

}
