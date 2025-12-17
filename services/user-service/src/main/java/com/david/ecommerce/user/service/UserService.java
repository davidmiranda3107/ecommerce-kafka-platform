package com.david.ecommerce.user.service;

import com.david.ecommerce.user.dto.UserRequest;
import com.david.ecommerce.user.dto.UserResponse;
import com.david.ecommerce.user.entity.Role;
import com.david.ecommerce.user.entity.User;
import com.david.ecommerce.user.event.UserCreatedEvent;
import com.david.ecommerce.user.kafka.UserEventProducer;
import com.david.ecommerce.user.repository.RoleRepository;
import com.david.ecommerce.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventProducer userEventProducer;

    public UserResponse createUser(UserRequest request) {
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = new User();
        user.setUsername(request.getEmail());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));

        User saved = userRepository.save(user);

        userEventProducer.publishUserCreated(new UserCreatedEvent(
                user.getId(), user.getUsername(), user.getEmail()
        ));

        return new UserResponse(saved.getId(), saved.getEmail(), List.of(role.getName()));
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        return new UserResponse(user.getId(), user.getEmail(), roles);
    }
}
