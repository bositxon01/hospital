package hospital.hospital_system.controller;

import hospital.hospital_system.aop.CheckAuth;
import hospital.hospital_system.payload.ApiResult;
import hospital.hospital_system.payload.HomeDto;
import hospital.hospital_system.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @CheckAuth
    @GetMapping
    public ResponseEntity<?> home() {
        ApiResult<HomeDto> home = homeService.getHome();
        return ResponseEntity.ok(home);
    }
}
