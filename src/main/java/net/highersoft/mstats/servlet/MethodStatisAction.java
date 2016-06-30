package net.highersoft.mstats.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.highersoft.mstats.service.ActionMethodService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

/**
 * 
 * @author chengzhong
 *
 */
public class MethodStatisAction {
	private static Log log=LogFactory.getLog(MethodStatisAction.class);
	private ActionMethodService mstatsService;
	
	
	
	public void setMstatsService(ActionMethodService mstatsService) {
		this.mstatsService = mstatsService;
	}

	
	
	
	public Object process(String fullUrl,String configPath,HttpServletRequest request) throws Exception{
		if("/query.json".equals(fullUrl)){
			Map<String,Object> retMap=query(request);
			
			return  retMap;
			
		}else{
			log.info("未知请求:fullUrl:"+fullUrl);
			return "未知请求";
		}
	}
	
	
	
	public Map<String,Object> query(HttpServletRequest req){
		Map<String,Object> chartData=new HashMap();
		SimpleDateFormat datesdf=new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");		
		String url=req.getParameter("url");
		if(url==null){
			url="-1";
		}
		int sysId=1;
		/*
		String sysIds=req.getParameter("selSys");
		if(StringUtils.isNotBlank(sysIds)){
			sysId=Integer.valueOf(sysIds);
		}else{
			List<Map<String,Object>> sys=mstatsService.querySysNames();
			if(sys!=null&&sys.size()>0){
				Map<String,Object> sysMap=sys.get(0);
				sysId=Integer.valueOf(String.valueOf(sysMap.get("system_id")));
			}
		}*/
		int startDate=0;
		int endDate=0;
		String startDates=req.getParameter("startDate");
		if(StringUtils.isNotBlank(startDates)){			
			try{
				startDate=Integer.valueOf(datesdf.format(sdf.parse(startDates)));
				String endDates=req.getParameter("endDate");		
				endDate=Integer.valueOf(datesdf.format(sdf.parse(endDates)));
			}catch(Exception e){
				log.error(e.getMessage(),e);
			}
		}else{			
			endDate=Integer.valueOf(datesdf.format(Calendar.getInstance().getTime()));
			Calendar now=Calendar.getInstance();
			now.add(Calendar.DATE, -15);		
			startDate=Integer.valueOf(datesdf.format(now.getTime()));
		}
		
		try {
			chartData.put("startDate", sdf.format(datesdf.parse(String.valueOf(startDate))));
			chartData.put("endDate", sdf.format(datesdf.parse(String.valueOf(endDate))));
		} catch (ParseException e) {
			log.error(e.getMessage(),e);
		}
		
		
		
		List<Map<String,Object>> downloadData=mstatsService.queryDownLoadData(sysId,url, startDate, endDate);
		if(downloadData!=null){
			List<String> cates=new ArrayList<String>();
			Map<String,Object> downNum=new HashMap<String,Object>();
			downNum.put("name", "访问次数");
			downNum.put("data", new ArrayList<Integer>());
			Map<String,Object> pathNum=new HashMap<String,Object>();
			pathNum.put("name", "方法数");			
			pathNum.put("data", new ArrayList<Integer>());
			Map<String,Object> erpNum=new HashMap<String,Object>();
			erpNum.put("name", "用户数");
			erpNum.put("data", new ArrayList<Integer>());
			
			
			for(Map<String,Object> m:downloadData){
				cates.add(String.valueOf(m.get("create_date")));
				((ArrayList<Integer>)downNum.get("data")).add(Integer.valueOf(String.valueOf(m.get("num"))));
				((ArrayList<Integer>)pathNum.get("data")).add(Integer.valueOf(String.valueOf(m.get("pathNum"))));
				((ArrayList<Integer>)erpNum.get("data")).add(Integer.valueOf(String.valueOf(m.get("erpNum"))));
			}
			Map[]	seriesData=new Map[]{downNum,pathNum,erpNum};
			//格式化日期
			if(cates!=null){
				for(int i=0;i<cates.size();i++){
					try {
						cates.set(i, sdf.format(datesdf.parse(cates.get(i))));
					} catch (ParseException e) {
						log.error(e.getMessage(),e);
					}
				}
			}
			chartData.put("categorys", cates);
			chartData.put("seriesData", seriesData);
			
		}		
		//系统
		/*List<Map<String,Object>> sysNameObjs=mstatsService.querySysNames();
		Map<String,String> sysNames=new HashMap<String,String>();
		if(sysNameObjs!=null){
			for(Map<String,Object> m:sysNameObjs){
				sysNames.put(String.valueOf(m.get("system_id")), String.valueOf(m.get("system_name")));
			}
			chartData.put("selSys", sysNames);
		}
		chartData.put("selSyskey", sysId);*/
		
		
		//路径		
		Map<String,String> allPath=new HashMap<String,String>();
		/*Map<String,String> pathMap=mstatsService.querySystemPaths(sysId);
		if(pathMap!=null){
			for(Entry<String,String> entry:pathMap.entrySet()){
				
				allPath.put(entry.getKey(), entry.getValue());
				
				
			}
		}*/
		allPath.put("-1", "全部");		
		allPath.putAll(ActionMethodService.pathInfo);
		chartData.put("url",allPath);
		chartData.put("selurlkey", url);
	
		
		//表格数据
		try {
			List<List<String>> tabData=mstatsService.queryDownLoadErpData(sysId,url, startDate, endDate);
			chartData.put("tabData", tabData);
		} catch (ParseException e) {
			log.error(e.getMessage(),e);
		}
		
		return chartData;
	}
}
