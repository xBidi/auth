package com.spring.auth.exceptions.util;

import com.spring.auth.exceptions.domain.ErrorResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * @author diegotobalina created on 22/06/2020
 */
public abstract class ExceptionUtil {

    public static ErrorResponse getErrorResponse(
            HttpServletRequest request, HttpStatus forbidden, Exception e) {
        String timestamp = new Timestamp(System.currentTimeMillis()).toString();
        int status = forbidden.value();
        String error = forbidden.name();
        String message = getMessage(e);
        String path = request.getRequestURI();
        return new ErrorResponse(timestamp, status, error, message, path);
    }

    private static String getMessage(final Exception ex) {
        // final String methodName = getMethodName(ExceptionUtils.getRootCause(ex));
        final String rootCause = ExceptionUtils.getRootCauseMessage(ex);
        return String.format("%s", rootCause);
    }

    private static String getMethodName(final Throwable cause) {
        Throwable rootCause = cause;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause)
            rootCause = rootCause.getCause();
        final String[] classNameSplit = rootCause.getStackTrace()[0].getClassName().split("\\.");
        final String className = classNameSplit[classNameSplit.length - 1];
        final String methodName = rootCause.getStackTrace()[0].getMethodName();
        return String.format("%s.%s", className, methodName);
    }
}
