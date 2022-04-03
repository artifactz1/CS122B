package com.github.klefstad_teaching.cs122b.setup;

import com.github.klefstad_teaching.cs122b.core.spring.StackService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@StackService
public class SetupService
{
    public static void main(String[] args)
    {
        SpringApplication.run(SetupService.class, args);
    }
}
