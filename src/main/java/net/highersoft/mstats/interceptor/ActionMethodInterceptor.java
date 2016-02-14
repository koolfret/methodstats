package net.highersoft.mstats.interceptor;

import javax.servlet.http.HttpServletRequest;

import net.highersoft.mstats.service.ActionMethodService;
import net.highersoft.mstats.servlet.ResourceServlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 
 * @author chengzhong
 *
 */
public class ActionMethodInterceptor  implements Interceptor  {
    private final static Log log = LogFactory.getLog(ActionMethodInterceptor.class);
    private ActionMethodService mstatsService;
    private IPinService pinService;
   
    
    

	public ActionMethodService getMstatsService() {
		return mstatsService;
	}

	public void setMstatsService(ActionMethodService mstatsService) {
		this.mstatsService = mstatsService;
	}

	public void setPinService(IPinService pinService) {
		this.pinService = pinService;
	}

	/**
     * 鍩轰簬褰撳墠鐧诲綍鐢ㄦ埛缁熻pv uv鐨勬嫤鎴櫒锛岄渶瑕侀厤缃湪sso鐨勬嫤鎴櫒鍚庨潰,浠ヤ究褰撳墠context鏈夊綋鍓嶇櫥褰曠敤鎴蜂俊鎭�
     * @param invocation
     * @return
     * @throws Exception
     */
    public String intercept(ActionInvocation invocation) throws Exception {
        try{
        	if(mstatsService==null){
    			mstatsService=ResourceServlet.context.getBean(ActionMethodService.class);
    		}
    		if(pinService==null){
    			pinService=ResourceServlet.context.getBean(IPinService.class);
    		}
    		
            ActionContext actionContext = invocation.getInvocationContext();
            HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
            String erpId=pinService.getPin(request);
            String currentPath=request.getServletPath();

            mstatsService.putVisitData(erpId,currentPath);

        }catch (Exception e)
        {
            log.error(e.getMessage(),e);
        }

        return invocation.invoke();  //To change body of implemented methods use File | Settings | File Templates.
    }

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}
}
