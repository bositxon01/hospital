package hospital.hospital_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "users")

@SQLRestriction(value = "is_deleted=false")
@SQLDelete(sql = ("update users set is_deleted=true where id=?"))
public class User extends AbsIntegerEntity implements UserDetails {
    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @NotBlank
    @Column(nullable = false)
    private String password;

    @JsonIgnore
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

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private Employee employee;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Patient> patients;
}
