package com.cm.httpserver;

import com.cm.util.Util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {
    // 常量
    private static final String CRLF = "\r\n";
    private static final String BLANK = " ";
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
            bw = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream())
            );
        } catch (IOException e) {
            resHead = null;
        }
    }

    // 构建响应行和响应首部
    private void createHeadInfo(int code) {
        // http协议和协议版本、状态码、简单描述
        resHead.append("HTTP/1.1").append(BLANK).append(code).append(BLANK);

        switch (code) {
            case 200:
                resHead.append("OK");
                break;
            case 404:
                resHead.append("NOT FOUND");
                break;
            case 500:
                resHead.append("SERVER ERROR");
                break;
        }

        resHead.append(CRLF);

        // 响应首部
        resHead.append("Server:CM Server/0.0.1").append(CRLF);
        resHead.append("Date:").append(new Date()).append(CRLF);
        resHead.append("Content-type:text/html;charset=GBK").append(CRLF);
        resHead.append("Content-Length:").append(len).append(CRLF);

        // 响应首部和响应正文之间的分隔符
        resHead.append(CRLF);
    }

    // 响应正文
    public Response println(String info) {
        resContent.append(info).append(CRLF);
        len += (info + CRLF).getBytes().length;

        return this;
    }

    // 发送到客户端
    public void pushToClient(int code) {
        if (resHead == null) {
            code = 500;
        }
        this.createHeadInfo(code);

        try {
            bw.append(resHead.toString());
            bw.append(resContent.toString());
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 关闭流
    public void close() {
        Util.closeIO(bw);
    }
}
