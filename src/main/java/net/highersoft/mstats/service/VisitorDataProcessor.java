package net.highersoft.mstats.service;

import java.util.List;

import net.highersoft.mstats.model.VisitorData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 访客数据处理thread，获取到访客数据的二级部门后，持久化到数据库
 * Created by chengzhong
 */
public class VisitorDataProcessor extends Thread{
    private static final Log log= LogFactory.getLog(VisitorDataProcessor.class);
    private ActionMethodService mstatsService;
    
    private static  long defTime =60000L ;
    
    
    public VisitorDataProcessor() {
		super();		
//		this.start();
//		log.info("Visitor 线程启动...");
		
	}
   
    

	
	public  void setDefTime(long defTime) {
		VisitorDataProcessor.defTime = defTime;
	}
	
	public ActionMethodService getMstatsService() {
		return mstatsService;
	}




	public void setMstatsService(ActionMethodService mstatsService) {
		this.mstatsService = mstatsService;
		
		
       /* try{
        	mstatsService.regist();
        }catch(Exception e){
        	log.error(e.getMessage(),e);
        }*/
	}





    public void run() {
        while (true)
        {
        	try {
                Thread.sleep(defTime);
            } catch (InterruptedException e) {
                log.error(e.getMessage(),e);
            }
            try{            	
                List<VisitorData> visitorDatas=mstatsService.pollVisitDatas();
                if(visitorDatas!=null){
                    log.info("current visitorData size:"+visitorDatas.size());
                    mstatsService.addVisitorDatas(visitorDatas);
                    log.info("current visitorData processed over");
                }            	

            }catch (Exception e){
            	if(log!=null){
            		log.error(e.getMessage(),e);
            	}
            }
            

        }
    }
}
