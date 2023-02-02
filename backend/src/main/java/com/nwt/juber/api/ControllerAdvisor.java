package com.nwt.juber.api;

import com.nwt.juber.exception.*;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvisor {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseError handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        e.getBindingResult().getGlobalErrors().forEach(error ->
                errors.put(error.getObjectName(), error.getDefaultMessage())
        );

        return new ResponseError(HttpStatus.BAD_REQUEST, "Field validation failed.", errors);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseError handleEmailAlreadyInUseException(EmailAlreadyInUseException e) {
        return new ResponseError(HttpStatus.CONFLICT, "Email address is used by another user.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(PhoneNumberAlreadyInUseException.class)
    public ResponseError handlePhoneNumberAlreadyInUseException(PhoneNumberAlreadyInUseException e) {
        return new ResponseError(HttpStatus.CONFLICT, "Phone number is used by another user.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseError handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseError(HttpStatus.NOT_FOUND, "User not found.");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ResponseError handleJwtExceptions(JwtException e) {
        return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }
    
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidPasswordRequestException.class)
    public ResponseError handleInvalidPasswordRequestException(InvalidPasswordRequestException e) {
        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidRecoveryTokenException.class)
    public ResponseError handleInvalidRecoveryTokenException(InvalidRecoveryTokenException e) {
        return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidVerificationTokenException.class)
    public ResponseError handleInvalidVerificationTokenException(InvalidVerificationTokenException e) {
        return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(FileUploadException.class)
    public ResponseError handleFileUploadException(FileUploadException e) {
        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseError handleFileNotFoundException(FileNotFoundException e) {
        return new ResponseError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ConversationNotFoundException.class)
    public ResponseError handleConversationNotFoundException(ConversationNotFoundException e) {
        return new ResponseError(HttpStatus.NOT_FOUND, "Conversation not found.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProfileChangeRequestNotFoundException.class)
    public ResponseError handleProfileChangeRequestNotFoundException(ProfileChangeRequestNotFoundException e) {
        return new ResponseError(HttpStatus.NOT_FOUND, "Change request not found.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ProfileChangeRequestAlreadyResolvedException.class)
    public ResponseError handleProfileChangeRequestAlreadyResolvedException(ProfileChangeRequestAlreadyResolvedException e) {
        return new ResponseError(HttpStatus.CONFLICT, "Profile change request is already resolved.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyBlockedException.class)
    public ResponseError handleUserAlreadyBlockedException(UserAlreadyBlockedException e) {
        return new ResponseError(HttpStatus.CONFLICT, "User is already blocked.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyInRideException.class)
    public ResponseError handleUserAlreadyInRideException(UserAlreadyInRideException e) {
        return new ResponseError(HttpStatus.CONFLICT, "User is already in ride.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseError handleDriverNotFoundException(DriverNotFoundException e) {
        return new ResponseError(HttpStatus.NOT_FOUND, "Driver not found.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseError handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException e) {
        return new ResponseError(HttpStatus.CONFLICT, "Confilct. Try again later!");
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseError handleInsufficientFundsException(InsufficientFundsException e) {
        return new ResponseError(HttpStatus.NOT_ACCEPTABLE, "Insufficient funds.");
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(EndRideException.class)
    public ResponseError handleEndRideException(EndRideException e) {
        return new ResponseError(HttpStatus.NOT_ACCEPTABLE, "Error while ending ride.");
    }


    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(StartRideException.class)
    public ResponseError handleStartRideException(StartRideException e) {
        return new ResponseError(HttpStatus.NOT_ACCEPTABLE, "Error while starting ride.");
    }

}
