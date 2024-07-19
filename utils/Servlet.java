package utils;

import model.State;

public interface Servlet {
    void service(Request req, Response res, State state);
}
