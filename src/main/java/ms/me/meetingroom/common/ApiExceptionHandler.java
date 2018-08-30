package ms.me.meetingroom.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
@ResponseBody
public class ApiExceptionHandler {
    static Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler({RuntimeException.class})
    private ResponseEntity<Response> handleApiException(Exception e) {
        log.error("{}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.fail(e.getMessage()));
    }

    @ExceptionHandler({ReservationException.class})
    private ResponseEntity<Response> handleValidException(Exception e) {
        log.error("{}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.fail(e.getMessage()));
    }
}
