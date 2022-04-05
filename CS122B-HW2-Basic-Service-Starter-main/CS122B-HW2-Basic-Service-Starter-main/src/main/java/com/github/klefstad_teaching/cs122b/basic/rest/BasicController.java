package com.github.klefstad_teaching.cs122b.basic.rest;

import com.github.klefstad_teaching.cs122b.basic.config.BasicServiceConfig;
import com.github.klefstad_teaching.cs122b.basic.response.HelloResponse;
import com.github.klefstad_teaching.cs122b.basic.response.ReverseResponse;
import com.github.klefstad_teaching.cs122b.basic.util.Validate;
import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.BasicResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.*;

@RestController
public class BasicController {
    private final BasicServiceConfig config;
    private final Validate validate;

    @Autowired
    public BasicController(BasicServiceConfig config,
            Validate validate) {
        this.config = config;
        this.validate = validate;
    }

    @GetMapping("/hello")
    public ResponseEntity<HelloResponse> hello() {

        HelloResponse body = new HelloResponse()
                .setGreeting(config.greetingMessage())
                .setResult(BasicResults.HELLO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(body);
    }

    @GetMapping("/reverse/{message}")
    public ResponseEntity<ReverseResponse> reverse(@PathVariable String message) {
        String rev = new StringBuilder(message).reverse().toString();
        ReverseResponse good = new ReverseResponse()
                .setReversed(rev)
                .setResult(BasicResults.STRING_SUCCESSFULLY_REVERSED);

        if (message.isEmpty() == true || message.equals(" ")) {
            throw new ResultError(BasicResults.STRING_IS_EMPTY);
        }

        String regex = "^[a-zA-Z0-9_\\s]+$";

        if (message.matches(regex) == false) {
            throw new ResultError(BasicResults.STRING_CONTAINS_INVALID_CHARACTERS);

        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(good);

    }

}
