package hospital.hospital_system.config;

import hospital.hospital_system.entity.Role;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.enums.PermissionEnum;
import hospital.hospital_system.enums.RoleEnum;
import hospital.hospital_system.repository.RoleRepository;
import hospital.hospital_system.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class SuperAdminInitializer {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeSuperAdmin() {
        Role adminRole = roleRepository.findByRoleEnum(RoleEnum.ADMIN)
                .orElseGet(
                        () -> {
                            Role role = new Role();
                            role.setRoleEnum(RoleEnum.ADMIN);
                            role.setPermissions(Set.of(
                                    PermissionEnum.values()
                            ));
                            return roleRepository.save(role);
                        }
                );

        if (userRepository.findByUsername("superadmin").isEmpty()) {
            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("superpassword"));
            superAdmin.setEmail("superadmin@example.com");
            superAdmin.setRole(adminRole);
            superAdmin.setVerified(true);

            userRepository.save(superAdmin);
            System.out.println("Super Admin created with username: superadmin and password: superpassword");
        }
    }
}

