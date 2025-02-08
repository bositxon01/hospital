package hospital.hospital_system.payload;

import hospital.hospital_system.entity.Employee;
import hospital.hospital_system.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VerificationInfo {
    private String code;
    private User user;
    private Employee employee;
    private long expiryTime;
    private int attempts;
}
