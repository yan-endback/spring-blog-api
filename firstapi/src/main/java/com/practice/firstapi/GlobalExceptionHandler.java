package com.practice.firstapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import org.springframework.security.access.AccessDeniedException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PostNotFoundException.class)
    public ProblemDetail notFound(PostNotFoundException e){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,e.getMessage()
        );
        pd.setTitle("Не найдено");
        return pd;
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail badType(MethodArgumentTypeMismatchException e){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, e.getMessage()
        );
        pd.setTitle("Неверный запрос");
        return pd;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail invalid(MethodArgumentNotValidException e){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Ошибка валидации"
        );
        pd.setTitle("Неверный запрос");

        Map<String,String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a,b) -> a
                ));
        pd.setProperty("errors",errors);
        return pd;
    }
    @ExceptionHandler(Exception.class)
    public ProblemDetail unexpected(Exception e){
        log.error("Непредвиденная ошибка: ", e);
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,"Внутренняя ошибка сервера"
        );
        pd.setTitle("Ошибка сервера");
        return pd;
    }
    @ExceptionHandler(AuthorNotFoundException.class)
    public ProblemDetail notFoundAuthor(AuthorNotFoundException e){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, e.getMessage()
        );
        pd.setTitle("Не найдено");
        return pd;
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail badCredentials(BadCredentialsException e){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, e.getMessage()
        );
        pd.setTitle("Неверные данные");
        return pd;
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handle(UserAlreadyExistsException e){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, e.getMessage()
        );
        pd.setTitle("Конфликт данных");
        return pd;
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail accessDetail(AccessDeniedException e){
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        pd.setTitle("Доступ запрещен");
        return pd;

    }
}
