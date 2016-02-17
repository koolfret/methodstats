# methodstats
java web项目方法调用次数统计组件

1.在pom.xml里添加依赖	
	
	&lt;dependency&gt;
		&lt;groupId&gt;net.highersoft&lt;/groupId&gt;
		&lt;artifactId&gt;methodstatis&lt;/artifactId&gt;
		&lt;version&gt;0.0.1&lt;/version&gt;
	&lt;/dependency&gt;
	
	
2.在web.xml里配置，注意configPath是你存放数据库文件的路径
	
	&lt;servlet&gt;
		&lt;servlet-name&gt;methodstatis&lt;/servlet-name&gt;
		&lt;servlet-class&gt;net.highersoft.mstats.servlet.ResourceServlet&lt;/servlet-class&gt;
		&lt;init-param&gt;  
	       &lt;param-name&gt;configPath&lt;/param-name&gt;  
	       &lt;param-value&gt;E:/dev_soft/apache-tomcat-7.0.39&lt;/param-value&gt;  
	    &lt;/init-param&gt;
	    &lt;load-on-startup&gt;0&lt;/load-on-startup&gt; 
	&lt;/servlet&gt;
	&lt;servlet-mapping&gt;
		&lt;servlet-name&gt;methodstatis&lt;/servlet-name&gt;
		&lt;url-pattern&gt;/methodstatis/*&lt;/url-pattern&gt;
	&lt;/servlet-mapping&gt;
	
3.访问http://yoursite/methodstatis/index.html	