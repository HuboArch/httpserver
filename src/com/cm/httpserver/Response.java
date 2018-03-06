package com.cm.httpserver;

import com.cm.util.Util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {
    // 常量
    public static final String CRLF = "\r\n";
    public static final String BLANK = " ";
    // 输出流
    private BufferedWriter bw;
    // 响应头
    private StringBuilder resHead;
    // 响应正文
    private StringBuilder resContent;
    // 响应正文的字节长度
    private int len;

    public Response() {
        this.resHead = new StringBuilder();
        this.resContent = new StringBuilder();
        this.len = 0;
    }

    public Response(Socket client) {
        this();

        try {
            this.bw = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream())
            );
        } catch (IOException e) {
            resHead = null;
        }
    }

    // 构建响应头
    private void createHeadInfo(int code) {
        // http协议和协议版本、状态码、简单描述
        this.resHead.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);

        switch (code) {
            case 200:
                this.resHead.append("OK");
                break;
            case 404:
                this.resHead.append("NOT FOUND");
                break;
            case 500:
                this.resHead.append("SERVER ERROR");
                break;
        }

        this.resHead.append(CRLF);

        // 响应头信息
        resHead.append("Server:CM Server/0.0.1").append(CRLF);
        resHead.append("Date:").append(new Date()).append(CRLF);
        resHead.append("Content-type:text/html;charset=GBK").append(CRLF);
        resHead.append("Content-Length:").append(this.len).append(CRLF);
        // 响应头和响应正文之间的分隔符
        resHead.append(CRLF);
    }

    // 构建正文+换行
    public void println(String info) {
        this.resContent.append(info).append(CRLF);
        this.len += (info + CRLF).getBytes().length;
    }

    // 发送到客户端
    public void pushToClient(int code) {
        if (null == this.resHead) {
            code = 500;
        }
        this.createHeadInfo(code);

        try {
            bw.append(this.resHead.toString());
            bw.append(this.resContent.toString());
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 关闭流
    public void close() {
        Util.closeIO(this.bw);
    }
}
