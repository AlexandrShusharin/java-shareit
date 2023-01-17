package ru.practicum.shareit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.exceptions.*;

import javax.validation.ValidationException;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

/*    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentException(MethodArgumentTypeMismatchException e) {
        String message = "Unknown state: " + e.getValue().toString();
        log.error(message);
        return new ErrorResponse(message);
    }
*/

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class,
            ObjectIncorrectArguments.class, MethodArgumentTypeMismatchException.class,
            BookingStatusProcessedException.class, UserNotItemBookerException.class})
        public ErrorResponse handleValidationException(Exception e) {
        String message;
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException eValidation = (MethodArgumentNotValidException) e;
            message = Objects.requireNonNull(eValidation.getBindingResult().getFieldError()).getDefaultMessage();
        } else if (e instanceof MethodArgumentTypeMismatchException) {
            MethodArgumentTypeMismatchException eValidation = (MethodArgumentTypeMismatchException) e;
            message = "Unknown state: " + eValidation.getValue().toString();
        } else {
            message = e.getMessage();
        }
        log.error(message);
        return new ErrorResponse(message);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ObjectNotFoundException.class, UserNotOwnerException.class, BookerIsItemOwnerException.class})
    public ErrorResponse handleObjectNotFoundException(Exception e) {
        log.error(e.getMessage());
        System.out.println(e.getClass());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}
