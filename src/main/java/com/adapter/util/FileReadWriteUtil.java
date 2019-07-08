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
 * FileReadWriteUtil.java - This utility class handles JSON and XML read write
 * functions
 * 
 * @author Vikas Singh
 * @version 1.0
 */
public class FileReadWriteUtil {
	private JSONObject jo = new JSONObject();

	/**
	 * Read JSON file map to RequestModel POJO class
	 */
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	public List<RequestModel> readJson() {
		List<RequestModel> reqList = new ArrayList<>();

		try {
			JsonFactory factory = new JsonFactory();
			ObjectMapper mapper = new ObjectMapper(factory);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JsonNode rootNode = mapper
					.readTree(new FileReader("C:\\XML Job\\Workspace\\XmlAdapter\\src\\main\\resources\\input1.json"));

			Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry<String, JsonNode> field = fieldsIterator.next();
				RequestModel newReqModel = mapper.treeToValue(field.getValue(), RequestModel.class);
				reqList.add(newReqModel);
				System.out.println("Key: " + field.getKey() + "\tValue:" + field.getValue());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reqList;
	}

	/**
	 * this metos Reads the XML file helps in finding XML attributes
	 */
	@SuppressWarnings("unchecked")
	public void readAndExtractXML(List<RequestModel> reqList) {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document;
		try {
			builder = docFactory.newDocumentBuilder();
//			document = builder.parse(new URL("https://www.ft.com/?format=rss").openStream());
			document = builder
					.parse(new File("C:\\XML Job\\Workspace\\XmlAdapter\\src\\main\\resources\\sampleInput.xml"));

			String inputXpath = "";
			String singleValue = "";
			String key = "";
			String newAttrValue = "";
			for (RequestModel requestModel : reqList) {
				String proposedName = requestModel.getProposedName();
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				NodeList nodeList = null;
				int nodeListCount = 0;
				inputXpath = requestModel.getxPath();
				if (!StringUtils.isEmpty(inputXpath)) {
					XPathExpression expr = xpath.compile(inputXpath);

					if (inputXpath.endsWith("text()")) {
						singleValue = (String) expr.evaluate(document, XPathConstants.STRING);
						if (StringUtils.isEmpty(proposedName))
							key = requestModel.getOriginalName();
						else
							key = proposedName;
						mapToJson(key, singleValue);
						continue;
					}

					nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODE);
				}
				List<ResponseTagModel> tagModelList = new ArrayList<>();
				List<Attributes> attributesList = null;

				if (nodeList != null && nodeList.getLength() > 0) {
					// System.out.println(nodeList.getLength());
					nodeList = fetchValidNodes(nodeList);
					// System.out.println(nodeList.getLength());
					if (requestModel.getNoOfChilds().equalsIgnoreCase("all"))
						nodeListCount = nodeList.getLength();
					else {
						nodeListCount = Integer.parseInt(requestModel.getNoOfChilds());
					}
					for (int count = 0; count < nodeListCount; count++) {
						// System.out.println(nodeList.item(count).getNodeName());
						attributesList = new ArrayList<>();
						ResponseTagModel tagModel = new ResponseTagModel();
						Attributes attributes = null;
						Node node = nodeList.item(count);
						System.out.println("Node name : " + node.getNodeName());
						System.out.println("Node value : " + node.getTextContent());
						if (node != null) {
							System.out.println(node.getChildNodes().getLength());
							tagModel.setTagName(node.getNodeName());
							tagModel.setTextValue(node.getTextContent());
							System.out.println("Node name : " + node.getNodeName());
							System.out.println("Node value : " + node.getNodeValue());
							if (node.hasAttributes()) {
								NamedNodeMap nodeMap = node.getAttributes();
								for (int p = 0; p < nodeMap.getLength(); p++) {
									Node attrNode = nodeMap.item(p);

									attributes = new Attributes();
									newAttrValue = attrNode.getNodeName();
									if (!CollectionUtils.isEmpty(requestModel.getAttributes()) && !StringUtils
											.isEmpty(requestModel.getAttributes().get(p).getAttrNewValue()))
										newAttrValue = getNewAttributeValue(attrNode.getNodeName(),
												requestModel.getAttributes());
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

				if (StringUtils.isEmpty(proposedName))
					key = requestModel.getOriginalName();
				else
					key = proposedName;
				mapToJson(key, tagModelList);
//					joArray.add(jo);
//					mainJo.put(key,joArray);
			}
			writeToJson();
		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private String getNewAttributeValue(String oldAtrributeName, List<Attributes> attributes) {
		String newsAttributeValue = oldAtrributeName;
		for (int i = 0; i < attributes.size(); i++) {
			if (oldAtrributeName.equals(attributes.get(i).getAttrName())) {
				newsAttributeValue = attributes.get(i).getAttrNewValue();
				break;
			}
		}
		return newsAttributeValue;
	}

	private NodeList fetchValidNodes(NodeList nodeList) {

		Node node = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			node = nodeList.item(i);
			if (node instanceof Text) {
				node.getParentNode().removeChild(node);
			}
		}
		return nodeList;
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
	private void mapToJson(String key, String value) {
		Gson gson = new Gson();
		jo.put(key, gson.toJson(value));
	}

	@SuppressWarnings("unchecked")
	private void mapToJson(String key, List<ResponseTagModel> value) {
		Gson gson = new Gson();
		jo.put(key, gson.toJson(value));

	}

	private Node getTempNodeForIndexElement(int i, Node tempNode, int index) {
		if (i != index) {
			// System.out.println("This is not Index Element");
			tempNode = null;
		}

		return tempNode;

	}

	public void readXML(List<RequestModel> reqList)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document;

		builder = docFactory.newDocumentBuilder();
//		document = builder.parse(new URL("https://www.ft.com/?format=rss").openStream());

		for (RequestModel requestModel : reqList) {
			document = builder
					.parse(new File("C:\\XML Job\\Workspace\\XmlAdapter\\src\\main\\resources\\sampleInput1.xml"));

			String xpathExpression = "/rss/catalog/product[@description=\"Cardigan Sweater\"]/catalog_item[2]//size[2]";
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			XPathExpression compile = xpath.compile(xpathExpression);
			NodeList nodeList = (NodeList) compile.evaluate(document, XPathConstants.NODESET);
			displayNodeList(nodeList, requestModel);
		}

	}

	private void displayNodeList(NodeList nodeList, RequestModel requestModel) {

		String key = "";
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String NodeName = node.getNodeName();

			NodeList childNodes = node.getChildNodes();
			if (childNodes.getLength() > 1) {
				getChildNodeAndAttribute(childNodes, requestModel);
			} else {
				String proposedName = requestModel.getProposedName();
				if (StringUtils.isEmpty(proposedName))
					key = requestModel.getOriginalName();
				else
					key = proposedName;
				mapToJson(key, node.getTextContent());
				System.out.format("\n Node Name:[%s], Text[%s] ", NodeName, node.getTextContent());
			}
		}
	}

	private void getChildNodeAndAttribute(NodeList childNodes, RequestModel requestModel) {
		List<Attributes> attributesList = new ArrayList<>();
		ResponseTagModel tagModel = new ResponseTagModel();
		String proposedName = requestModel.getProposedName();
		Attributes attributes = null;
		String key = "";
		List<ResponseTagModel> tagModelList = new ArrayList<>();
		String newAttrValue = "";

		for (int j = 0; j < childNodes.getLength(); j++) {

			Node child = childNodes.item(j);
			short nodeType = child.getNodeType();
			if (nodeType == 1) {
				System.out.format("\n\t Node Name:[%s], Text[%s] ", child.getNodeName(), child.getTextContent());
				if (child.hasAttributes()) {
					NamedNodeMap nodeMap = child.getAttributes();

					for (int p = 0; p < nodeMap.getLength(); p++) {
						Node attrNode = nodeMap.item(p);
						attributes = new Attributes();
						newAttrValue = attrNode.getNodeName();
						if (!CollectionUtils.isEmpty(requestModel.getAttributes())
								&& !StringUtils.isEmpty(requestModel.getAttributes().get(p).getAttrNewValue()))
							newAttrValue = getNewAttributeValue(attrNode.getNodeName(), requestModel.getAttributes());
						attributes.setAttrName(newAttrValue);
						attributes.setAttrNewValue(attrNode.getNodeValue());
						System.out.println("attr name : " + attrNode.getNodeName());
						System.out.println("attr value : " + attrNode.getNodeValue());
						attributesList.add(attributes);

						tagModel.setAttributes(attributesList);
						tagModelList.add(tagModel);
					}
				}
			}
		}
		if (StringUtils.isEmpty(proposedName))
			key = requestModel.getOriginalName();
		else
			key = proposedName;
		mapToJson(key, tagModelList);

	}

}
