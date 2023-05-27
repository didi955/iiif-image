package fr.didi955.iiifimageapi.exception;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class BadRequestException extends RuntimeException {

    private final String message;

    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }

    public ResponseEntity<String> sendResponse(){
        String json = "{\"error\":\"" + this.message + "\"}";
        return ResponseEntity.status(NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body(json);
    }


}
