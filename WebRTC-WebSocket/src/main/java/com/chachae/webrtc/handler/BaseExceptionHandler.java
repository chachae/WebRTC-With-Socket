package com.chachae.webrtc.handler;

import com.chachae.webrtc.common.ApiException;
import com.chachae.webrtc.common.R;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author chachae
 * @version v1.0
 * @date 2020/8/13 22:28
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected R<String> handleException(Exception e) {
    log.error("系统内部异常，异常信息", e);
    return R.fail("系统内部异常");
  }

  @ExceptionHandler(value = ApiException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected R<String> handleApiException(ApiException e) {
    log.error("系统错误", e);
    return R.fail(e.getMessage());
  }

  /**
   * 统一处理请求参数校验(实体对象传参)
   *
   * @param e BindException
   * @return R<String>
   */
  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected R<String> handleBindException(BindException e) {
    StringBuilder message = new StringBuilder();
    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    for (FieldError error : fieldErrors) {
      message.append(error.getField()).append(error.getDefaultMessage()).append(",");
    }
    message = new StringBuilder(message.substring(0, message.length() - 1));
    return R.fail(message.toString());
  }

  /**
   * 统一处理请求参数校验(普通传参)
   *
   * @param e ConstraintViolationException
   * @return R<String>
   */
  @ExceptionHandler(value = ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected R<String> handleConstraintViolationException(ConstraintViolationException e) {
    StringBuilder message = new StringBuilder();
    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
    for (ConstraintViolation<?> violation : violations) {
      Path path = violation.getPropertyPath();
      String[] pathArr = StringUtils.split(path.toString(), '.');
      message.append(pathArr[1]).append(violation.getMessage()).append(',');
    }
    message = new StringBuilder(message.substring(0, message.length() - 1));
    return R.fail(message.toString());
  }

  /**
   * 统一处理请求参数校验(json)
   *
   * @param e ConstraintViolationException
   * @return R<String>
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  protected R<String> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    StringBuilder message = new StringBuilder();
    for (FieldError error : e.getBindingResult().getFieldErrors()) {
      message.append(error.getField()).append(error.getDefaultMessage()).append(",");
    }
    message = new StringBuilder(message.substring(0, message.length() - 1));
    log.error(message.toString(), e);
    return R.fail(message.toString());
  }

  @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected R<String> handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException e) {
    return R.fail("该方法不支持" + StringUtils.substringBetween(e.getMessage(), "'", "'") + "媒体类型");
  }

  @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  protected R<String> handleHttpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException e) {
    return R.fail("该方法不支持" + StringUtils.substringBetween(e.getMessage(), "'", "'") + "请求方法");
  }

}