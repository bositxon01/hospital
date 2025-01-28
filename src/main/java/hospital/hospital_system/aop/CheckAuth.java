package hospital.hospital_system.aop;

import hospital.hospital_system.enums.PermissionEnum;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface CheckAuth {
    PermissionEnum[] permissions() default {};
}
