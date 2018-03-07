package com.cm.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Request {
    // 请求方式
    private String method;
    // URL
    private String url;
    // 查询参数
    private Map<String, List<String>> queryString;

    public static final String CRLF = "\r\n";
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


    }
}
