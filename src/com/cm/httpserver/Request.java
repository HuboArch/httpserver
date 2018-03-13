package com.cm.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Request {
    // 请求方式
    private String method;
    // URL
    private String url;
    // 查询参数
    private Map<String, List<String>> queryString;

    private static final String CRLF = "\r\n";
    private InputStream is;
    private String requestInfo;

    public Request() {
        this.method = "";
        this.url = "";
        this.queryString = new HashMap<String, List<String>>();
        this.requestInfo = "";
    }

    public Request(InputStream is) {
        this();
        this.is = is;

        try {
            byte[] buf = new byte[1024];
            int len = is.read(buf);

            this.requestInfo = new String(buf, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        }

        parseRequestInfo();
    }

    // 分析请求信息
    private void parseRequestInfo() {

        if (null == requestInfo || (requestInfo = requestInfo.trim()).equals("")) {
            return;
        }

        /*
         * =====================================
         * get /src/index.html?name=dean&age=22 http/1.1
         * =====================================
         * */

        // 查询字符串
        String paramString = "";

        // 请求首行信息
        String firstLine = requestInfo.substring(0, requestInfo.indexOf(CRLF));
        // / 第一次出现的位置索引
        int firIdx = firstLine.indexOf("/");
        this.method = firstLine.substring(0, firIdx).trim();

        String urlStr = firstLine.substring(firIdx, firstLine.indexOf("HTTP/")).trim();

        /*
         * =============
         * 简单设定请求方式为 get 和 post
         *
         * post方式，请求参数在请求正文内
         *
         * get方式，请求参数如果有，则存在于请求首行信息的 url 内
         *
         * =============
         * */

        if (this.method.equalsIgnoreCase("post")) {
            this.url = urlStr;
            paramString = requestInfo.substring(requestInfo.lastIndexOf(CRLF)).trim();
        } else if (this.method.equalsIgnoreCase("get")) {
            if (urlStr.contains("?")) {
                // 如果存在查询参数
                String[] urlArr = urlStr.split("\\?");

                this.url = urlArr[0];
                paramString = urlArr[1];
            } else {
                this.url = urlStr;
            }
        }

        if (paramString.equals("")) {
            return;
        }

        // 将请求参数封装到 Map中
        parseParams(paramString);
    }

    private void parseParams(String paramString) {
        String queryStr = "name=dean&age=12&fav=football&fav=movie&fav=music";


    }
}
