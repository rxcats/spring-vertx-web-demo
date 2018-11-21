package io.github.rxcats.springvertxwebdemo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.github.rxcats.springvertxwebdemo.domain.message.ResultCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -5244422173847256626L;

    private ResultCode resultCode;

    private String details;

    public ServiceException(ResultCode resultCode) {
        this(resultCode, null);
    }

    public ServiceException(ResultCode resultCode, String details) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.details = details;
    }

}
