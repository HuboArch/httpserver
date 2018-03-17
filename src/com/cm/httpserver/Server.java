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

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try {
            server = new ServerSocket(8989);
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

            // 打印客户端的请求信息
            String requestInfo = new String(buf, 0, len).trim();
            System.out.println(requestInfo);

            Request request = new Request(requestInfo);
            Response response = new Response(client);

            Servlet servlet = new Servlet();
            servlet.service(request, response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
