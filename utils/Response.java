package utils;

import java.io.OutputStream;
import java.io.PrintWriter;
import model.HTTPContentType;
import model.HTTPStatus;

public class Response {
    private PrintWriter out;
    HTTPStatus status;
    HTTPContentType type;
    String body = "";

    public Response(OutputStream outputStream) {
        this.out = new PrintWriter(outputStream, true);
    }

    public void send() {
        out.println("HTTP/1.1 " + this.status.toString());
        out.println("Content-Length: " + this.body.length());
        out.println("Content-Type: " + this.type.toString());
        // Connection: close is fine for HTTP/1.1 (which is all we need for now), but is prohibited in higher versions
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Connection
        out.println("Connection: close"); 
        out.println();
        out.println(this.body);
    }

    public void send(String body, HTTPStatus status, HTTPContentType type) {
        out.println("HTTP/1.1 " + status.toString());
        out.println("Content-Length: " + body.length());
        out.println("Content-Type: " + type.toString());
        out.println("Connection: close");
        out.println();
        out.println(body);
    }
    
}
