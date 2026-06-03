package io.vexis.polaris.application.handlers;

import io.vexis.polaris.domain.exceptions.OperationNotImplementedException;
import io.vexis.polaris.domain.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(
      ResourceNotFoundException exception, HttpServletRequest request) {
    return build(HttpStatus.NOT_FOUND, "Recurso não encontrado.", request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception, HttpServletRequest request) {
    return build(HttpStatus.BAD_REQUEST, "Verifique os dados enviados.", request);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
      HttpMessageNotReadableException exception, HttpServletRequest request) {
    return build(HttpStatus.BAD_REQUEST, "Não foi possível ler os dados enviados.", request);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(
      IllegalArgumentException exception, HttpServletRequest request) {
    return build(HttpStatus.BAD_REQUEST, "Algum dado enviado é inválido.", request);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
    return build(HttpStatus.BAD_REQUEST, "Algum dado enviado é inválido.", request);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestParameter(
      MissingServletRequestParameterException exception, HttpServletRequest request) {
    return build(HttpStatus.BAD_REQUEST, "Algum dado enviado é inválido.", request);
  }

  @ExceptionHandler(OperationNotImplementedException.class)
  public ResponseEntity<ErrorResponse> handleOperationNotImplemented(
      OperationNotImplementedException exception, HttpServletRequest request) {
    return build(HttpStatus.NOT_IMPLEMENTED, "Esta operação ainda não está disponível.", request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(
      Exception exception, HttpServletRequest request) {
    return build(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Ocorreu um erro inesperado. Tente novamente mais tarde.",
        request);
  }

  private ResponseEntity<ErrorResponse> build(
      HttpStatus status, String message, HttpServletRequest request) {
    var response =
        new ErrorResponse(
            status.value(),
            status.getReasonPhrase(),
            message,
            request.getRequestURI(),
            Instant.now());
    return ResponseEntity.status(status).body(response);
  }
}
