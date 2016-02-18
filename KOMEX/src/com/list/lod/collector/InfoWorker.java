package com.list.lod.collector;

import java.util.ArrayList;
import java.util.List;

public class InfoWorker {
	
	private String type;
	private String domainName;
	private List<String> propList;
	
	public InfoWorker(String type, String domainName, List<String> propList) {
		super();
		this.type = type;
		this.domainName = domainName;
		this.propList = propList;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public List<String> getPropList() {
		return propList;
	}
	
	public void setPropList(List<String> propList) {
		//this.propList = propList;
		this.propList.addAll(propList);
	}
	
	public void addPropertyToList(String property){
		if(!this.propList.contains(property)){
			this.propList.add(property);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Worker Type ::: " + this.type + " Domain Name ::: " + domainName + " Property List ::: " + propList.toString());
		return sb.toString();
	}
}
