package com.spring.auth.log.wrappers;


import com.spring.auth.log.streams.BufferedServletInputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author diegotobalina created on 24/06/2020
 */
public class BufferedRequestWrapper extends HttpServletRequestWrapper implements ServletRequest {

    private byte[] buffer = null;

    public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
        super(req);
        // Read InputStream and store its content in a buffer.
        InputStream is = req.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int read;
        while ((read = is.read(buf)) > 0) {
            baos.write(buf, 0, read);
        }
        this.buffer = baos.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream bais = new ByteArrayInputStream(this.buffer);
        BufferedServletInputStream bsis = new BufferedServletInputStream(bais);
        return bsis;
    }

    public String getRequestBody() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
        String line = null;
        StringBuilder inputBuffer = new StringBuilder();
        do {
            line = reader.readLine();
            if (null != line) {
                inputBuffer.append(line.trim());
            }
        } while (line != null);
        reader.close();
        return inputBuffer.toString().trim();
    }
}
