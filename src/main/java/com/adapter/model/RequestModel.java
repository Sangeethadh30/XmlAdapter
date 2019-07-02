package com.adapter.model;

import java.util.List;

/**  
* RequestModel.java - a simple model class mapped to request json.  
* @author  Vikas Singh
* @version 1.0 
*/ 
public class RequestModel {
	private String originalName;
	private String proposedName;
	private String xPath;
	private int noOfChilds;
	private List<Attributes> attributes;
	
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}
	public String getProposedName() {
		return proposedName;
	}
	public void setProposedName(String proposedName) {
		this.proposedName = proposedName;
	}
	public int getNoOfChilds() {
		return noOfChilds;
	}
	public void setNoOfChilds(int noOfChilds) {
		this.noOfChilds = noOfChilds;
	}
	public List<Attributes> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attributes> attributes) {
		this.attributes = attributes;
	}
	public String getxPath() {
		return xPath;
	}
	public void setxPath(String xPath) {
		this.xPath = xPath;
	}
}
