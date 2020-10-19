package com.spring.auth.log.wrappers;


import com.spring.auth.log.streams.TeeServletOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

/**
 * @author diegotobalina created on 24/06/2020
 */
public class BufferedResponseWrapper implements HttpServletResponse {

    HttpServletResponse original;
    TeeServletOutputStream tee;
    ByteArrayOutputStream bos;

    public BufferedResponseWrapper(HttpServletResponse response) {
        original = response;
    }

    public String getContent() {
        if (bos == null) return null;
        return bos.toString();
    }

    public PrintWriter getWriter() throws IOException {
        return original.getWriter();
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if (tee == null) {
            bos = new ByteArrayOutputStream();
            tee = new TeeServletOutputStream(original.getOutputStream(), bos);
        }
        return tee;
    }

    @Override
    public String getCharacterEncoding() {
        return original.getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String charset) {
        original.setCharacterEncoding(charset);
    }

    @Override
    public String getContentType() {
        return original.getContentType();
    }

    @Override
    public void setContentType(String type) {
        original.setContentType(type);
    }

    @Override
    public void setContentLength(int len) {
        original.setContentLength(len);
    }

    @Override
    public void setContentLengthLong(long l) {
        original.setContentLengthLong(l);
    }

    @Override
    public int getBufferSize() {
        return original.getBufferSize();
    }

    @Override
    public void setBufferSize(int size) {
        original.setBufferSize(size);
    }

    @Override
    public void flushBuffer() throws IOException {
        tee.flush();
    }

    @Override
    public void resetBuffer() {
        original.resetBuffer();
    }

    @Override
    public boolean isCommitted() {
        return original.isCommitted();
    }

    @Override
    public void reset() {
        original.reset();
    }

    @Override
    public Locale getLocale() {
        return original.getLocale();
    }

    @Override
    public void setLocale(Locale loc) {
        original.setLocale(loc);
    }

    @Override
    public void addCookie(Cookie cookie) {
        original.addCookie(cookie);
    }

    @Override
    public boolean containsHeader(String name) {
        return original.containsHeader(name);
    }

    @Override
    public String encodeURL(String url) {
        return original.encodeURL(url);
    }

    @Override
    public String encodeRedirectURL(String url) {
        return original.encodeRedirectURL(url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String encodeUrl(String url) {
        return original.encodeUrl(url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String encodeRedirectUrl(String url) {
        return original.encodeRedirectUrl(url);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        original.sendError(sc, msg);
    }

    @Override
    public void sendError(int sc) throws IOException {
        original.sendError(sc);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        original.sendRedirect(location);
    }

    @Override
    public void setDateHeader(String name, long date) {
        original.setDateHeader(name, date);
    }

    @Override
    public void addDateHeader(String name, long date) {
        original.addDateHeader(name, date);
    }

    @Override
    public void setHeader(String name, String value) {
        original.setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        original.addHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        original.setIntHeader(name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
        original.addIntHeader(name, value);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setStatus(int sc, String sm) {
        original.setStatus(sc, sm);
    }

    @Override
    public String getHeader(String arg0) {
        return original.getHeader(arg0);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return original.getHeaderNames();
    }

    @Override
    public Collection<String> getHeaders(String arg0) {
        return original.getHeaders(arg0);
    }

    @Override
    public int getStatus() {
        return original.getStatus();
    }

    @Override
    public void setStatus(int sc) {
        original.setStatus(sc);
    }
}
