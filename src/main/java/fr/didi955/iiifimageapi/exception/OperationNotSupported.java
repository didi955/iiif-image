package fr.didi955.iiifimageapi.exception;

public class OperationNotSupported extends RuntimeException {

    private final String message;

    public OperationNotSupported(String message) {
        super(message);
        this.message = message;
    }
}
