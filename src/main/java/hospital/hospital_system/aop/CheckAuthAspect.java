package hospital.hospital_system.aop;

import hospital.hospital_system.entity.PositionPermission;
import hospital.hospital_system.entity.User;
import hospital.hospital_system.enums.PermissionEnum;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Order(1)
@Aspect
@Component
public class CheckAuthAspect {

    @Before(value = "@annotation(checkAuth)")
    public void checkAuthExecutor(CheckAuth checkAuth) {
        check(checkAuth);
    }

    public void check(CheckAuth checkAuth) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication) || authentication.getPrincipal().toString().equals("anonymousUser"))
            throw new AccessDeniedException("Access denied");

        User user = (User) authentication.getPrincipal();

        PermissionEnum[] mustHavePermissions = checkAuth.permissions();

        if (Objects.isNull(mustHavePermissions) || mustHavePermissions.length == 0) {
            return;
        }

        List<PermissionEnum> userPermissions = user.getPosition()
                .getPositionPermissionList()
                .stream()
                .map(PositionPermission::getPermission)
                .toList();

        for (PermissionEnum mustHavePermission : mustHavePermissions) {
            if (userPermissions.contains(mustHavePermission)) {
                return;
            }
        }

        throw new AccessDeniedException("Access denied");

    }

}