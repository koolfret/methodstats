package com.helijia.user.web.handler;

import static com.helijia.user.util.UserCode.USER_TOKEN_EXPIRED;
import static com.helijia.user.util.UserCode.USER_TOKEN_INVALID;
import static java.lang.String.format;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.helijia.common.api.model.ApiException;
import com.helijia.user.api.model.UserTokenModel;
import com.helijia.user.api.service.UserTokenService;
import com.helijia.user.web.common.UserAuthContext;

/**
 * <pre>
 * 鉴权, 验证Token有效性
 * 协议
 * 1. 支持HTTP HEADER, Authorization, 如
 * Authorization: accessToken zlRfURJ92kb1WYvxRZl9
 * 
 * 2. 支持URL参数, http://apiserver/api?accessToken=zlRfURJ92kb1WYvxRZl9
 * 
 * 优先使用HTTP HEADER, HEADER不存在, 使用URL参数.
 * </pre>
 *
 * @author jinli Dec 18, 2015
 */
public class UserTokenAuthInterceptor implements HandlerInterceptor {

    private static final String HEADER_AUTHORICATION = "Authorization";
    private static final String AUTHORIZATION_ACCESS_TOKEN = "accessToken";

    @Autowired
    private UserTokenService userTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = getAccessToken(request);
        UserTokenModel token = userTokenService.getUserTokenByAccessToken(accessToken);
        if (token == null) {
            String msg = format("token [%s] not found.", accessToken);
            throw new ApiException(msg).build(USER_TOKEN_INVALID.getApiCode(), USER_TOKEN_INVALID.getApiMessage());
        }
        if (isTokenExpired(token)) {
            String msg = format("token [%s] is expired [expiration:%tc; now: %tc].", accessToken, token.getTokenExpiration(), new Date());
            throw new ApiException(msg).build(USER_TOKEN_EXPIRED.getApiCode(), USER_TOKEN_EXPIRED.getApiMessage());
        }
        UserAuthContext.setUserToken(token);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserAuthContext.setUserToken(null);
    }

    protected String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORICATION);
        if (header != null) {
            String[] tokens = header.split(" ");
            if (tokens == null || tokens.length != 2 || !AUTHORIZATION_ACCESS_TOKEN.equals(tokens[0].trim())) {
                String msg = format("header authorization [%s] is invalid.", header);
                throw new ApiException(msg).build(USER_TOKEN_INVALID.getApiCode(), USER_TOKEN_INVALID.getApiMessage());
            }
            return tokens[1].trim();
        } else {
            String token = request.getParameter(AUTHORIZATION_ACCESS_TOKEN);
            if (token == null || token.isEmpty()) {
                throw new ApiException("url authorization is invalid.").build(USER_TOKEN_INVALID.getApiCode(), USER_TOKEN_INVALID.getApiMessage());
            }
            return token.trim();
        }
    }

    protected boolean isTokenExpired(UserTokenModel token) {
        return token.getTokenExpiration() == null || token.getTokenExpiration().getTime() < new Date().getTime();
    }

}
