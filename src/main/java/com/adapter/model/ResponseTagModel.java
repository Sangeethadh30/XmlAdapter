package com.adapter.model;

import java.util.List;

public class ResponseTagModel {
	private String tagName;
	private String tagValue;
	private String textValue;
	private List<Attributes> attributes;
	
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagValue() {
		return tagValue;
	}
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
	public String getTextValue() {
		return textValue;
	}
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	public List<Attributes> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attributes> attributes) {
		this.attributes = attributes;
	}

}
