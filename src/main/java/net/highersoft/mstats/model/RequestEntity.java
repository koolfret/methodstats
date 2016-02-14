package net.highersoft.mstats.model;

import java.util.Map;

public class RequestEntity {
	private Map  header;
	private Map  param;
	public Map getHeader() {
		return header;
	}
	public void setHeader(Map header) {
		this.header = header;
	}
	public Map getParam() {
		return param;
	}
	public void setParam(Map param) {
		this.param = param;
	}
	
}
