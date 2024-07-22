package utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import model.HTTPRequestMethod;

public class Request {
    public HTTPRequestMethod method;
    public String path;
    public Map<String, String> headers = new HashMap<>();
    public String body;

    public Request(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        String[] requestLine = line.split(" ");
        this.method = HTTPRequestMethod.valueOf(requestLine[0]);
        this.path = requestLine[1];

        while ((line = reader.readLine()).length() > 0) {
            if (line.contains(":")) {
                String[] tokens = line.split(": ");
                this.headers.put(tokens[0], tokens[1]);
            }
        }
        
        if (this.headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(this.headers.get("Content-Length"));
            char[] bodyArray = new char[contentLength];
            reader.read(bodyArray);
            this.body = "";
            for (char character : bodyArray) {
                this.body += character;
            }
        }
    }
}
