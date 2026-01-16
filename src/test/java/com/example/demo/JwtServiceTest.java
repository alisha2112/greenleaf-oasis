package com.example.demo;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.security.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void testTokenGenerationAndExtraction() {
        // 1. Створюємо "фейкового" користувача (не зберігаючи в БД)
        User user = User.builder()
                .email("test@greenleaf.com")
                .password("password123")
                .role(Role.USER)
                .build();

        // 2. Генеруємо токен
        String token = jwtService.generateToken(user);

        // 3. Виводимо токен у консоль, щоб ти могла його побачити
        System.out.println("-----------------------------------------------------------");
        System.out.println("GENERATED TOKEN: " + token);
        System.out.println("-----------------------------------------------------------");

        // 4. Перевіряємо, що токен не пустий
        Assertions.assertNotNull(token);
        Assertions.assertFalse(token.isEmpty());

        // 5. Перевіряємо, чи можемо ми витягнути з нього email
        String extractedUsername = jwtService.extractUsername(token);
        Assertions.assertEquals("test@greenleaf.com", extractedUsername);

        // 6. Перевіряємо валідацію
        Assertions.assertTrue(jwtService.isTokenValid(token, user));
    }
}