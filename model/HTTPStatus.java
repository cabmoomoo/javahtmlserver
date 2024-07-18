package model;

public enum HTTPStatus {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HTTPStatus(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code + " " + this.name();
    }
}
