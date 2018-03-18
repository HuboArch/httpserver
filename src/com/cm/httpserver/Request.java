package com.cm.httpserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;

public class Request {
    private static final String CRLF = "\r\n";
    // 请求方式
    private String method;
    // URL
    private String url;
    // 查询参数
    private Map<String, List<String>> queryString;

    public String getRequestInfo() {
        return requestInfo;
    }

    private String requestInfo;

    public Request() {
        this.method = "";
        this.url = "";
        this.queryString = new HashMap<>();

        parseRequestInfo();
    }

    public Request(Socket client) {
        this();

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(client.getInputStream())
            );

            StringBuilder stringBuilder = new StringBuilder();
            String store = "";
            while ((store = br.readLine()) != null) {
                stringBuilder.append(store);
            }

            requestInfo = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 分析请求信息
    private void parseRequestInfo() {

        if (requestInfo == null || (requestInfo = requestInfo.trim()).equals("")) {
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
        method = firstLine.substring(0, firIdx).trim();

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

        if (method.equalsIgnoreCase("post")) {
            url = urlStr;
            paramString = requestInfo.substring(requestInfo.lastIndexOf(CRLF)).trim();
        } else if (method.equalsIgnoreCase("get")) {
            if (urlStr.contains("?")) {
                // 如果存在查询参数
                String[] urlArr = urlStr.split("\\?");

                url = urlArr[0];
                paramString = urlArr[1];
            } else {
                url = urlStr;
            }
        }

        if (paramString.equals("")) {
            return;
        }

        // 将请求参数封装到 Map中
        parseParams(paramString);
    }

    private void parseParams(String paramString) {
        String[] arr = paramString.split("&");

        for (String str : arr) {
            String[] keyValue = str.split("=");

            if (keyValue.length == 1) {
                keyValue = Arrays.copyOf(keyValue, 2);
            }

            String key = keyValue[0];
            String value = keyValue[1] == null ? null : decodeUtil(keyValue[1], "gbk");

            // 分拣存储
            if (!queryString.containsKey(key)) {
                queryString.put(key, new ArrayList<>());
            }

            List<String> strings = queryString.get(key);
            strings.add(value);
        }
    }

    /**
     * 在查询字符串中，根据 key 返回相应的字符串数组
     *
     * @param key queryString 中的 key
     * @return 字符串数组
     */
    public String[] getParameterValues(String key) {
        List<String> values = queryString.get(key);

        if (values == null) {
            return null;
        } else {
            return values.toArray(new String[0]);
        }
    }

    /**
     * 在查询字符串中，根据 key 返回对应的值
     *
     * @param key queryString 中的 key
     * @return 对应的值
     */
    public String getParameterValue(String key) {
        String[] values = getParameterValues(key);

        return values == null ? null : values[0];
    }

    // MIME 解码
    private String decodeUtil(String value, String code) {
        try {
            return URLDecoder.decode(value, code);
        } catch (UnsupportedEncodingException ignored) {
        }

        return null;
    }
}
