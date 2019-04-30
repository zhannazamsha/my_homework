package homework.api;


import homework.exceptions.ApplicationNotFoundException;
import homework.exceptions.CompanyInBlacklistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class LoanApplicationErrorAdvice {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleRunTimeException(RuntimeException e) {
        return error(INTERNAL_SERVER_ERROR, e);
    }

    @ExceptionHandler({ApplicationNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(ApplicationNotFoundException e) {
        return error(NOT_FOUND, e);
    }

    @ExceptionHandler({CompanyInBlacklistException.class})
    public ResponseEntity<String> handleCompanyInBlacklistException(CompanyInBlacklistException e) {
        return error(METHOD_NOT_ALLOWED, e);
    }

    private ResponseEntity<String> error(HttpStatus status, Exception e) {
        log.error("Exception : ", e);
        return ResponseEntity.status(status).body(e.getMessage());
    }
}
