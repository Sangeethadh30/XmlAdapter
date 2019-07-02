package com.adapter.model;
/**  
* RequestModel.java - a simple model class mapped to request json.  
* @author  Vikas Singh
* @version 1.0 
*/ 
public class RequestModel {
	private String originalName;
	private String proposedName;
	private String parentNode;
	private String attributes;
	private int itemIndex;
	
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
	public String getParentNode() {
		return parentNode;
	}
	public void setParentNode(String parentNode) {
		this.parentNode = parentNode;
	}
	public int getItemIndex() {
		return itemIndex;
	}
	public void setItemIndex(int itemIndex) {
		this.itemIndex = itemIndex;
	}
	public String getAttributes() {
		return attributes;
	}
	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}
}
