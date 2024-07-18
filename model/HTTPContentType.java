package model;

// Enum built based off of:
// https://stackoverflow.com/a/48704300
public enum HTTPContentType {
    PLAIN("text/plain"),
    HTML("text/html"),
    CSS("text/css"),
    JSON("application/json"),
    PNG("img/png"),
    MP4("video/mp4");

    private final String type;

    HTTPContentType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
