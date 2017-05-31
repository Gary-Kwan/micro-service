package com.core.support.web.domain;

import com.alibaba.fastjson.JSON;
import com.core.exception.MicroException;
import com.core.exception.ErrorHolder.CodeTemp;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * ClassName: ApiResult <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 *
 * @author 半轴
 * @date: 2016年5月31日 下午12:44:20 <br/>
 * @since JDK 1.8
 */
@Data
public class ErrorResult implements Serializable {

    private static final long serialVersionUID = -1514168386299353908L;
    private String exception;
    private String code = CodeTemp.SUCCESS.getCode();
    private String message;
    private Integer httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public ErrorResult() {
    }

    public ErrorResult(MicroException exception, HttpStatus status) {
        this(exception.getClass().getName(), exception.getMessage(), exception.getErrorCode(), status);
    }

    public ErrorResult(String exception, String message, String errorCode, HttpStatus status) {
        this.message = message;
        this.code = errorCode;
        this.exception = exception;
        this.httpStatus = status.value();
    }

    public ErrorResult(MicroException exception) {
        this(exception.getClass().getName(), exception.getMessage(), exception.getErrorCode());
    }

    public ErrorResult(String exception, String message, String errorCode) {
        this(exception, message, errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this).concat(super.toString());
    }
}
