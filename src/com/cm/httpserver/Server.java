package com.cm.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 创建服务器端套接字
 */

public class Server {
    public static final String CRLF = "\r\n";
    public static final String BLANK = " ";

    private ServerSocket server;

    public Server() {
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try {
            server = new ServerSocket(8888);
            this.receive();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receive() {
        try {
            // 侦听客户端的连接请求
            Socket client = server.accept();

            byte[] buf = new byte[20480];
            int len = client.getInputStream().read(buf);

            // 输出浏览器的请求信息
            String requestInfo = new String(buf, 0, len).trim();
            System.out.println(requestInfo);

            // 响应
            Response response = new Response(client);

            response.println("<html><head><title>Response</title></head><body>hello CM server!</body></html>");
            response.pushToClient(200);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
