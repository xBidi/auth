package com.example.demo.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice @Slf4j public class ResponseBodyLogger implements ResponseBodyAdvice<Object> {

    @Autowired LoggingService loggingService;

    @Override public boolean supports(MethodParameter methodParameter,
        Class<? extends HttpMessageConverter<?>> aClass) {
        log.debug("{supports start}");
        log.debug("{supports end}");
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
        Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
        ServerHttpResponse serverHttpResponse) {
        log.debug("{beforeBodyWrite start}");
        if (serverHttpRequest instanceof ServletServerHttpRequest
            && serverHttpResponse instanceof ServletServerHttpResponse) {
            loggingService
                .logResponse(((ServletServerHttpRequest) serverHttpRequest).getServletRequest(),
                    ((ServletServerHttpResponse) serverHttpResponse).getServletResponse(), o);
        }
        log.debug("{beforeBodyWrite end}");
        return o;
    }


}
