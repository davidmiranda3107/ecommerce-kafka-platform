package com.david.ecommerce.user.service;

import com.david.ecommerce.user.dto.UserRequest;
import com.david.ecommerce.user.dto.UserResponse;
import com.david.ecommerce.user.entity.Role;
import com.david.ecommerce.user.entity.User;
import com.david.ecommerce.user.kafka.UserEventProducer;
import com.david.ecommerce.user.repository.RoleRepository;
import com.david.ecommerce.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserEventProducer userEventProducer;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() {
        UserRequest request = new UserRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");

        Role role = new Role();
        role.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER"))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded");

        when(userRepository.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        UserResponse response = userService.createUser(request);

        assertNotNull(response);
        assertEquals("test@test.com", response.getEmail());
        assertTrue(response.getRoles().contains("ROLE_USER"));
    }
}
