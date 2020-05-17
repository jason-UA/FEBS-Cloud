package com.febs.gateway.filter;

import com.febs.common.entity.FebsResponse;
import com.febs.common.utils.FebsUtil;
import com.netflix.zuul.context.RequestContext;
import io.lettuce.core.dynamic.support.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;

@Slf4j
@Component
public class FebsGatewayErrorFilter extends SendErrorFilter {

    @Override
    public Object run() {
        try {
            FebsResponse febsResponse = new FebsResponse();
            RequestContext context = RequestContext.getCurrentContext();
            String serviceId = (String) context.get(FilterConstants.SERVICE_ID_KEY);

            ExceptionHolder exceptionHolder = findZuulException(context.getThrowable());
            String errorCause = exceptionHolder.getErrorCause();
            Throwable throwable = exceptionHolder.getThrowable();
            String message = throwable.getMessage();
            message = StringUtils.isBlank(message) ? errorCause : message;
            febsResponse = resolveExceptionMessage(message, serviceId, febsResponse);

            HttpServletResponse response = context.getResponse();
            FebsUtil.makeResponse(response, MediaType.APPLICATION_JSON_VALUE,
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR, febsResponse);
            log.error("Zull sendError：{}", febsResponse.getMessage());
        } catch (Exception exception) {
            log.error("Zuul sendError", exception);
            ReflectionUtils.rethrowRuntimeException(exception);
        }
        return null;
    }

    private FebsResponse resolveExceptionMessage(String message, String serviceId, FebsResponse febsResponse) {
        if (StringUtils.containsIgnoreCase(message, "time out")) {
            return febsResponse.message("请求" + serviceId + "服务超时");
        }
        if (StringUtils.containsIgnoreCase(message, "forwarding error")) {
            return febsResponse.message(serviceId + "服务不可用");
        }
        return febsResponse.message("Zuul请求" + serviceId + "服务异常");
    }
}
