package cn.adelyn.blog.auth.filter;

import cn.adelyn.blog.auth.service.AliPayLoginService;
import cn.adelyn.blog.auth.service.TokenService;
import cn.adelyn.framework.cache.util.CaffeineCacheUtil;
import cn.adelyn.framework.core.context.UserInfoContext;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.pojo.bo.UserInfoBO;
import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.response.ServerResponse;
import cn.adelyn.framework.core.util.RandomIdUtil;
import cn.adelyn.framework.core.util.StringUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author chengze
 * @date 2022/12/7
 * @desc 授权过滤，子系统可以自己实现AuthConfigAdapter接口
 * 		 也可以在 adelyn.auth.unanth-uri 直接配置不需要授权的路径
 */
@AllArgsConstructor
@Slf4j
@Component
public class WebAuthFilter implements Filter {

	private final AntPathMatcher antPathMatcher;
	private final TokenService tokenService;
	private final AliPayLoginService aliPayLoginService;

	/**
	 * 无需登录权限 unwanted auth 的uri
	 */
	@Value("#{'${adelyn.auth.unauth.uriPatterns:}'.empty ? null : '${adelyn.auth.unauth.uriPatterns:}'.split(',')}")
	private LinkedList<String> unAuthUriPatternList;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		// 如果匹配不需要授权的路径，就不需要校验是否需要授权
		for (String excludePathPattern : unAuthUriPatternList) {
			if (antPathMatcher.match(excludePathPattern, req.getRequestURI())) {
				chain.doFilter(req, resp);
				return;
			}
		}

		// 未登录用户没有token，如果没在unAuthPatterns放行，判为未授权访问
		String token = req.getHeader("Authorization");

		if (!StringUtil.hasText(token)) {
			printServerResponseToWeb(ServerResponse.fail(ResponseEnum.UNLOGIN, getRedirectUrl()));
			return;
		}

		// 校验token，并返回用户信息
		UserInfoBO userInfoBO;
		try {
			userInfoBO = tokenService.validToken(token);
		} catch (Exception e) {
			userInfoBO = null;
		}

		if (Objects.isNull(userInfoBO)) {
			printServerResponseToWeb(ServerResponse.fail(ResponseEnum.UNLOGIN, getRedirectUrl()));
			return;
		}

		try {
			// 保存上下文
			UserInfoContext.set(userInfoBO);
			chain.doFilter(req, resp);
		} finally {
			UserInfoContext.clean();
		}
	}

	private String getRedirectUrl() {
		return aliPayLoginService.getAliPayRedirectUrl();
	}

	public <T> void printServerResponseToWeb(ServerResponse<T> serverResponse) {
		if (serverResponse == null) {
			log.info("print obj is null");
			return;
		}

		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		if (requestAttributes == null) {
			log.error("requestAttributes is null, can not print to web");
			return;
		}
		HttpServletResponse response = requestAttributes.getResponse();
		if (response == null) {
			log.error("httpServletResponse is null, can not print to web");
			return;
		}

		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter printWriter;
		try {
			printWriter = response.getWriter();
			printWriter.write(JSON.toJSONString(serverResponse));
		} catch (IOException e) {
			throw new AdelynException(ResponseEnum.FAIL, e);
		}
	}

}
