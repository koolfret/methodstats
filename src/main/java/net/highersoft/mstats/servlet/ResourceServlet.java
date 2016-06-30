/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.highersoft.mstats.servlet;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.highersoft.mstats.service.ActionMethodService;
import net.highersoft.mstats.service.ConfigService;
import net.highersoft.mstats.util.Utils;
import net.sf.json.JSONObject;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public   class ResourceServlet extends HttpServlet {

    private final static Log   log                 = LogFactory.getLog(ResourceServlet.class);
	

 
    public static ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext("/methodstatis/resources/spring_mstats_sqllite.xml"); 
    protected  String     resourcePath="methodstatis/resources";
    private String parentPath;
    //数据库路径
    private String dbPath;
    //配置文件路径
    private static String configPath;
    //本次启动是否执行了初始化
    private   boolean initThisTime=false;
    private MethodStatisAction methodStatisAction;
    /*public ResourceServlet(){
    	 try {    	
    		 super.init();
			init();
		} catch (ServletException e) {
			log.error(e.getMessage(),e);
		}
    }*/
    
   
    public static String getConfigPath(){
    	return configPath;
    }
    
	
    @Override
    public void init(ServletConfig config) throws ServletException {
    	
        try {
			initAuthEnv(config);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
		}
    }

    private void initAuthEnv(ServletConfig config) throws FileNotFoundException {
    	this.parentPath=config.getInitParameter("configPath");
    	if(StringUtils.isBlank(this.parentPath) ||!new File(this.parentPath).exists()){
    		this.parentPath=System.getProperty("catalina.base");
    	}
        if(StringUtils.isBlank(parentPath)){
        	log.error("web.xml中配置的ResourceServlet的configPath参数为空!");
        	throw new RuntimeException("配置错误");
        }
        //读配置
        MethodStatisAction ma=context.getBean(MethodStatisAction.class);
        methodStatisAction=ma;
        BasicDataSource datas=context.getBean(BasicDataSource.class);
        datas.setUrl(ConfigService.getDbPath(parentPath));        
        
        //检查配置
        initThisTime=ConfigService.checkConfig(parentPath);
        dbPath=ConfigService.getDbPath(parentPath);
        configPath=ConfigService.getConfigPath(parentPath);
        
        //设置拦截路径
    	InputStream in = new FileInputStream(configPath);
    	Properties props = new Properties();
		try {			
			props.load(new InputStreamReader(in,"UTF-8"));
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		Map<String,String> paths=new HashMap<String,String>();
        Set<Object> keys=props.keySet();
        for(Object key:keys){
        	paths.put(String.valueOf(key), props.getProperty(String.valueOf(key)));
        }
        ActionMethodService.setPathInfo(paths);
        
    }

    

    protected String getFilePath(String fileName) {
        return resourcePath + fileName;
    }

    protected void returnResourceFile(String fileName, String uri, HttpServletResponse response)
                                                                                                throws ServletException,
                                                                                                IOException {

        String filePath = getFilePath(fileName);
        if (fileName.endsWith(".jpg")||fileName.endsWith(".gif")) {
            byte[] bytes = Utils.readByteArrayFromResource(filePath);
            if (bytes != null) {
                response.getOutputStream().write(bytes);
            }

            return;
        }

        String text = Utils.readFromResource(filePath);
        if (text == null) {
            response.sendRedirect(uri + "/index.html");
            return;
        }
        if (fileName.endsWith(".css")) {
            response.setContentType("text/css;charset=utf-8");
        } else if (fileName.endsWith(".js")) {
            response.setContentType("text/javascript;charset=utf-8");
        }
        response.getWriter().write(text);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {    	
        String contextPath = request.getContextPath();
        String servletPath = request.getServletPath();
        String requestURI = request.getRequestURI();
        JSONObject jsonObj=new JSONObject();

        response.setCharacterEncoding("utf-8");

        if (contextPath == null) { // root context
            contextPath = "";
        }
        String uri = contextPath + servletPath;
        String path = requestURI.substring(contextPath.length() + servletPath.length());

        if ("/".equals(path)) {
            response.sendRedirect("index.html");
            return;
        }

        if (path.contains(".json")) {
            String fullUrl = path;
            if (request.getQueryString() != null && request.getQueryString().length() > 0) {
                fullUrl += "?" + request.getQueryString();
            }
           // response.getWriter().print(process(fullUrl));
            Object obj=null;
            try{
            	obj=methodStatisAction.process(fullUrl,parentPath,request);
            }catch(Exception e){
            	log.error(e.getMessage(),e);
            }
            jsonObj.put("data", obj);
            if(initThisTime){
            	jsonObj.put("initInfo", "本次启动执行了初始化程序,请编辑"+ResourceServlet.getConfigPath()+"文件设置过滤的URL和功能名.如:/methodstatis/GameAction!personalIncome.action=个人所得税");
			}
            jsonObj.put("dbPath", dbPath);
            jsonObj.put("configPath", configPath);
            response.getWriter().print(jsonObj);
            return;
        }

        // find file in resources path
        returnResourceFile(path, uri, response);
    }

   

    

    /*protected String getRemoteAddress(HttpServletRequest request) {
        String remoteAddress = null;
        
        if (remoteAddressHeader != null) {
            remoteAddress = request.getHeader(remoteAddressHeader);
        }
        
        if (remoteAddress == null) {
            remoteAddress = request.getRemoteAddr();
        }
        
        return remoteAddress;
    }*/

   
}
