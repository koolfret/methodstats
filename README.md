# methodstats
java web项目方法调用次数统计组件

1.在pom.xml里添加依赖	
	
	<dependency>
		<groupId>net.highersoft</groupId>
		<artifactId>methodstatis</artifactId>
		<version>0.0.1</version>
	</dependency>
	
	
2.在web.xml里配置，注意configPath是你存放数据库文件的路径
	
	<servlet>
		<servlet-name>methodstatis</servlet-name>
		<servlet-class>net.highersoft.mstats.servlet.ResourceServlet</servlet-class>
		<init-param>  
	       <param-name>configPath</param-name>  
	       <param-value>E:/dev_soft/apache-tomcat-7.0.39</param-value>  
	    </init-param>
	    <load-on-startup>0</load-on-startup> 
	</servlet>
	<servlet-mapping>
		<servlet-name>methodstatis</servlet-name>
		<url-pattern>/methodstatis/*</url-pattern>
	</servlet-mapping>
	
3.访问http://yoursite/methodstatis/index.html	
