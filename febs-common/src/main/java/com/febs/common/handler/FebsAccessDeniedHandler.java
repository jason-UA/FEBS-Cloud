package com.febs.common.handler;

import com.febs.common.entity.FebsResponse;
import com.febs.common.utils.FebsUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FebsAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        FebsResponse febsResponse = new FebsResponse();
        FebsUtil.makeResponse(
                httpServletResponse, MediaType.APPLICATION_JSON_VALUE,
                HttpServletResponse.SC_FORBIDDEN, febsResponse.message("没有权限访问该资源"));
    }
}
