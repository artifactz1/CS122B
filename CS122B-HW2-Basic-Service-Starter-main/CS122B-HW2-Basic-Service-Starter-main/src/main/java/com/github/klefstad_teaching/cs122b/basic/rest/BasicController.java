package com.github.klefstad_teaching.cs122b.basic.rest;

import com.github.klefstad_teaching.cs122b.basic.config.BasicServiceConfig;
import com.github.klefstad_teaching.cs122b.basic.response.HelloResponse;
import com.github.klefstad_teaching.cs122b.basic.util.Validate;
import com.github.klefstad_teaching.cs122b.core.result.BasicResults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
