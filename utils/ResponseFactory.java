package utils;

import java.io.OutputStream;
import model.HTTPContentType;
import model.HTTPStatus;

public class ResponseFactory {
    private Response res;
    
    /*
     * Constructors
     */
    public ResponseFactory(OutputStream outputStream) {
        this.res = new Response(outputStream);
    }

    public ResponseFactory(Response res) {
        this.res = res;
    }

    /*
     * Factory methods
     */
    public ResponseFactory setStatus(HTTPStatus status) {
        this.res.status = status;
        return this;
    }
    
    public ResponseFactory setType(HTTPContentType type) {
        this.res.type = type;
        return this;
    }

    public ResponseFactory setBody(String body) {
        this.res.body = body;
        return this;
    }

    /*
     * Build
     */
    public Response build() {
        return this.res;
    }

    /*
     * Common Responses
     */
    public static Response fileNotFoundResponse(Response res) {
        res.status = HTTPStatus.NOT_FOUND;
        res.type = HTTPContentType.PLAIN;
        res.body = "Could not find file";
        return res;
    }

    public static Response badRequest(Response res, String body) {
        res.status = HTTPStatus.BAD_REQUEST;
        res.type = HTTPContentType.PLAIN;
        res.body = body;
        return res;
    }

}
