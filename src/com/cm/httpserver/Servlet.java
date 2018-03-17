package com.cm.httpserver;

public class Servlet {

    public void service(Request request, Response response) {

        response.println("<html><head><title>Hello CM server</title>");
        response.println("<meta charset='UTF-8'>");
        response.println("</head>");
        response.println("<body><h1>Socket Connected</h1>");

        response.println("Welcome ").println(request.getParameterValue("name")).println("be here again.");

        response.println("</body></html>");
        response.pushToClient(200);

    }
}
