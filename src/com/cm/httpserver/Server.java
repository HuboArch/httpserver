package com.cm.httpserver;

import com.cm.util.Util;

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
        start(8888);
    }

    public void start(int port) {
        try {
            server = new ServerSocket(port);
            receive();
        } catch (IOException e) {
            //stop();
        }
    }

    private void receive() {
        try {
            // 侦听客户端的连接请求
            Socket client = server.accept();

            Request request = new Request(client);
            Response response = new Response(client);

            Servlet servlet = new Servlet();
            servlet.service(request, response);

        } catch (IOException e) {
            // stop();
        }
    }

}
