package net.highersoft.mstats.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.highersoft.mstats.model.RequestEntity;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultPinServiceImpl implements IPinService {
	private static Log log=LogFactory.getLog(DefaultPinServiceImpl.class);
	public String getPin(HttpServletRequest req) {
		//记录日志分析问题      
		RequestEntity re=new RequestEntity();
		re.setHeader(getRequestHeaderValues(req));
		re.setParam(req.getParameterMap());
		JSONObject jsonObject = JSONObject.fromObject(re);
        log.info("req :"+req.getRequestURI()+jsonObject.toString());
		return "default";
	}
	
	public Map<String,String> getRequestHeaderValues(HttpServletRequest req) {
		Map<String,String> headerMap=new HashMap<String,String>();
		Enumeration<String> enums=req.getHeaderNames();
		while(enums.hasMoreElements()){
			Object key=enums.nextElement();			
			headerMap.put(String.valueOf(key), req.getHeader(String.valueOf(key)));
		}
		return headerMap;
	}
	public static void main(String args[]){
		Map headerMap=new HashMap();
		headerMap.put("223", "dfdf");
		
		RequestEntity re=new RequestEntity();
		re.setHeader(headerMap);
		re.setParam(headerMap);
		JSONObject jsonObject = JSONObject.fromObject(re);
		System.out.println(jsonObject.toString());
	}
	


}
