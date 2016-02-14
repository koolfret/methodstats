package net.highersoft.mstats.interceptor;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author chengzhong
 *
 */
public interface IPinService {
	public String getPin(HttpServletRequest req);
}
