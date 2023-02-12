package com.chung.receiptsmanager.integrationTests;

import com.chung.receiptsmanager.ReceiptsManagerApplication;
import com.chung.receiptsmanager.dto.CreateUserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static com.chung.receiptsmanager.utils.HeadersUtil.generateBasicAuthHeaderValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ReceiptsManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AuthenticationAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    public static class OverriddenBeansForTest {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return NoOpPasswordEncoder.getInstance();
        }
    }

    @Test
    @SneakyThrows
    void cantCreateUserWithSameUsername() {
        final CreateUserDto dto1 = CreateUserDto.builder()
                .username("AmberUsername")
                .firstName("Amber")
                .lastName("Charlie")
                .password("password")
                .emailAddress("amber@email.com")
                .city("Amber City")
                .build();

        final CreateUserDto dto2 = CreateUserDto.builder()
                .username("AmberUsername")
                .firstName("Amber2")
                .lastName("Charlie2")
                .password("password2")
                .emailAddress("amber2@email.com")
                .city("Amber City 2")
                .build();

        mockMvc.perform(post("/api/auth/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto1)));

        mockMvc.perform(post("/api/auth/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)))
                    .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    void cantCreateUserWithSameEmailAddress() {
        final CreateUserDto dto1 = CreateUserDto.builder()
                .username("AmberUsername")
                .firstName("Amber")
                .lastName("Charlie")
                .password("password")
                .emailAddress("amber@email.com")
                .city("Amber City")
                .build();

        final CreateUserDto dto2 = CreateUserDto.builder()
                .username("AmberUsername2")
                .firstName("Amber2")
                .lastName("Charlie2")
                .password("password2")
                .emailAddress("amber@email.com")
                .city("Amber City 2")
                .build();

        mockMvc.perform(post("/api/auth/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto1)));

        mockMvc.perform(post("/api/auth/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto2)))
                .andExpect(status().isConflict());
    }

}