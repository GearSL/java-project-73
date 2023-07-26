package hexlet.code.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private Date timestamp;

    public ErrorResponse(int status, String error) {
        this.status = status;
        this.message = error;
        this.timestamp = new Date();
    }
}
