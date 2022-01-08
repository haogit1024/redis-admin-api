package com.czh.redis.framework.wrapper;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        // 把 body 的内容存起来
        try {
            this.body = getRequestBody(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] getRequestBody(HttpServletRequest request) throws IOException {
        String body = IoUtil.read(request.getInputStream(), StandardCharsets.UTF_8);
//        System.out.println("requestWrapper body: " + body);
        log.info("requestWrapper body: {}", body);
        return body.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        // 其他地方使用的时候把 body 的内容返回
        ByteArrayInputStream bais = new ByteArrayInputStream(this.body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
