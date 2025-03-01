package hospital.hospital_system.aop;

import hospital.hospital_system.enums.PermissionEnum;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
@Component
public @interface CheckAuth {
    PermissionEnum[] permissions() default {};
}