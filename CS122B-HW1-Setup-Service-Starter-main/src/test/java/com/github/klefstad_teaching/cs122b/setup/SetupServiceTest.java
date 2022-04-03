package com.github.klefstad_teaching.cs122b.setup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class SetupServiceTest
{
    private final MockMvc mockMvc;

    @Autowired
    public SetupServiceTest(MockMvc mockMvc)
    {
        this.mockMvc = mockMvc;
    }

    @Test
    public void applicationLoads() { }
}
