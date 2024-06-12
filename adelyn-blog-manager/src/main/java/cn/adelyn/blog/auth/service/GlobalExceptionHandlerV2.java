package cn.adelyn.blog.auth.service;

import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.response.ResponseEnum;
import cn.adelyn.framework.core.response.ServerResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author chengze
 * @date 2022/8/17
 * @desc 全局异常处理 http响应状态全部定为200
 */
@Slf4j
@RestControllerAdvice
@Order(Integer.MIN_VALUE)
public class GlobalExceptionHandlerV2 {

    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
    public ResponseEntity<ServerResponse<List<String>>> methodArgumentNotValidExceptionHandler(Exception e) {
        log.error("methodArgumentNotValidExceptionHandler", e);
        List<FieldError> fieldErrors = null;
        if (e instanceof MethodArgumentNotValidException) {
            fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
        }
        if (e instanceof BindException) {
            fieldErrors = ((BindException) e).getBindingResult().getFieldErrors();
        }

        List<String> defaultMessages = new ArrayList<>(fieldErrors.size());
        for (FieldError fieldError : fieldErrors) {
            defaultMessages.add(fieldError.getField() + ":" + fieldError.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ServerResponse.fail(ResponseEnum.METHOD_ARGUMENT_NOT_VALID, defaultMessages));
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<ServerResponse<List<String>>> methodArgumentNotValidExceptionHandler(
            ConstraintViolationException e) {
        log.error("methodArgumentNotValidExceptionHandler", e);
        Set<ConstraintViolation<?>> fieldErrors = e.getConstraintViolations();
        List<String> defaultMessages = new ArrayList<>(fieldErrors.size());
        for (ConstraintViolation fieldError : fieldErrors) {
            defaultMessages.add(fieldError.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ServerResponse.fail(ResponseEnum.METHOD_ARGUMENT_NOT_VALID, defaultMessages));
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<ServerResponse<List<FieldError>>> methodArgumentNotValidExceptionHandler(
            HttpMessageNotReadableException e) {
        log.error("methodArgumentNotValidExceptionHandler", e);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ServerResponse.fail(ResponseEnum.HTTP_MESSAGE_NOT_READABLE));
    }

    @ExceptionHandler(AdelynException.class)
    public ResponseEntity<ServerResponse<Object>> adelynExceptionHandler(AdelynException e) {
        log.error("adelynExceptionHandler", e);
        ResponseEnum responseEnum = e.getResponseEnum();
        // 失败返回失败消息 + 状态码
        if (responseEnum != null) {
            return ResponseEntity.status(HttpStatus.OK).body(ServerResponse.fail(responseEnum, e.getObject()));
        }
        // 失败返回消息 状态码固定为直接显示消息的状态码
        return ResponseEntity.status(HttpStatus.OK).body(ServerResponse.fail(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServerResponse<Object>> exceptionHandler(Exception e) {
        log.error("exceptionHandler", e);
/*        log.info("RootContext.getXID(): " + RootContext.getXID());
        if (StrUtil.isNotBlank(RootContext.getXID())) {
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
        }*/
        return ResponseEntity.status(HttpStatus.OK).body(ServerResponse.fail(ResponseEnum.EXCEPTION));
    }
}
