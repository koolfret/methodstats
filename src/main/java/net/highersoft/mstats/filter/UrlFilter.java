package net.highersoft.mstats.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.highersoft.mstats.service.ActionMethodService;
import net.highersoft.mstats.servlet.ResourceServlet;

public class UrlFilter implements Filter {
	private static Log log=LogFactory.getLog(UrlFilter.class);
	private ActionMethodService mstatsService;	

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		mstatsService=ResourceServlet.getBean(ActionMethodService.class);
	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		String currentPath=((HttpServletRequest)req).getServletPath();
		try {
			mstatsService.putVisitData(null, currentPath);
		}catch(Exception e) {
			log.error(e.getMessage(),e);
		}
		chain.doFilter(req, resp);
        //mstatsService.putVisitData(null,currentPath);
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
