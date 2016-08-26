# methodstats
java web项目方法调用次数统计组件

1.在pom.xml里添加依赖	
	<pre><code>
	<dependency>
		<groupId>net.highersoft</groupId>
		<artifactId>methodstatis</artifactId>
		<version>1.0.0</version>
	</dependency>
	</code></pre>
	
	
2.在web.xml里配置，注意configPath是你存放数据库文件的路径
		<pre><code>
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
	</code></pre>
3.访问http://yoursite/methodstatis/index.html	

4.实现说明
j2ee容器(tomcat)启动时,会加载组件里的spring配置,本组件使用的默认数据库是sqllite,configPath的值就是数据库文件路径。
${configPath}/methodstatis/methodstatis.properties里配置需统计的路径。这个路径需要在配置相应的拦截器，如struts的如下配置:
<pre><code>
		<!-- 访问统计 --> 
		<interceptors>
			<interceptor name="mstats" class="net.highersoft.mstats.interceptor.ActionMethodInterceptor" />
	       	
			<interceptor-stack name="mstatsSecurityInterceptor">				
				<interceptor-ref name="mstats" />				 
				
			</interceptor-stack>
		</interceptors>
		<default-interceptor-ref name="mstatsSecurityInterceptor" />
</code></pre>	