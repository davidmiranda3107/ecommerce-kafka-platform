package com.david.ecommerce.user.repository;

import com.david.ecommerce.user.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByEmail() {
        User user = new User();
        user.setUsername("repo-test");
        user.setEmail("repo@test.com");
        user.setPassword("pass");

        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("repo-test");

        assertTrue(found.isPresent());
        assertEquals("repo@test.com", found.get().getEmail());
    }


}
