package com.project.shopapp.configuaration;

import com.project.shopapp.entity.Role;
import com.project.shopapp.entity.User;
import com.project.shopapp.enums.ROLE;
import com.project.shopapp.repository.RoleRepository;
import com.project.shopapp.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j //log
public class ApplicationInitConfig {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmail("admin").isEmpty()){
                Optional<Role> role = Optional.ofNullable(roleRepository.findByName(ROLE.ADMIN.name()));
//                var roles = new HashSet<Role>();
//                roles.add(role);
                Role adminRole;

                if (role.isEmpty()) {
                    // Nếu role ADMIN chưa tồn tại, tạo mới và lưu vào repository
                    adminRole = new Role();
                    adminRole.setName(ROLE.ADMIN.name());
                    roleRepository.save(adminRole);
                } else {
                    adminRole = role.get();
                }

                User user = User.builder()
                        .email("admin")
                        .password(passwordEncoder.encode("admin"))
                        .role(adminRole)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin");
            }
        };
    }
}
