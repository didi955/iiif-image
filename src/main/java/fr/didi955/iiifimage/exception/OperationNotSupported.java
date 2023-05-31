package fr.didi955.iiifimage.exception;

public class OperationNotSupported extends RuntimeException {

    private final String message;

    public OperationNotSupported(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
