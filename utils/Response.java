package utils;

import java.io.OutputStream;
import java.io.PrintWriter;
import model.HTTPContentType;
import model.HTTPStatus;

public class Response {
    private PrintWriter out;

    public Response(OutputStream outputStream) {
        this.out = new PrintWriter(outputStream);
    }

    public void send(String body, HTTPStatus status, HTTPContentType type) {
        out.println("HTTP/1.1 " + status.toString());
        out.println("Content-Length: " + body.length());
        out.println("Content-Type: " + type.toString());
        out.println();
        out.println(body);
        // I prefer to flush entire output at once, rather than one line at a time.
        out.flush();
    }
    
}
