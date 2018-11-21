package io.github.rxcats.springvertxwebdemo.domain.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    // @formatter:off
    ok(0, "Success"),
    error(900001, "Service Error"),
    ;
    // @formatter:on

    final int code;
    final String message;

}
