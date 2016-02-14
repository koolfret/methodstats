package net.highersoft.mstats.service;




import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;

import net.highersoft.mstats.dao.VisitorDataDao;
import net.highersoft.mstats.model.VisitorData;
import net.highersoft.mstats.util.ListMapConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author chengzhong
 *
 */
public class ActionMethodService {
    private static final Log log= LogFactory.getLog(ActionMethodService.class);
    private static ArrayBlockingQueue<VisitorData> visitorDatas;
    private VisitorDataDao visitorDataDao;
   // public static final int DOWNLOAD=1;
    /*
	 * <Path,中文名>	
	 */
	public static Map<String,String> pathInfo=new HashMap<String,String>();
	private Boolean recordAll=false;
	private static int sysId = 0 ;
	private static String sysName = "?" ;
	
	
    public Boolean getRecordAll() {
		return recordAll;
	}

	public void setRecordAll(Boolean recordAll) {
		this.recordAll = recordAll;
	}

	public static int getSysId() {
		return sysId;
	}

	public  void setSysId(int sysId) {
		ActionMethodService.sysId = sysId;
	}

	public static  void setPathInfo(Map<String, String> pathInfo) {
		ActionMethodService.pathInfo = pathInfo;
	}
    
	

	public  String getSysName() {
		return sysName;
	}

	public  void setSysName(String sysName) {
		ActionMethodService.sysName = sysName;
	}

	public Map<String, String> getPathInfo() {
		return pathInfo;
	}

	public VisitorDataDao getVisitorDataDao() {
        return visitorDataDao;
    }

    public void setVisitorDataDao(VisitorDataDao visitorDataDao) {
        this.visitorDataDao = visitorDataDao;
    }

    public ArrayBlockingQueue<VisitorData> getVisitorDatas() {
        return visitorDatas;
    }

    public void setVisitorDatas(ArrayBlockingQueue<VisitorData> visitorDatas) {
        this.visitorDatas = visitorDatas;
    }
    
   
    public void putVisitData(String erpId,String menuPath)
    {
        if(StringUtils.isNotBlank(erpId)&&StringUtils.isNotBlank(menuPath))
        {
        	//如果不是记录全部，并且不在配置范围内则退出
        	if(!recordAll&&!pathInfo.containsKey(menuPath)){
        		return;
        	}
            VisitorData visitorData=new VisitorData();
            visitorData.setSystemId(sysId);
            visitorData.setErpId(erpId);
            visitorData.setMenuPath(menuPath);
            visitorData.setVisitDate(new Date());
            boolean b=visitorDatas.offer(visitorData);
            if(!b)
            {
                log.warn("visitor data container is full,please check processor status,or change maxser!");
            }
        }
    }
    public List<VisitorData> pollVisitDatas(){
        List<VisitorData> list=new ArrayList<VisitorData>();
        if(!visitorDatas.isEmpty()){
            VisitorData visitorData=null;
            while ((visitorData=visitorDatas.poll())!=null){
                list.add(visitorData);
            }
        }
        return list;
    }

    /**
     *
     * @param visitorData
     */
    public void addVisitorData(VisitorData visitorData)
    {
        visitorDataDao.addVisitorData(visitorData);
    }

    /**
     * 定时处理一批数据
     * @param visitorDatas
     */
    public void addVisitorDatas(List<VisitorData> visitorDatas) {
        if(visitorDatas!=null)
        {
            for(VisitorData visitorData:visitorDatas){            	
            	visitorDataDao.addVisitorData(visitorData);
            }
        }
    }
    
    /**
     * 向服务器注册
     * 
     */
    public void regist(){
    	Map<String,String> pathTypes=querySystemPaths(sysId);
    	   	
    	if(pathTypes!=null){    		   		
    		if(!pathInfo.equals(pathTypes)){
    			visitorDataDao.delSystemInfos(sysId);
    			visitorDataDao.addSystemInfos(pathInfo,sysId,sysName);
    		}    		
    	}else{
    		visitorDataDao.addSystemInfos(pathInfo,sysId,sysName);
    	}
    }
    
    
    
    
    
    public List<Map<String,Object>> queryDownLoadData(int sysId,String url,int startDate,int endDate){
    	Map<String,Object> param=new HashMap<String,Object>();
    	param.put("systemId", sysId);    	
    	param.put("startDate", startDate);
    	param.put("endDate", endDate);   
    	if("-1".equals(url)){
    		return visitorDataDao.queryTotalVisitorData(param);
    	}else{
    		//String typeUrl[]=url.split("\\?");
    		//param.put("pathType", typeUrl[0]);
    		param.put("url", url);
    		return visitorDataDao.queryPathVisitorData(param);
    	}
    }
    
    public List<List<String>> queryDownLoadErpData(int sysId,String url,int istartDate,int iendDate) throws ParseException{
    	LinkedHashMap<Object,String> cols=new LinkedHashMap<Object,String>();
    	List<String> head=new ArrayList<String>();
    	head.add("");
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
    	SimpleDateFormat showsdf=new SimpleDateFormat("yyyy-MM-dd");
    	Date startDate=sdf.parse(String.valueOf(istartDate));
    	Date endDate=sdf.parse(String.valueOf(iendDate));
    
    	Calendar cal=Calendar.getInstance();    	
    	Date tmp=(Date)startDate.clone();
    	while(!tmp.after(endDate)){
    		head.add(showsdf.format(tmp));
    		cols.put(Integer.valueOf(sdf.format(tmp)),"");
    		cal.setTime(tmp);
    		cal.add(Calendar.DATE, 1);
    		tmp=cal.getTime(); 
    	}
    	
    	Map<String,Object> param=new HashMap<String,Object>();
    	param.put("systemId", sysId);
    	//param.put("pathType", DOWNLOAD);    	
    	param.put("startDate", istartDate);
    	param.put("endDate", iendDate);
    	
    	
    	List<Map<String,Object>> datas;
    	if("-1".equals(url)){
    		datas=visitorDataDao.queryTotalVisitorErpData(param);
    	}else{
    		/*String typeUrl[]=url.split("\\?");
    		param.put("pathType", typeUrl[0]);*/
    		param.put("url", url);
    		datas=visitorDataDao.queryPathVisitorErpData(param);
    	}
    	
    	List<List<String>> tabData=ListMapConverter.rowtocol(cols, "erp_id", "create_date", "num", datas);
    	
    	List<List<String>> rst=new ArrayList();
    	rst.add(head);
    	rst.addAll(tabData);
    	return rst;
    }
    
    public Map<String,String> querySystemPaths(int sysId){
    	List<Map<String,Object>> sysInfos=visitorDataDao.querySystemInfos(sysId);
    	Map<String,String> pathInfos=new HashMap(); 
    	for(Map<String,Object> m:sysInfos){
			
			pathInfos.put(String.valueOf(m.get("path")), String.valueOf(m.get("path_name")));
		} 
    	return pathInfos;
    }
    
    
    
    public List<Map<String,Object>> querySysNames(){
    	return visitorDataDao.querySystemNames();
    }
}
