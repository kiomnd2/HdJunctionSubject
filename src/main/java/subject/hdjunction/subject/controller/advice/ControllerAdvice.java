package subject.hdjunction.subject.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subject.hdjunction.subject.controller.response.Response;
import subject.hdjunction.subject.exception.NotFoundHospitalException;
import subject.hdjunction.subject.exception.NotFoundPatientException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundPatientException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<String>> validate(NotFoundPatientException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.fail(e.getMessage()));
    }

    @ExceptionHandler(NotFoundHospitalException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<String>> validate(NotFoundHospitalException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.fail(e.getMessage()));
    }

}
