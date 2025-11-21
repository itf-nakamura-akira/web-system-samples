package jp.co.itfllc.WebSystemSamples.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TestController {

    @GetMapping("/response-status-exception")
    public void throwResponseStatusException() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
    }

    @GetMapping("/exception")
    public void throwException() throws Exception {
        throw new Exception("Some Exception");
    }
}
