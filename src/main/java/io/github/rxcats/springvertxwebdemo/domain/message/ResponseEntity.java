package io.github.rxcats.springvertxwebdemo.domain.message;

import lombok.Data;

@Data
public class ResponseEntity<T> {

    int code = ResultCode.ok.getCode();

    T result;

    String message = ResultCode.ok.getMessage();

    String details;

    long timestamp = System.currentTimeMillis();

    private ResponseEntity() {

    }

    private ResponseEntity(T result) {
        this();
        this.result = result;
    }

    private ResponseEntity(ResultCode code, T result) {
        this(result);
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    private ResponseEntity(ResultCode code, T result, String details) {
        this(result);
        this.code = code.getCode();
        this.message = code.getMessage();
        this.details = details;
    }

    public static <T> ResponseEntity<T> of() {
        return new ResponseEntity<>();
    }

    public static <T> ResponseEntity<T> of(T result) {
        return new ResponseEntity<>(result);
    }

    public static <T> ResponseEntity<T> of(ResultCode code, T result) {
        return new ResponseEntity<>(code, result);
    }

    public static <T> ResponseEntity<T> of(ResultCode code, T result, String details) {
        return new ResponseEntity<>(code, result, details);
    }

    public static <T> ResponseEntity<T> error(String details) {
        return new ResponseEntity<>(ResultCode.error, null, details);
    }

    public ResponseEntity code(ResultCode code) {
        this.code = code.getCode();
        return this;
    }

    public ResponseEntity result(T result) {
        this.result = result;
        return this;
    }

    public ResponseEntity message(String message) {
        this.message = message;
        return this;
    }

    public ResponseEntity details(String details) {
        this.details = details;
        return this;
    }

}
