package modelo;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class Error {
    private String error;
    private Date timestamp;
    private int status;

    public Error(String error, HttpStatus status){
        this.timestamp = new Date();
        this.error = error;
        this.status = status.value();
    }
    public Error(){}
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
