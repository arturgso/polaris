package io.vexis.polaris.application.handlers;

import io.vexis.polaris.domain.exceptions.OperationNotImplementedException;
import io.vexis.polaris.domain.exceptions.ResourceNotFoundException;
import io.vexis.polaris.domain.exceptions.ShoppingListNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ShoppingListNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleShoppingListNotFound(
      ShoppingListNotFoundException exception, HttpServletRequest request) {
    return build(HttpStatus.NOT_FOUND, "Lista de compras não encontrada.", request);
  }

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

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
      DataIntegrityViolationException exception, HttpServletRequest request) {
    return build(HttpStatus.CONFLICT, "Não foi possível salvar por conflito de dados.", request);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationException(
      AuthenticationException exception, HttpServletRequest request) {
    return build(HttpStatus.UNAUTHORIZED, "Credenciais inválidas.", request);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(
      AccessDeniedException exception, HttpServletRequest request) {
    return build(HttpStatus.FORBIDDEN, "Acesso negado.", request);
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
