package hospital.hospital_system.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "users")
public class User extends AbsIntegerEntity implements UserDetails {
    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    private Position position;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return position.getPositionPermissionList()
                .stream()
                .map(PositionPermission::getPermission)
                .map(permissionEnum -> new SimpleGrantedAuthority(permissionEnum.name()))
                .toList();
    }

    @OneToOne(mappedBy = "user")
    private Employee employee;

    @OneToOne(mappedBy = "user")
    private Patient patient;
}
