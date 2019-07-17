package com.adapter.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
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

import org.json.simple.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.*;
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
	private JSONObject jsonObject = new JSONObject();

	/**
	 * Read JSON file map to RequestModel POJO class
	 */
	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	public List<RequestModel> readJson(String jsonFilePath) {
		List<RequestModel> reqList = new ArrayList<>();

		try {
			JsonFactory factory = new JsonFactory();
			ObjectMapper mapper = new ObjectMapper(factory);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JsonNode rootNode = mapper
					.readTree(new File(
							getClass().getClassLoader().getResource("input1.json").getFile()));
			/*JsonNode rootNode = mapper
			.readTree(new File(jsonFilePath));*/
			Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
			while (fieldsIterator.hasNext()) {
				Map.Entry<String, JsonNode> field = fieldsIterator.next();
				RequestModel newReqModel = mapper.treeToValue(field.getValue(), RequestModel.class);
				reqList.add(newReqModel);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reqList;
	}

	private String getNewAttributeValue(String oldAtrributeName, RequestModel requestModel) {
		String newsAttributeValue = oldAtrributeName;
		if (!CollectionUtils.isEmpty(requestModel.getAttributes())
				&& !StringUtils.isEmpty(requestModel.getAttributes())) {

			for (int i = 0; i < requestModel.getAttributes().size(); i++) {
				if (oldAtrributeName.equals(requestModel.getAttributes().get(i).getAttrName())) {
					newsAttributeValue = requestModel.getAttributes().get(i).getAttrNewValue();
					break;
				}
			}
		}
		return newsAttributeValue;
	}

	private void writeToJson() {
		PrintWriter pw;
		try {
			pw = new PrintWriter("JSONExample.json");
			pw.write(jsonObject.toJSONString());

			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void mapToJson(String key, String value) {
		Gson gson = new Gson();
		jsonObject.put(key, gson.toJson(value));
	}

	@SuppressWarnings("unchecked")
	private void mapToJson(String key, List<ResponseTagModel> value) {
		Gson gson = new Gson();
		jsonObject.put(key, gson.toJson(value));

	}

	/**
	 * this method Reads the XML file helps in finding XML attributes
	 */
	public void readXML(List<RequestModel> reqList,String xmlURL){
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			Document document;

			builder = docFactory.newDocumentBuilder();
			docFactory.setNamespaceAware(true);
//			document = builder.parse(new URL(xmlURL).openStream());
			document = builder
					.parse(new File(getClass().getClassLoader().getResource("sampleInput1.xml").getFile()));
			new NamespaceResolverutil(document);
			for (RequestModel requestModel : reqList) {
				String xpathExpression = requestModel.getxPath();
				if(xpathExpression.contains("[@xmlns")) {
					int startIndex = xpathExpression.indexOf("[@xmlns");
					int endIndex = xpathExpression.indexOf(']');
					StringBuilder sb = new StringBuilder(xpathExpression);
					xpathExpression =sb.delete(startIndex, endIndex+1).toString();
				}
				XPathFactory xPathfactory = XPathFactory.newInstance();
				XPath xpath = xPathfactory.newXPath();
				xpath.setNamespaceContext(new NamespaceResolverutil(document));
				XPathExpression compile = xpath.compile(xpathExpression);
				NodeList nodeList = (NodeList) compile.evaluate(document, XPathConstants.NODESET);
				displayNodeList(nodeList, requestModel);
				writeToJson();
			}
		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
			e.printStackTrace();
		}

	}

	private void displayNodeList(NodeList nodeList, RequestModel requestModel) {
		String key = "";
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
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
			}
		}
	}

	private void getChildNodeAndAttribute(NodeList childNodes, RequestModel requestModel) {
		List<Attributes> attributesList = null;
		ResponseTagModel tagModel = null;
		String proposedName = requestModel.getProposedName();
		Attributes attributes = null;
		String key = "";
		List<ResponseTagModel> tagModelList = new ArrayList<>();
		String newAttrValue = "";
		int noOfChildcount = 1; 
		boolean restrictChildNodes = calculateNoOfChilds(requestModel.getNoOfChilds());
		for (int j = 0; j < childNodes.getLength(); j++) {
			attributesList = new ArrayList<>();
			tagModel = new ResponseTagModel();
			Node child = childNodes.item(j);
			short nodeType = child.getNodeType();
			if (nodeType == 1) {
				if (child.hasAttributes()) {
					NamedNodeMap nodeMap = child.getAttributes();

					for (int p = 0; p < nodeMap.getLength(); p++) {
						Node attrNode = nodeMap.item(p);
						newAttrValue = attrNode.getNodeName();
						if (!CollectionUtils.isEmpty(requestModel.getAttributes())
								&& !StringUtils.isEmpty(requestModel.getAttributes().get(p).getAttrNewValue()))
							newAttrValue = getNewAttributeValue(attrNode.getNodeName(), requestModel);
						attributes = new Attributes();
						attributes.setAttrName(newAttrValue);
						attributes.setAttrNewValue(attrNode.getNodeValue());
						attributesList.add(attributes);
					}
					tagModel.setAttributes(attributesList);
					if (child.hasChildNodes()){
						tagModel.setTextValue(child.getTextContent());
						newAttrValue = getNewAttributeValue(child.getNodeName(), requestModel);
						tagModel.setTagName(newAttrValue);
					}
					tagModelList.add(tagModel);
					if(restrictChildNodes && noOfChildcount == Integer.parseInt(requestModel.getNoOfChilds()))
						break;
					else 
						noOfChildcount++;
				} 
			}
		}
		if (StringUtils.isEmpty(proposedName))
			key = requestModel.getOriginalName();
		else
			key = proposedName;
		mapToJson(key, tagModelList);
	}

	private boolean calculateNoOfChilds(String childCount) {
		if(childCount.equalsIgnoreCase("all"))
				return false;
		return true;
	}
}
